package com.example.instafire.models

data class User(var username: String = "", var email: String = "", var bio: String = "",
                var imageUrl: String = "", var follower: List<User> = emptyList(),
                var following: List<User> = emptyList())