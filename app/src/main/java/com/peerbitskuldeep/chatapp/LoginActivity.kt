package com.peerbitskuldeep.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btnRegistration.setOnClickListener {

            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()

        }

        btnLogin.setOnClickListener {

            var email = etEmailLogin.text.toString().trim()
            var password = etPasswordLogin.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
            {
                loginAcc(email, password)
            }
            else
            {
                Toast.makeText(
                    applicationContext,
                    "Invalid credential!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun loginAcc(email: String, pass: String)
    {
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->

                if(task.isSuccessful)
                {

                    Toast.makeText(
                        applicationContext,
                        "Logged In!",
                        Toast.LENGTH_SHORT
                    ).show()

                    var username = email.split("@")[0]

                    var dashboardIntent = Intent(this, DashboardActivity::class.java)
                    dashboardIntent.putExtra("name", username)
                    startActivity(dashboardIntent)
                    finish()

                }
                else
                {
                    Log.d("TAG", "createAccount: ${task.exception!!.message}")
                    Toast.makeText(applicationContext, "Error! + ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                }

            }
    }
}