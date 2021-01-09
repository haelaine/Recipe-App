package com.example.instafire

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_recipe_page.*
import kotlinx.android.synthetic.main.activity_recipe_page.homeButton
import kotlinx.android.synthetic.main.activity_recipe_page.profileButton
import kotlinx.android.synthetic.main.activity_recipe_page.fabCreate

private const val TAG = "RecipePage"

class RecipePage : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_page)

        //firestoreDb = FirebaseFirestore.getInstance()
        //val postsReference = firestoreDb.collection("posts")

        val recipeFields = intent.extras
        val dishName = recipeFields?.get("Dish Name") as String
        val description = recipeFields?.get("Description") as String
        val difficulty = recipeFields?.get("Difficulty") as Int
        val minutesNeeded = recipeFields?.get("Minutes needed") as Int
        val imageUrl = recipeFields?.get("imageUrl") as String
        val numIngredients = recipeFields?.get("Number of ingredients") as Int
        val profileImageUrl = recipeFields?.get("profileImageUrl") as String
        val username = recipeFields?.get("Username") as String

        var counter = 0 as Int
        var listOfIngredients = "" as String

        while (counter < numIngredients) {
            var ingredients = recipeFields?.get("ingredients" + counter.toString()) as String
            var numeratedIngredient = (counter + 1).toString() + ". "  + ingredients + "\n"
            listOfIngredients += numeratedIngredient
            Log.i(TAG, "ingredients" + counter.toString())
            counter++
        }

        Log.i(TAG, "Description: " + description)
        Log.i(TAG, "Minutes needed: " + minutesNeeded.toString() + "minutes")
        Log.i(TAG, "imageURL: " + imageUrl)

        tvDishName2.text = dishName
        tvDescription.text = description
        tvDifficulty_Post.text = "Difficulty: " + difficulty.toString()
        tvTime.text = "Duration: " + minutesNeeded.toString()
        Glide.with(this).load(imageUrl).into(recipeImage)
        tvIngredients.text = listOfIngredients
        Glide.with(this).load(profileImageUrl).into(ivUserProfile2)
        tvUsername2.text = username
        //tvSteps.text = steps


        /*postsReference.addSnapshotListener{ snapshot, exception->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            Log.i ("PostAdapter", "here first")

            val postList = snapshot.toObjects(Post::class.java)
            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }
        }*/

        fabCreate.setOnClickListener {
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}