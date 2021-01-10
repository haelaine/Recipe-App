package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile_setting.*
import kotlinx.android.synthetic.main.item_post.view.*
import java.math.BigInteger
import java.security.MessageDigest

private val TAG = "EditProfileActivity"


class EditProfileActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var fireauthDb: FirebaseAuth
    private var signedInUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firestoreDb = FirebaseFirestore.getInstance()
        fireauthDb = FirebaseAuth.getInstance()
        val user = fireauthDb.currentUser

        firestoreDb.collection("users")
            .document(user?.uid as String)
            .get()
            .addOnSuccessListener {userSnapshot ->
                signedInUser = userSnapshot?.toObject(User::class.java)
//                editUsername.setText(signedInUser?.username)
                editBio.setText(signedInUser?.bio)
//                Glide.with(this).load(getProfileImageUrl(signedInUser!!.username)).into(editProfilePicture)
                Log.i(TAG, "signed in user: $signedInUser")
            }

            .addOnFailureListener {exception ->
                Log.i(TAG, "failure fetching signed in user", exception)
            }
        editEmail.setText(user.email)

        btnSaveChanges.setOnClickListener{
//            val username = editUsername.text.toString()
            val email = editEmail.text.toString()
            val bio = editBio.text.toString()
//            if (username.isEmpty()) {
//                editUsername.error = "Please enter username."
//                return@setOnClickListener
//            }

            if (email.isEmpty()) {
                editEmail.error = "Please enter email."
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().currentUser?.updateEmail(email)
                ?.addOnSuccessListener {
                    val docRef = FirebaseFirestore.getInstance().collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid as String)
                    val edited = User(
                            signedInUser!!.username,
                        email,
                        bio,
                        ""
                    )
                    docRef
                        .set(edited)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, ProfileSettingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                ?.addOnFailureListener{
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
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