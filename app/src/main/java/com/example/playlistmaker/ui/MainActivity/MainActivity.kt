package com.example.playlistmaker.ui.MainActivity
/*
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.MediatekaActivity.MediatekaActivity
import com.example.playlistmaker.ui.SearchFragment.SearchActivity
import com.example.playlistmaker.ui.SettingsActivity.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val buttonSearch = binding.buttonSearch

        val displayIntent = Intent(this, SearchActivity::class.java)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(displayIntent)
            }
        }
        buttonSearch.setOnClickListener(buttonClickListener)

        val buttonMedia = binding.buttonMedia

        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(displayIntent)
        }

        val buttonSettings = binding.buttonSettings

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}*/