package com.example.flashcardapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcardapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
import kotlin.coroutines.CoroutineContext

lateinit var mycontext: Context
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var list: MutableList<FlashCards>
    var db = FirebaseDatabase.getInstance()
    var auth = FirebaseAuth.getInstance()
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mycontext=applicationContext

        // Navigation Drawer
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        userId = intent.getStringExtra("userId").toString()

        // Manual toggle Button
        binding.menuBtn.setOnClickListener {
            toggleDrawer()
        }

        // Fetching User Data
        db.getReference("Users").child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val user = it.getValue(Users::class.java)
                // Setting Header Text
                binding.navLayout.getHeaderView(0).findViewById<TextView>(R.id.headerName).setText(user!!.name)
            }
        }

        binding.navLayout.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> Toast.makeText(applicationContext, "Item1", Toast.LENGTH_SHORT).show()
                R.id.category -> Toast.makeText(applicationContext, "Item2", Toast.LENGTH_SHORT).show()
                R.id.logout -> {
                    auth.signOut()
                    val intent = Intent(this@MainActivity, login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }


        // Fetching Database from Firebase
        val ref = db.getReference("Users").child(userId).child("FlashCards")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list = mutableListOf<FlashCards>()
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val card = i.getValue(FlashCards::class.java)
                        list.add(card!!)
                    }
                }
                val adapter = ViewPagerAdapter(list)
                binding.viewPager.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        // Adding Flash Card
        binding.imageButton.setOnClickListener {
            val intent = Intent(applicationContext, input::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}