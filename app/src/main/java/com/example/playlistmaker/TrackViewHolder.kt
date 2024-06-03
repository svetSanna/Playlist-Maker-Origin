package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    var tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    var tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    val ivImage: ImageView = itemView.findViewById(R.id.image)

   // var artworkUrl100: TextView = itemView.findViewById(R.id.artworkUrl100)
    fun bind(item: Track){
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

            /*songName.text = item.trackName
       singer.text = item.artistName
       songTime.text = item.trackTime
       songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis) */

        // val image = parent.findViewById<ImageView>(R.id.image)

       Glide.with(itemView)
           .load(item.artworkUrl100)
           .fitCenter() //
           .transform(RoundedCorners(2)) //
           .into(ivImage)

       Glide.with(itemView)
           .load(item.artworkUrl100)
           .placeholder(R.drawable.place_holder)
           .fitCenter()
           .transform(RoundedCorners(2))
           .into(ivImage)

       // artworkUrl100.text = item.artworkUrl100
    }

}
