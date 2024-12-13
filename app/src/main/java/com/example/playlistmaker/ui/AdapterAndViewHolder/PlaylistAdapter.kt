package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {
    var items: MutableList<Playlist> = mutableListOf()
        set(value) {
            field = value.toMutableList()
            notifyDataSetChanged()
        }

    // передаем сюда слушателя нажатия на элемент
    var onPlaylistClickListener: PlaylistViewHolder.OnPlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_layout, parent, false)

        return PlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(
            item = items[position],
            onPlaylistClickListener = onPlaylistClickListener // присваиваем ссылку на тот объект, который создали в SearchActivity
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }
}