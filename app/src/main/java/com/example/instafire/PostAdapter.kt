package com.example.instafire

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.instafire.models.Post
import kotlinx.android.synthetic.main.item_post.*
import kotlinx.android.synthetic.main.item_post.view.*
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "PostAdapter"
class PostAdapter(val context: Context, val posts: List<Post>) :
        RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder((view))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(post: Post) {
            val toRecipeBtn: Button = itemView.findViewById(R.id.goToRecipeButton)
            val username = post.user?.username as String
            itemView.tvUsername.text = post.user?.username
            itemView.tvDishName.text = post.description
            // gradle dependency updated
            Glide.with(context).load(post.imageUrl).into(itemView.ivRecipePicture)
            Glide.with(context).load(getProfileImageUrl(username)).into(itemView.ivUserProfile)

            // relative time: 1 hour ago
            var formatter = SimpleDateFormat("MM/dd/yyyy")
            var dateString = formatter.format(Date(post.creationTimeMs))
            itemView.tvRelativeTime.text = dateString     // DateUtils.getRelativeTimeSpanString(post.creationTimeMs)

//            TODO: only works when clicking on the image, directs to create page not the recipe page
            itemView.setOnClickListener {v: View ->
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "item clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context, CreateActivity::class.java)
                Log.i(TAG, "go to recipe button clicked")
                startActivity(itemView.context, intent, null)
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
}