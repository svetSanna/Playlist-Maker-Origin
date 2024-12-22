package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist

class ChosePlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageView = itemView.findViewById(R.id.image_chose_playlist)
    private val name: TextView = itemView.findViewById(R.id.chose_playlist_name)
    private val count: TextView = itemView.findViewById(R.id.chose_playlist_count)

    fun bind(
        item: Playlist,
        onChosePlaylistClickListener: OnChosePlaylistClickListener?
    ) {
        Glide.with(itemView)
            .load(item.path)
            .placeholder(R.drawable.place_holder)
            //.centerCrop()
            //.transform(RoundedCorners(dpToPx(8F, itemView.context)))
            .into(image)

        name.text = item.name
        //count.text = item.count.toString() + " трек" + App.getEnding(item.count)
        count.text = item.count.toString() + App.getEndingTrack(item.count)

        // генерируем слушателя нажатия на элемент
        itemView.setOnClickListener {
            onChosePlaylistClickListener?.onChosePlaylistClick(item)
        }
    }
    interface OnChosePlaylistClickListener {
        fun onChosePlaylistClick(item: Playlist)
    }
}