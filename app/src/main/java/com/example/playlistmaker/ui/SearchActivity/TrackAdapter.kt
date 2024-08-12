package com.example.playlistmaker.ui.SearchActivity

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track


class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    // private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"
    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value.toMutableList()
            notifyDataSetChanged()
        }

    // передаем сюда слушателя нажатия на элемент
    var onItemClickListener: TrackViewHolder.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)

        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(
            item = items[position],
            onItemClickListener = onItemClickListener // присваиваем ссылку на тот объект, который создали в SearchActivity
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

