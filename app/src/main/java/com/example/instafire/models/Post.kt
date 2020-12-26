package com.example.instafire.models

import com.google.firebase.firestore.PropertyName

/**
data class Post(
        var description: String = "",
        var image_url: String = "",
        var creation_time: Long = 0,
        var user: User? = null
)
*/


data class Post(
        var description: String = "",
        var difficulty: Int = 0,
        var image_url: String = "",
        //var ingredients: Array<String> = {}, TODO: use String arr as ingredients list
        var time_needed: String = "",           // TODO: use int instead? easier for sorting
        var title: String = "",
        var user: User? = null
)