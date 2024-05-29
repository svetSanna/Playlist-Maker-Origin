package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
   // private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"
    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value.toMutableList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)

       /* val image = parent.findViewById<ImageView>(R.id.image)

        Glide.with(itemView).load(imageUrl).into(image)

        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.place_holder)
            .into(image)
*/
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

