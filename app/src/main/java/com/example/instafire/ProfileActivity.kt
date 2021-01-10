package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.instafire.models.Post
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_explore.homeButton
import kotlinx.android.synthetic.main.activity_explore.profileButton
import kotlinx.android.synthetic.main.activity_explore.rvPosts
import kotlinx.android.synthetic.main.activity_explore.fabCreate
import kotlinx.android.synthetic.main.activity_profile.*
import java.math.BigInteger
import java.security.MessageDigest

private const val TAG = "ProfileActivity"

class ProfileActivity : AppCompatActivity() {
    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //create layout file that represents one post - done
        //create data source - done
        posts = mutableListOf()     // empty mutable list
        //create the adapter
        adapter = PostAdapter(this, posts)
        //bind adapter and layout manager to the RV
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)
        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid as String)
                .get()
                .addOnSuccessListener {userSnapshot ->
                    signedInUser = userSnapshot.toObject(User::class.java)
                    profileBio.text = signedInUser?.bio
                    profileUsername.text = signedInUser?.username
                    val signedInUsername = signedInUser?.username
                    Log.i(TAG, "signedInUsername: $signedInUsername")
                    if (signedInUsername != null)
                        Glide.with(this).load(getProfileImageUrl(signedInUsername)).into(profilePic)
                    Log.i(TAG, "signed in user: $signedInUser")
                }

                .addOnFailureListener {exception ->
                    Log.i(TAG, "failure fetching signed in user", exception)
                }

        var postsReference = firestoreDb
                .collection("posts") as Query
//                .limit(20)
        // .orderBy("creation_time", Query.Direction.DESCENDING)

        var usersReference = firestoreDb.
                collection("users") as Query

        val extraUsername = intent.getStringExtra(EXTRA_USERNAME)
        homeText.text = "Profile"
        if(extraUsername != null) {
            supportActionBar?.title = extraUsername
            postsReference = postsReference.whereEqualTo("user.username", extraUsername)
//            usersReference = usersReference.whereEqualTo("username", extraUsername)
//            profileUsername.text = usersReference.get("username")
            // only display user's post on profile

//            if (signedInUsername != extraUsername)
//                homeText.text = "$extraUsername's Profile"

        }


        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }
        }

        fabCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
            finish()
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileButton.setOnClickListener {
            Toast.makeText(this, "You are already on Profile page!", Toast.LENGTH_SHORT).show()
        }

        profileSettingBtn.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
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