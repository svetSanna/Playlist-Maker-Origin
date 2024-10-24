package com.example.playlistmaker.ui.MediatekaFragment
/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity(){//, SelectPage {
    private lateinit var binding: ActivityMediatekaBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediatekaBinding.inflate(layoutInflater)

        val adapter = MediatekaViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
        )
        binding.viewPager.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        setContentView(binding.root)

        // кнопка "Назад"
        val buttonBackMedia = binding.buttonSearchBack
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}*/