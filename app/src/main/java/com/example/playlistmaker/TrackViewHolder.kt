package com.example.playlistmaker

import android.view.View
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    var trackTime: TextView = itemView.findViewById(R.id.trackTime)
    var artworkUrl100: TextView = itemView.findViewById(R.id.artworkUrl100)
    fun bind(item: Track){
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        trackTime.text = item.trackTime
        artworkUrl100.text = item.artworkUrl100
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