package com.peerbitskuldeep.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth= FirebaseAuth.getInstance()


        btnLoginMain.setOnClickListener {

            startActivity(Intent(applicationContext, LoginActivity::class.java))

        }

        btnSignUpMain.setOnClickListener {

            startActivity(Intent(applicationContext, SignUpActivity::class.java))

        }
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser

        if (currentUser != null)
        {

            Toast.makeText(
                applicationContext,
                "Welcome!",
                Toast.LENGTH_SHORT
            ).show()

            var dashboardIntent = Intent(this, DashboardActivity::class.java)
//            dashboardIntent.putExtra("name", username)
            startActivity(dashboardIntent)
            finish()

        }

    }
}