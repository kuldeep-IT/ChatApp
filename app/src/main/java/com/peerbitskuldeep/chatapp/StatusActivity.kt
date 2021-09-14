package com.peerbitskuldeep.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mCurrentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        if(intent.extras != null)
        {
            var oldStatus = intent.extras!!.get("status")
            etStatusStatus.setText(oldStatus.toString())
        }
        else
        {
            etStatusStatus.setText("Enter your status:")
        }

        btnStatusStatus.setOnClickListener {

            var status = etStatusStatus.text.toString().trim()

            mAuth = FirebaseAuth.getInstance()
            mCurrentUser = mAuth.currentUser!!
            var uid = mCurrentUser.uid

            mDatabaseReference = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(uid)
                .child("status")

            mDatabaseReference.setValue(status).addOnCompleteListener {task ->

                if (task.isSuccessful)
                {
                    Toast.makeText(applicationContext,"Status updated!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                else
                {
                    Toast.makeText(applicationContext,"Error ${task.exception!!.message}!", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}