package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.Const
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.repository.SharingRepository

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    val switch by lazy {
        application as App
    }


    private lateinit var viewModelSetting: SettingViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModelSetting=ViewModelProvider(this, SettingViewModelFactory(this))[SettingViewModel::class.java]

        setSupportActionBar(binding.toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        viewModelSetting.isTurn.observe(this) {
            binding.switchSettingsDarkTheme.isChecked = it
           switch.switchTheme(it)
           (applicationContext as App).switchTheme(it)
        }


        binding.switchSettingsDarkTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModelSetting.changeAppTheme(checked)
        }


        binding.btnImShare.setOnClickListener {
            viewModelSetting.shareApp(getString(R.string.send_to))
        }

        binding.btnImSupport.setOnClickListener {
            viewModelSetting.sendToSupport(EmailData(
                sender = getString(R.string.my_mail),
                subject = getString(R.string.TEXT_EXTRA_SUBJEC),
                message =getString(R.string.BODY_EXTRA_TEXT) ))
        }
        binding.btnImNext.setOnClickListener {
            viewModelSetting.openTerms(getString(R.string.adress))
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
