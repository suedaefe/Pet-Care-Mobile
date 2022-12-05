package com.example.petcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var email = findViewById<EditText>(R.id.editText_email_update)
        var password = findViewById<EditText>(R.id.editText_password_update)
        var btnemail = findViewById<Button>(R.id.btn_email_update)
        var btnpassword = findViewById<Button>(R.id.btn_password_update)
        var user = FirebaseAuth.getInstance()
        var btnlogout : ImageButton = findViewById(R.id.btn_logout)
        var btndelete: Button = findViewById(R.id.btn_delete_account)
        var btnback : ImageButton = findViewById(R.id.btn_back_st)


        btnback.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cikis yapmak icin
        btnlogout.setOnClickListener {
            user.signOut()
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //sifre yenileme icin
        btnpassword.setOnClickListener {
            val user = auth.currentUser
            val passwordnew = password.text.toString()
            if(passwordnew!=null){
                user?.updatePassword(passwordnew)?.addOnCompleteListener {
                    if(it.isSuccessful){

                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //mail yenileme icin
        btnemail.setOnClickListener {
            val user = auth.currentUser
            val emailnew = email.text.toString()
            if(emailnew!=null){
                user?.updateEmail(emailnew)?.addOnCompleteListener {
                    if(it.isSuccessful){

                        Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //hesabı silmek icin
        btndelete.setOnClickListener {
            val user = auth.currentUser
            user?.delete()?.addOnCompleteListener {
                if(it.isSuccessful){

                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}