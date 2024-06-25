package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import com.example.playlistmaker.R.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MediaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        // кнопка "Назад"
        val buttonSearchBack = findViewById<ImageView>(id.button_search_back_media)
        buttonSearchBack.setOnClickListener{
            onBackPressed()
        }

        var item : Track? = getIntent().getParcelableExtra("TRACK")



    }
}