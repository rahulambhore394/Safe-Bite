package com.rahul.safebite.SpeciesIdentification

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.developer_rahul.imagedetection.ImageClassifier
import com.developer_rahul.imagedetection.SpeciesInfoFetcher
import com.rahul.safebite.R

class IdentifyFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
    }

    private lateinit var classifier: ImageClassifier
    private lateinit var imageView: ImageView
    private lateinit var resultText: TextView
    private lateinit var infoText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_identify, container, false)

        // 1) Bind views
        imageView    = view.findViewById(R.id.imageView)
        resultText   = view.findViewById(R.id.resultText)
        infoText     = view.findViewById(R.id.infoText)
        val btnSelect  = view.findViewById<Button>(R.id.btnSelect)
        val btnCapture = view.findViewById<Button>(R.id.btnCapture)

        // 2) Load your classifier
        classifier = ImageClassifier(requireContext())

        // 3) Wire up buttons
        btnSelect.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickIntent, REQUEST_IMAGE_PICK)
        }
        btnCapture.setOnClickListener {
            val capIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(capIntent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return

        // 1) Decode Bitmap
        val rawBmp: Bitmap? = when(requestCode) {
            REQUEST_IMAGE_PICK -> {
                val uri: Uri = data.data!!
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                } else {
                    val src = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                    ImageDecoder.decodeBitmap(src)
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                data.extras?.get("data") as? Bitmap
            }
            else -> null
        }

        rawBmp?.let { bmp ->
            // 2) Force ARGB_8888 and display
            val bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true)
            imageView.setImageBitmap(bitmap)

            // 3) Classify & fetch info
            classifyAndFetchInfo(bitmap)
        }
    }

    private fun classifyAndFetchInfo(bitmap: Bitmap) {
        // A) Run the TFLite classifier
        val preds = classifier.classify(bitmap)
        resultText.text = preds.joinToString("\n") {
            "${it.label} (${(it.confidence * 100).toInt()}%)"
        }

        // B) Fetch raw Wikipedia paragraph
        val top = preds.firstOrNull()
        top?.let {
            SpeciesInfoFetcher().getSpeciesInfo(it.label) { info ->
                activity?.runOnUiThread {
                    infoText.text = info
                }
            }
        }
    }
}
