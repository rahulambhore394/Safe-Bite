package com.developer_rahul.imagedetection

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.image.TensorImage

data class Prediction(val label: String, val confidence: Float)

class ImageClassifier(context: Context) {
    private val labels = FileUtil.loadLabels(context, "labels_mobilenet_quant_v1_224.txt")
    private val interpreter: Interpreter

    init {
        val modelBuffer = FileUtil.loadMappedFile(context, "mobilenet_v1_1.0_224_quant.tflite")
        interpreter = Interpreter(modelBuffer)
    }

    fun classify(bitmap: Bitmap): List<Prediction> {
        val tensorImage = TensorImage.fromBitmap(
            Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        )

        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, labels.size),
            org.tensorflow.lite.DataType.UINT8
        )

        interpreter.run(tensorImage.buffer, outputBuffer.buffer.rewind())
        val scores = outputBuffer.buffer.array().map { (it.toInt() and 0xFF) / 255f }

        return labels.zip(scores)
            .map { Prediction(it.first, it.second) }
            .sortedByDescending { it.confidence }
            .take(3)
    }
}
