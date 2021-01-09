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
        var title: String = "",
        var description: String = "",
        var difficulty: Int = 0,
        var ingredients: List<String> = emptyList(),
        var steps: String = "",
        var minutes_needed: Int = 0,           // var time_needed: String = "",
        @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String = "",
        @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTimeMs: Long = 0,
        var user: User? = null
)
