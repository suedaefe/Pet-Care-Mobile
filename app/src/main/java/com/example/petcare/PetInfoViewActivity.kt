package com.example.petcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.petcare.databinding.ActivityPetInfoViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class PetInfoViewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPetInfoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        var btnedit : ImageButton = findViewById(R.id.btn_edit_petinfo)
        btnedit.setOnClickListener {
            val intent= Intent(this, PetInfoSaveActivity::class.java)
            startActivity(intent)
        }

        var btnback : ImageButton = findViewById(R.id.btn_back)
        btnback.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var mAuth = FirebaseAuth.getInstance()
        var u = mAuth.currentUser
        var userId= u?.uid
        var databaseref= FirebaseDatabase.getInstance().reference.child("Pet Info").child(userId.toString())

        var getData = object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var pname = snapshot.child("petname").getValue()
                var pbreed = snapshot.child("petbreed").getValue()
                var pbday = snapshot.child("petbday").getValue()
                var pfm = snapshot.child("petfm").getValue()
                var pweight = snapshot.child("petweight").getValue()

                binding.tvName.setText(pname.toString())
                binding.tvBreed.setText(pbreed.toString())
                binding.tvBday.setText(pbday.toString())
                binding.tvFm.setText(pfm.toString())
                binding.tvWeight.setText(pweight.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        databaseref.addValueEventListener(getData)
        databaseref.addListenerForSingleValueEvent(getData)
    }
}