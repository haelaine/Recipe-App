package com.example.instafire
// TODO: consider resetting password
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goPostsActivity()
        }

        forgot_password.setOnClickListener {
            Log.d(TAG, "Try to show reset password activity")
            //launch the reset password activity somehow
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false

            etEmail.setOnClickListener {
                btnLogin.isEnabled = true
            }
            etPassword.setOnClickListener {
                btnLogin.isEnabled = true
            }

            val email = etEmail.text.toString()
            val password = etPassword.text.toString();
            if (email.isBlank()) {
                etEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                etPassword.error = "Password cannot be empty"
                return@setOnClickListener
            }

            //Firebase authentication
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    goPostsActivity()
                    finish()    // remove back stack
                }
                else {
                    Log.e(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btSignUp.setOnClickListener {
            Log.d(TAG, "Try to show sign up activity")

            //launch the login activity somehow
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }


    private fun goPostsActivity() {
        Log.i(TAG, "goPostActivity")
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
        finish()
    }
}