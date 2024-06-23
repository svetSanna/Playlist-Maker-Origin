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
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)

        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
        // устанавливаем слушателя нажатия на элемент списка
        holder.itemView.setOnClickListener{
            var item = items[position]
            var itemSearchHistory = trackListSearchHistory.firstOrNull { it.trackId == item.trackId }
            if (itemSearchHistory!=null)
                trackListSearchHistory.remove(itemSearchHistory)
            trackListSearchHistory.add(0,item)

            if(trackListSearchHistory.size>10)
                trackListSearchHistory.remove(trackListSearchHistory[10])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

