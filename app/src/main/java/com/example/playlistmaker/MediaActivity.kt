package com.example.playlistmaker

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class MediaActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        // кнопка "Назад"
        val buttonBackMedia = findViewById<Toolbar>(R.id.toolbar)
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // получаем данные трека из Intent
        var item : Track? = getIntent().getParcelableExtra(TRACK)

        // раскладываем эти данные по соответствующим вьюшкам
        var ivTrackImage: ImageView = findViewById(R.id.track_image)
        var tvTrackName: TextView = findViewById(R.id.track_name_data)
        var tvArtistName: TextView = findViewById(R.id.artist_name_data)
        var tvTrackTime: TextView = findViewById(R.id.track_time_millis_data) // длительность
        var tvCollectionName: TextView = findViewById(R.id.collection_name_data)
        var tvReleaseDate: TextView = findViewById(R.id.release_date_data)
        var tvPrimaryGenreName: TextView = findViewById(R.id.primary_genre_name_data)
        var tvCountry: TextView = findViewById(R.id.country_data)

        if(item != null) {
            Glide.with(this)
                .load(item.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))//(R.dimen.corner_radius_8))
                .into(ivTrackImage)

            tvTrackName.text = item.trackName
            tvArtistName.text = item.artistName
            tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
            tvCollectionName.text = item.collectionName
            tvReleaseDate.text = item.releaseDate.substring(0,4)
            tvPrimaryGenreName.text = item.primaryGenreName
            tvCountry.text = item.country
        }
    }
}