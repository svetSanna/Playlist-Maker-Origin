package com.example.playlistmaker

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MediaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val buttonBackMedia = findViewById<Toolbar>(R.id.toolbar)
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // кнопка "Назад"
      /*  val buttonSearchBack = findViewById<ImageView>(id.button_search_back_media)
        buttonSearchBack.setOnClickListener{
            onBackPressed()
        }
*/
       var item : Track? = getIntent().getParcelableExtra(TRACK)



    }
}