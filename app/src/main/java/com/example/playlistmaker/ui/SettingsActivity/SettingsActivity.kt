package com.example.playlistmaker.ui.SettingsActivity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val buttonSettingsBack = binding.buttonSettingsBack
        // вернуться назад
        buttonSettingsBack.setOnClickListener {
            onBackPressed()
        }

        val buttonSettingsShare = binding.buttonSettingsShare
        // поделиться приложением
        buttonSettingsShare.setOnClickListener {
            onButtonShare()
        }

        val buttonSettingsSupport = binding.buttonSettingsSupport
        // написать в поддержку
        buttonSettingsSupport.setOnClickListener {
            onButtonSupport()
        }

        val buttonSettingsAllowForward = binding.buttonSettingsAllowForward
        // пользовательское соглашение
        buttonSettingsAllowForward.setOnClickListener {
            onButtonAllowForward()
        }

        val selectorSwitch = binding.selectorSwitch
        selectorSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.editTheme(checked.toString())
        }
        selectorSwitch.isChecked = (applicationContext as App).darkTheme
    }

    fun onButtonShare() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")

        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.message_share))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this@SettingsActivity, getResources().getString(R.string.message_error_intent),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("IntentReset")
    fun onButtonSupport() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.setType("text/plain")
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(getResources().getString((R.string.my_email)))
        )
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getResources().getString((R.string.message_theme))
        )
        emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString((R.string.message_text)))
        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this@SettingsActivity, getResources().getString((R.string.message_error_intent)),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun onButtonAllowForward() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setType("text/plain")
        intent.data = Uri.parse(getResources().getString((R.string.offer_address)))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this@SettingsActivity,
                getResources().getString((R.string.message_error_intent)),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}