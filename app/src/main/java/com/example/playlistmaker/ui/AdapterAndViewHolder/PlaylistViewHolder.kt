package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageView = itemView.findViewById(R.id.image_playlist_layout)
    private val name: TextView = itemView.findViewById(R.id.name_playlist_layout)
    private val count: TextView = itemView.findViewById(R.id.count_playlist_layout)

    fun bind(
        item: Playlist,
        onPlaylistClickListener: OnPlaylistClickListener?
    ) {
        Glide.with(itemView)
            .load(item.path)
            .placeholder(R.drawable.place_holder)
            //.centerCrop()
            //.transform(RoundedCorners(dpToPx(8F, itemView.context)))
            .into(image)

        name.text = item.name
        count.text = item.count.toString() + App.getEndingTrack(item.count)

        // генерируем слушателя нажатия на элемент
        itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(item)
        }
    }
    interface OnPlaylistClickListener {
        fun onPlaylistClick(item: Playlist)
    }
}
