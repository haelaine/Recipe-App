package com.example.instafire

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile_setting.*
import java.math.BigInteger
import java.security.MessageDigest

private val TAG = "ProfileSettingActivity"


class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var fireauthDb: FirebaseAuth
    private var signedInUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        firestoreDb = FirebaseFirestore.getInstance()
        fireauthDb = FirebaseAuth.getInstance()
        val user = fireauthDb.currentUser

        firestoreDb.collection("users")
                .document(user?.uid as String)
                .get()
                .addOnSuccessListener {userSnapshot ->
                    signedInUser = userSnapshot?.toObject(User::class.java)
                    Glide.with(this).load(getProfileImageUrl(signedInUser!!.username)).into(editProfilePicture)
                    tvName.text = signedInUser?.username
                    tvDisplayBio.text = signedInUser?.bio
                    Log.i(TAG, "signed in user: $signedInUser")
                }

                .addOnFailureListener {exception ->
                    Log.i(TAG, "failure fetching signed in user", exception)
                }

        tvDisplayEmail.text = user.email

        btnUpdateProfile.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnGoBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnResetPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        btnDeleteAccount.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.delete()
                    ?.addOnSuccessListener {
                        Toast.makeText(this,"Account deleted", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "User account deleted.")
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
        }
    }

    private fun getProfileImageUrl(username: String): String {
        val digest = MessageDigest.getInstance("MD5");
        val hash = digest.digest(username.toByteArray());
        val bigInt = BigInteger(hash);
        val hex = bigInt.abs().toString(16);
        return "https://www.gravatar.com/avatar/$hex?d=identicon";
    }


}
