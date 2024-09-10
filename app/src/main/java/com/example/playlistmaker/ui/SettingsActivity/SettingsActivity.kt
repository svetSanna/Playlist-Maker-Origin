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
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)      // setContentView(R.layout.activity_settings)

        val buttonSettingsBack = binding.buttonSettingsBack // findViewById<ImageView>(R.id.button_settings_back)
        // вернуться назад
        buttonSettingsBack.setOnClickListener {
            onBackPressed()
        }

        val buttonSettingsShare = binding.buttonSettingsShare //findViewById<ImageView>(button_settings_share)
        // поделиться приложением
        buttonSettingsShare.setOnClickListener {
            onButtonShare()
        }

        val buttonSettingsSupport = binding.buttonSettingsSupport //findViewById<ImageView>(button_settings_support)
        // написать в поддержку
        buttonSettingsSupport.setOnClickListener {
            onButtonSupport()
        }

        val buttonSettingsAllowForward = binding.buttonSettingsAllowForward// findViewById<ImageView>(button_settings_allow_forward)
        // пользовательское соглашение
        buttonSettingsAllowForward.setOnClickListener {
            onButtonAllowForward()
        }

        //val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)

        //Creator.initApplication(this) //p2

        //val sharedPrefs = Creator.provideSharedPreferences() //p3

        val sharedPreferencesInteractor = Creator.provideSharedPreferencesInteractor()

        val selectorSwitch = binding.selectorSwitch // findViewById<SwitchCompat>(R.id.selector_switch)
        selectorSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPreferencesInteractor.edit(checked.toString())
            /*sharedPrefs.edit()
                .putString(THEME_SWITCH_KEY, checked.toString())
                .apply()*/

        }
        selectorSwitch.isChecked = (applicationContext as App).darkTheme
    }

    override fun onResume() { // в задании этого пока не было, решила попробовать.
        super.onResume()
        // setContentView(R.layout.activity_settings)
        /* Выставляла переключатель по заданной теме, потом переделала с помощью sharedPrefernces
            val selectorSwitch = findViewById<SwitchCompat>(R.id.selector_switch)

            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { selectorSwitch.setChecked(true)}
            Configuration.UI_MODE_NIGHT_NO -> {selectorSwitch.setChecked(false)}
        } */
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
        val emailIntent = Intent(Intent.ACTION_SENDTO);
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