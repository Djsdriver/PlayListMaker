package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.playlistmaker.databinding.ActivityMediaScreenBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding

class MediaScreen : AppCompatActivity() {
    lateinit var binding: ActivityMediaScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMediaScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMedia).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}