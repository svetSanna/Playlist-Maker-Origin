package com.example.playlistmaker.ui.MediatekaActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.presentation.repository.SelectPage
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity(), SelectPage {
    private lateinit var binding: ActivityMediatekaBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediatekaBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_mediateka)

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

        //adapter.createFragment(1)
        //navigateTo(1)
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
    override fun navigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }
}