package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R.id.button_settings_allow_forward
import com.example.playlistmaker.R.id.button_settings_share
import com.example.playlistmaker.R.id.button_settings_support

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonSettingsBack = findViewById<ImageView>(R.id.button_settings_back)
        buttonSettingsBack.setOnClickListener {
            onBackPressed()
        }

        val buttonSettingsShare = findViewById<ImageView>(button_settings_share)
        buttonSettingsShare.setOnClickListener{
            onButtonShare()
        }

        val buttonSettingsSupport = findViewById<ImageView>(button_settings_support)
        buttonSettingsSupport.setOnClickListener {
            onButtonSupport()
        }

        val buttonSettingsAllowForward = findViewById<ImageView>(button_settings_allow_forward)
        buttonSettingsAllowForward.setOnClickListener {
            onButtonAllowForward()
        }
    }
    fun onButtonShare(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")

        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.message_share))
        try{
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@SettingsActivity, getResources().getString(R.string.message_error_intent),
                Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("IntentReset")
    fun onButtonSupport(){
        val emailIntent = Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("text/plain")
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getResources().getString((R.string.my_email))))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString((R.string.message_theme)))
        emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString((R.string.message_text)))
        try{
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@SettingsActivity, getResources().getString((R.string.message_error_intent)),
                Toast.LENGTH_SHORT).show()
        }
    }
    fun onButtonAllowForward(){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setType("text/plain")
        intent.data = Uri.parse(getResources().getString((R.string.offer_address)))
        try{
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@SettingsActivity, getResources().getString((R.string.message_error_intent)), Toast.LENGTH_SHORT).show()
        }
    }
}