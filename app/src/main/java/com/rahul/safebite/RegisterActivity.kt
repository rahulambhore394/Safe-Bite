package com.rahul.safebite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rahul.safebite.databinding.ActivityRegisterBinding

data class User(
    val fullname: String = "",
    val mobileNo: String = "",
    val emergencyMobileNo1: String = "",
    val emergencyMobileNo2: String = "",
    val username: String = "",
    val password: String = ""
)

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("users")

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegister.setOnClickListener {
            val fullname = binding.etNameRegister.text.toString()
            val mobileNo = binding.etPersonalMoNoRegister.text.toString()
            val emergencyMobileNo1 = binding.etEmergency1MobileNoRegister.text.toString()
            val emergencyMobileNo2 = binding.etEmergency2MobileNoRegister.text.toString()
            val username = binding.etUsernameRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()


            if (fullname.isNotEmpty() && mobileNo.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                insertUserData(fullname, mobileNo, emergencyMobileNo1, emergencyMobileNo2, username, password)
            } else {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertUserData(
        fullname: String,
        mobileNo: String,
        emergencyMobileNo1: String,
        emergencyMobileNo2: String,
        username: String,
        password: String
    ) {
        val user = User(fullname, mobileNo, emergencyMobileNo1, emergencyMobileNo2, username, password)
        reference.child(username).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to register user: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
