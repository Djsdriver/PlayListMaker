package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnMedia.setOnClickListener {
            startActivity(Intent(this,MediaScreen::class.java))
        }

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchScreen::class.java))

        }

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }


    }



}