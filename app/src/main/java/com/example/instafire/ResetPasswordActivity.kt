package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*

private const val TAG = "ResetPasswordActivity"
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var firestoreDb: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btnResetPW.setOnClickListener {
            Log.i(TAG, "reset btn clicked")
            btnResetPW.isEnabled = false

            email_edittext_resetPW.setOnClickListener {
                btnResetPW.isEnabled = true
            }

            val email  = email_edittext_resetPW.text.toString()
            val regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
            val result = regex matches email

            if (!result) {
                email_edittext_resetPW.error = "Email is invalid"
                return@setOnClickListener
            }

            if (email.isBlank()) {
                email_edittext_resetPW.error = "Email cannot be empty"
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Oops, something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
            }

            // TODO: checking if user is registered
//            val registeredEmail = firestoreDb.collection("users")
//                .whereEqualTo("email", "yuz101@ucsd.edu")
//            Log.d(TAG, "registered email is $registeredEmail")
        }

        btnToLogin.setOnClickListener {
            Log.i(TAG, "back to login page")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}