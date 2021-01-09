package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instafire.models.Post
import com.google.firebase.firestore.Query
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post.*

private const val TAG = "PostActivity"
const val EXTRA_USERNAME = "EXTRA_USERNAME"     // will be overwritten anyway
const val EXTRA_SEARCH = "EXTRA_SEARCH"

open class PostActivity : AppCompatActivity() {

    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
//    private lateinit var searchResults: MutableList<Post>
    private lateinit var adapter: PostAdapter
//    private lateinit var searchAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //create layout file that represents one post - done
        //create data source - done
        posts = mutableListOf()     // empty mutable list
//        searchResults = mutableListOf()

        //create the adapter
        adapter = PostAdapter(this, posts)
//        searchAdapter = PostAdapter(this, searchResults)

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

        // TODO: consider limit to 20 posts
        var postsReference = firestoreDb
                .collection("posts")
                as? Query
//                .limit(20)
                // .orderBy("creation_time", Query.Direction.DESCENDING)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if(username != null) {
            supportActionBar?.title = username
            postsReference = postsReference?.whereEqualTo("user.username", username)
            // only display user's post on profile
        }

        val searchInput = intent.getStringExtra(EXTRA_SEARCH)
        if (searchInput != null) {
            postsReference = postsReference?.whereEqualTo("title", searchInput)
        }

        postsReference?.addSnapshotListener { snapshot, exception ->
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

//        fabCreate.setOnClickListener {
//            val intent = Intent(this, ExploreActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        homeButton.setOnClickListener {
            Toast.makeText(this, "You are already on Post page!", Toast.LENGTH_SHORT).show()
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
            finish()
        }

        searchRecipeBtn.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(EXTRA_SEARCH, searchInputBox.text.toString())
            startActivity(intent)
            finish()
        }

        // TODO: consider using searchView?
//        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                Log.i(TAG, "text submitted")
//                searchBar.clearFocus()
//                val searchText = query?.toLowerCase(Locale.getDefault())
//                posts.forEach{
//                    if(!it.title.toLowerCase(Locale.getDefault()).contains(searchText!!))
//                        posts.remove(it)
//                }
//                adapter.notifyDataSetChanged()
//                return false
//            }
//
//            override fun onQueryTextChange(query: String?): Boolean {
//                Log.i(TAG, "text changed")
////                val searchText = query?.toLowerCase(Locale.getDefault())
////                posts.forEach{
////                    if(!it.title.toLowerCase(Locale.getDefault()).contains(searchText!!))
////                        posts.remove(it)
////                }
////                adapter.notifyDataSetChanged()
//                return false
//            }
//        })

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_posts, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_profile) {
//            val intent = Intent(this, ProfileActivity::class.java)
//            //intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
//            startActivity(intent)
//        }
//        return super.onOptionsItemSelected(item)
//    }
}