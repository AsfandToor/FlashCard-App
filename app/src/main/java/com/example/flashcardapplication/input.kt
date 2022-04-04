package com.example.flashcardapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flashcardapplication.databinding.ActivityInputBinding
import com.google.firebase.database.FirebaseDatabase
import java.sql.CallableStatement

class input : AppCompatActivity() {

    lateinit var binding: ActivityInputBinding
    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    lateinit var statement: String
    lateinit var answer: String
    lateinit var userId: String
    var isFront = true
    val db = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        // Getting the User ID
        var userId = intent.getStringExtra("userId").toString()

        // Setting Up Rotation
        front_anim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animation) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animation) as AnimatorSet

        val scale: Float = applicationContext.resources.displayMetrics.density

        binding.inputFrontCard.cameraDistance = scale * 8000
        binding.inputBackCard.cameraDistance = scale * 8000

        binding.inputFrontCard.setOnClickListener {
            if (isFront) {
                front_anim.setTarget(binding.inputFrontCard)
                back_anim.setTarget(binding.inputBackCard)
                front_anim.start()
                back_anim.start()
                isFront = false
                binding.inputBackCard.bringToFront()
            }
        }

        binding.inputBackCard.setOnClickListener {
            if (!isFront) {
                front_anim.setTarget(binding.inputBackCard)
                back_anim.setTarget(binding.inputFrontCard)
                front_anim.start()
                back_anim.start()
                isFront = true
                binding.inputFrontCard.bringToFront()
            }
        }

        // Saving Data to Firebase
        val ref = db.getReference("Users").child(userId).child("FlashCards")

        binding.addBtn.setOnClickListener {
            statement = binding.inputFront.text.toString()
            answer = binding.inputBack.text.toString()
            if (statement.isNotEmpty() && answer.isNotEmpty()) {
                val key = ref.push().key
                ref.child(key!!).setValue(FlashCards(key, statement, answer)).addOnSuccessListener {
                    finish()
                }
            }
            else {
                Toast.makeText(applicationContext, "Please Enter Both Statement & Answer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}