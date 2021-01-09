package com.example.instafire
// TODO: consider inherit postActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instafire.models.Post
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_explore.*

private const val TAG = "ExploreActivity"
class ExploreActivity : AppCompatActivity() {
    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

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
                Log.i(TAG, "signed in user: $signedInUser")
            }

            .addOnFailureListener {exception ->
                Log.i(TAG, "failure fetching signed in user", exception)
            }

        var postsReference = firestoreDb
            .collection("posts")
            .limit(20)
                // .orderBy("creation_time", Query.Direction.DESCENDING)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if(username != null) {
            supportActionBar?.title = username
            postsReference = postsReference.whereEqualTo("user.username", username)
            // only display user's post on profile
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

        fabCreate.setOnClickListener{
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        fabCreate.setOnClickListener {
            Toast.makeText(this, "You are already on Explore page!", Toast.LENGTH_SHORT).show()
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}