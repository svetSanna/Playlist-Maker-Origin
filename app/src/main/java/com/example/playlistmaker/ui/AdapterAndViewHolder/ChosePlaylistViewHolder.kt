package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.content.Context
import android.util.TypedValue
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
            //.fitCenter()
            //.transform(RoundedCorners(dpToPx(8F, itemView.context)))
            //.centerCrop()
            .into(image)

        name.text = item.name
        count.text = item.count.toString() + " трек" + App.getEnding(item.count)

        // генерируем слушателя нажатия на элемент
        itemView.setOnClickListener {
            onChosePlaylistClickListener?.onChosePlaylistClick(item)
        }
    }
    interface OnChosePlaylistClickListener {
        fun onChosePlaylistClick(item: Playlist)
    }

    // функция отпределяет окончание к слову "трек" в зависимости от числительного n
    /*private fun getEnding(n: Int) : String{
        var s = n.toString()

        if (s.endsWith("5") || s.endsWith("6") || s.endsWith("7") || s.endsWith("8") || s.endsWith("9")
            || s.endsWith("0") || s.endsWith("11") || s.endsWith("12") || s.endsWith("13") || s.endsWith("14")
            || s.endsWith("15") || s.endsWith("16") || s.endsWith("17") || s.endsWith("18") || s.endsWith("19")) return "ов"
        if (s.endsWith("2") || s.endsWith("3") || s.endsWith("4")) return "а"
        return ""
    } */
}