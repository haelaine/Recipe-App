package com.example.instafire

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instafire.models.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_recipe_page.*
import java.io.InputStream
import java.lang.System.load
import java.net.URL


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
        tvDifficulty.text = difficulty.toString()
        tvTime.text = minutesNeeded.toString()
        Glide.with(this).load(imageUrl).into(recipeImage)
        tvIngredients.text = listOfIngredients
        //tvSteps.text = steps

    }
}