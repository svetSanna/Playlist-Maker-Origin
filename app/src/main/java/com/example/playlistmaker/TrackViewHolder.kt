package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    var tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    var tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    val ivImage: ImageView = itemView.findViewById(R.id.image)

   // var artworkUrl100: TextView = itemView.findViewById(R.id.artworkUrl100)
    fun bind(item: Track){
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text = item.trackTime

        // val image = parent.findViewById<ImageView>(R.id.image)

       Glide.with(itemView).load(item.artworkUrl100).into(ivImage)

       Glide.with(itemView)
           .load(item.artworkUrl100)
           .placeholder(R.drawable.place_holder)
           .fitCenter()
           .transform(RoundedCorners(2))
           .into(ivImage)

       // artworkUrl100.text = item.artworkUrl100
    }

}

/*
*   val tvText: TextView = itemView.findViewById(R.id.tvText)
    val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)

    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun bind(item: ChatMessage.MyChatMessage) {
        tvText.text = item.text
        tvDate.text = formatter.format(item.date)
        ivStatus.setImageResource(
            when (item.status) {
                MessageStatus.NEW -> R.drawable.ic_new
                MessageStatus.SENT -> R.drawable.ic_sent
                MessageStatus.READ -> R.drawable.ic_read
            }
        )*/