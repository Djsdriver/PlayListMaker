package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding

class SearchScreen : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}