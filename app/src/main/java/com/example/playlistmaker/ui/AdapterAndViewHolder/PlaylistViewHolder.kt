package com.example.playlistmaker.ui.AdapterAndViewHolder

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist
import kotlin.math.roundToInt

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
            //.fitCenter()
            //.transform(RoundedCorners(dpToPx(8F, itemView.context)))
            //.centerCrop()
            .into(image)

        /*
             if (item != null) {
            Glide.with(this)
                .load(item!!.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(ivTrackImage)
         */



        name.text = item.name
        count.text = item.count.toString() + " трек" + getEnding(item.count)

        // генерируем слушателя нажатия на элемент
        itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(item)
        }
    }
    interface OnPlaylistClickListener {
        fun onPlaylistClick(item: Playlist)
    }

    // функция отпределяет окончание к слову "трек" в зависимости от числительного n
    private fun getEnding(n: Int) : String{
        var s = n.toString()

        if (s.endsWith("5") || s.endsWith("6") || s.endsWith("7") || s.endsWith("8") || s.endsWith("9")
            || s.endsWith("0") || s.endsWith("11") || s.endsWith("12") || s.endsWith("13") || s.endsWith("14")
            || s.endsWith("15") || s.endsWith("16") || s.endsWith("17") || s.endsWith("18") || s.endsWith("19")) return "ов"
        if (s.endsWith("2") || s.endsWith("3") || s.endsWith("4")) return "а"
        return ""
    }
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }
}
