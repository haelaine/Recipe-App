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
        @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String = "",
        @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTimeMs: Long = 0,
        var user: User? = null)

//        var description: String = "",
//        var difficulty: Int = 0,                // TODO: 0-5?
//        var image_url: String = "",
//        var ingredients: Array<String> = emptyArray(),
//        var time_needed: String = "",           // TODO: use int (minutes) instead? easier for sorting
//        var title: String = "",
//        var user: User? = null                  // mapping
//) {
//        override fun equals(other: Any?): Boolean {
//                if (this === other) return true
//                if (javaClass != other?.javaClass) return false
//
//                other as Post
//
//                if (description != other.description) return false
//                if (difficulty != other.difficulty) return false
//                if (image_url != other.image_url) return false
//                if (!ingredients.contentEquals(other.ingredients)) return false
//                if (time_needed != other.time_needed) return false
//                if (title != other.title) return false
//                if (user != other.user) return false
//
//                return true
//        }
//
//        override fun hashCode(): Int {
//                var result = description.hashCode()
//                result = 31 * result + difficulty
//                result = 31 * result + image_url.hashCode()
//                result = 31 * result + ingredients.contentHashCode()
//                result = 31 * result + time_needed.hashCode()
//                result = 31 * result + title.hashCode()
//                result = 31 * result + (user?.hashCode() ?: 0)
//                return result
//        }
// }