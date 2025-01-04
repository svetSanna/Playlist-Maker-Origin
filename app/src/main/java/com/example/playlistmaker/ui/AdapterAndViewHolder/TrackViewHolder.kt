package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.icu.text.SimpleDateFormat
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    var tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    var tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    val ivImage: ImageView = itemView.findViewById(R.id.image)

    // var artworkUrl100: TextView = itemView.findViewById(R.id.artworkUrl100)
    fun bind(
        item: Track,
        onItemClickListener: OnItemClickListener?,
        onLongClickListener: OnLongClickListener?
    ) {
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.place_holder)
            .fitCenter()
            .transform(RoundedCorners(2))//(R.dimen.corner_radius_2))
            .into(ivImage)

        // для истории поиска генерируем слушателя нажатия на элемент
        itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }

        // для истории поиска генерируем слушателя нажатия на элемент
        itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(item)!!
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Track)
    }
    interface OnLongClickListener{
        fun onLongClick(item: Track): Boolean
    }
}
