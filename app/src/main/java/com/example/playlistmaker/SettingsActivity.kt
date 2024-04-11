package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonSettingsBack = findViewById<Button>(R.id.button_settings_back)

        buttonSettingsBack.setOnClickListener {
            onBackPressed()
        }
    }
}