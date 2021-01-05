package com.example.instafire

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.instafire.models.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_recipe_page.*

private const val TAG = "RecipePage"

class RecipePage : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_page)

        firestoreDb = FirebaseFirestore.getInstance()
        val postsReference = firestoreDb.collection("posts")

        val recipeFields = intent.extras
        val dishName = recipeFields?.get("Dish Name") as String
        val description = recipeFields?.get("Description") as String
        val difficulty = recipeFields?.get("Difficulty") as Int
        val minutesNeeded = recipeFields?.get("Minutes needed") as Int
        //val ingredients = recipeFields?.get("Ingredients") as String
        //val steps = recipeFields?.get("Steps") as String


        Log.i(TAG, "Description: " + description)
        Log.i(TAG, "Minutes needed: " + minutesNeeded.toString())

        tvDishName2.text = dishName
        tvDescription.text = description
        tvDifficulty.text = difficulty.toString()
        tvTime.text = minutesNeeded.toString()
        //tvIngredients.text = ingredients
        //tvSteps.text = steps


        postsReference.addSnapshotListener{ snapshot, exception->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            Log.i ("PostAdapter", "here first")

            val postList = snapshot.toObjects(Post::class.java)
            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }
        }
    }
}