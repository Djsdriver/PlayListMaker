package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    val switch=App()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        val sharedPrefs = getSharedPreferences(Const.PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val isTurn=sharedPrefs.getBoolean(Const.SWITCH_KEY,false)
        binding.switchSettingsDarkTheme.isChecked=isTurn
        switch.switchTheme(isTurn)


        binding.switchSettingsDarkTheme.setOnCheckedChangeListener { switcher, checked ->
            sharedPrefs.edit().putBoolean(Const.SWITCH_KEY,checked).apply()
            (applicationContext as App).switchTheme(checked)
        }


        binding.btnImShare.setOnClickListener {

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.send_to));
            startActivity(Intent.createChooser(shareIntent,getString(R.string.send_to)))
        }

        binding.btnImSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO);
            supportIntent.data=Uri.parse("mailto:") // only email apps should handle this
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.TEXT_EXTRA_SUBJEC))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.BODY_EXTRA_TEXT));
            startActivity(Intent.createChooser(supportIntent,""))
        }
        binding.btnImNext.setOnClickListener {
            val nextIntent= Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.adress)))

            startActivity(Intent.createChooser(nextIntent,""))
        }

    }

    private fun darkOrLightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_color)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_color)
            }
        }
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }*/
}
