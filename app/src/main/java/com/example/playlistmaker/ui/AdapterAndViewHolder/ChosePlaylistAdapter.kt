package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist

class ChosePlaylistAdapter: RecyclerView.Adapter<ChosePlaylistViewHolder>() {
    var items: MutableList<Playlist> = mutableListOf()
        set(value) {
            field = value.toMutableList()
            notifyDataSetChanged()
        }

    // передаем сюда слушателя нажатия на элемент
    var onChosePlaylistClickListener: ChosePlaylistViewHolder.OnChosePlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosePlaylistViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chose_playlist_layout, parent, false)

        return ChosePlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChosePlaylistViewHolder, position: Int) {
        holder.bind(
            item = items[position],
            onChosePlaylistClickListener = onChosePlaylistClickListener // присваиваем ссылку на тот объект, который создали в SearchActivity
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }
}