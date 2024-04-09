package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_search = findViewById<Button>(R.id.button_search)

        val displayIntent = Intent(this, SearchActivity::class.java)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(displayIntent)
                //Toast.makeText(this@MainActivity, "Нажали на кнопку \"Поиск\"!", Toast.LENGTH_SHORT).show()
            }
        }
        button_search.setOnClickListener(buttonClickListener)

        val button_media = findViewById<Button>(R.id.button_media)

        button_media.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
            // Toast.makeText(this@MainActivity, "Нажали на кнопку \"Медиатека\"!", Toast.LENGTH_SHORT).show()
        }

        val button_settings = findViewById<Button>(R.id.button_settings)

        button_settings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
            //Toast.makeText(this@MainActivity, "Нажали на кнопку \"Настройки\"!", Toast.LENGTH_SHORT).show()
        }
    }
}