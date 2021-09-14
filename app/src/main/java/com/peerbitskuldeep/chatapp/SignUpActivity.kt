package com.peerbitskuldeep.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()


        btnSignupSignUp.setOnClickListener {

            var email = etEmailSignUp.text.toString().trim()
            var name = etNameSignUp.text.toString().trim()
            var password = etPassWordSignUp.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(name)
                || !TextUtils.isEmpty(password)
            ) {
                createAccount(email, password, name)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please fill the required fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    fun createAccount(
        email: String,
        password: String,
        displayname: String
    ) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    var currentUser = mAuth.currentUser
                    var uid = currentUser!!.uid

                    mDatabase = FirebaseDatabase.getInstance().reference
                        .child("Users")
                        .child(uid)

                    var userObject = HashMap<String, String>()
                    userObject.put("display_name", displayname)
                    userObject.put("email", email)
                    userObject.put("password", password)
                    userObject.put("image", "defalt")
                    userObject.put("status", "hey whatsup")
                    userObject.put("thumb_img", "defalt")



                    mDatabase!!.setValue(userObject).addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "User Created!", Toast.LENGTH_SHORT)
                                .show()

                            var dashboardIntent = Intent(this, DashboardActivity::class.java)
                            dashboardIntent.putExtra("name", displayname)
                            startActivity(dashboardIntent)

                            finish()

                        } else {

                            Log.d("TAG", "createAccount: ${task.exception!!.message}")
                            Toast.makeText(applicationContext, "Error! + ${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                        }

                    }

                }


            }


    }

}