package com.example.petcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val logintext: TextView = findViewById(R.id.textView3)

        logintext.setOnClickListener {
            val intent = Intent(this, LoginActivity:: class.java)
            startActivity(intent)
        }
        val registerButton : Button = findViewById(R.id.button_signup)

        registerButton.setOnClickListener {
            performSignUp()
        }

    }
    fun performSignUp(){

        val email = findViewById<EditText>(R.id.editText_signup_email)
        val password = findViewById<EditText>(R.id.editText_signup_password)

        if((email.text.isEmpty())&&(password.text.isEmpty())){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, LoginActivity:: class.java)
                    startActivity(intent)
                    Toast.makeText(baseContext, "Registration successful.",
                        Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(baseContext, "Registration failed.",
                        Toast.LENGTH_SHORT).show()

                }

            }
            .addOnFailureListener{
                Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }


    }
}