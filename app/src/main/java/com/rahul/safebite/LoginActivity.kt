package com.rahul.safebite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rahul.safebite.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNewUser.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val sharedPreferences = getSharedPreferences("SafeBitePrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("users")

        binding.btnLoginLogin.setOnClickListener {
            val username = binding.etUsernameLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        reference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val storedPassword = snapshot.child("password").getValue(String::class.java)
                    if (storedPassword == password) {
                        val userName = snapshot.child("fullname").getValue(String::class.java) ?: "Unknown User"
                        val userMobile = snapshot.child("mobileNo").getValue(String::class.java) ?: "No mobile"

                        val sharedPreferences = getSharedPreferences("SafeBitePrefs", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("userName", userName)
                            putString("userMobile", userMobile)
                            apply()
                        }

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                            putExtra("userName", userName)
                            putExtra("userMobile", userMobile)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Failed to read data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
