package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_search = findViewById<Button>(R.id.button_search)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку \"Поиск\"!", Toast.LENGTH_SHORT).show()
            }
        }
        button_search.setOnClickListener(buttonClickListener)

        val button_media = findViewById<Button>(R.id.button_media)

        button_media.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Медиатека\"!", Toast.LENGTH_SHORT).show()
        }

        val button_settings = findViewById<Button>(R.id.button_settings)

        button_settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Настройки\"!", Toast.LENGTH_SHORT).show()
        }
    }
}