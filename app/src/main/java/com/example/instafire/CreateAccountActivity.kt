package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instafire.models.Post
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_account.*

private const val TAG = "CreateAccountActivity"
class CreateAccountActivity : AppCompatActivity() {
    private lateinit var firestoreDb: FirebaseFirestore   // points to root of firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        firestoreDb = FirebaseFirestore.getInstance()

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_text_view.setOnClickListener {
            Log.d(TAG, "Try to show login activity")

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        //val is a constant
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        //logs a debug message
        Log.d(TAG, "Email is: " + email)
        Log.d(TAG, "Password: $password")

        //Firebase authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    //if the creation is not successful
                    if (!it.isSuccessful)
                        return@addOnCompleteListener

                    //else if creating user is successful
                    Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                    // TODO: insert user into fireStoreDb users collection
//                    val user = User(
//                        "default_username",
//                        18,
//                        email
//                    )
//                    firestoreDb.collection("users").add(user)

                    //launch profile screen after creating an account
                    val intent = Intent(this, PostActivity::class.java)
                    startActivity(intent)
                    finish()    // remove back stack
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }

    }
}
