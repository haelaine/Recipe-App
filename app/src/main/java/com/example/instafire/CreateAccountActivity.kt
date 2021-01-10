package com.example.instafire

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instafire.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlin.properties.Delegates


// TODO: do not allow duplicated username, it's still buggy

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
        var username = username_edittext_register.text.toString()
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val rePassword = re_password_edittext_register.text.toString()

        var duplicateUsername = false
        firestoreDb.collection("users")
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            if (document.get("username") == username) {
                                username_edittext_register.error = "Username is taken, please use another one."
                                duplicateUsername = true
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                })

        Log.i(TAG, "dup: $duplicateUsername")
        if (duplicateUsername) {
            return
        }

        if (username.isEmpty()) {
            username_edittext_register.error = "Please enter username."
            return
        }

        if (email.isEmpty()) {
            email_edittext_register.error = "Please enter email."
            return
        }

        if (password.isEmpty()) {
            password_edittext_register.error = "Please enter password."
            return
        }

        if (rePassword.isEmpty()) {
            re_password_edittext_register.error = "Please re-enter password."
            return
        }

        if (rePassword != password) {
            re_password_edittext_register.error = "Password and confirm password does not match."
            return
        }

        //logs a debug message
        Log.d(TAG, "Username is:  + $username")
        Log.d(TAG, "Email is:  + $email")
        Log.d(TAG, "Password: $password")

        //Firebase authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    //if the creation is not successful
                    if (!it.isSuccessful)
                        return@addOnCompleteListener

                    //else if creating user is successful
                    Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                    val user = User(
                            username,
                            email,
                            "",
                            ""
                    )

                    var usersReference = firestoreDb.collection("users")
                    usersReference.document(FirebaseAuth.getInstance().currentUser?.uid as String)
                            .set(user)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

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
