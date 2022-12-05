package com.example.petcare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.petcare.databinding.ActivityPetInfoSaveBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PetInfoSaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPetInfoSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackhome.setOnClickListener {
            intent = Intent(this, PetInfoViewActivity::class.java)
            startActivity(intent)
        }

        //Bilgileri kaydetmek icin
        binding.btnSave.setOnClickListener {
            var name = binding.editTextPetname.text.toString()
            var breed = binding.editTextPetbreed.text.toString()
            var bday = binding.editTextPetbirthday.text.toString()
            var fm = binding.editTextPetfm.text.toString()
            var weight = binding.editTextPetweight.text.toString()
            var mAuth = FirebaseAuth.getInstance()
            var u = mAuth.currentUser
            var userId = u?.uid
            var databaseref =
                FirebaseDatabase.getInstance().reference.child("Pet Info").child(userId.toString())

            databaseref.setValue(PetInfoData(name, breed, bday, fm, weight)).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Information saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}