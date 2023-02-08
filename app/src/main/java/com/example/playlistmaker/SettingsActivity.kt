package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSettings)
            //почему работает и без функции  onOptionsItemSelected если нажали кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnImShare.setOnClickListener { Toast.makeText(this,"Share", Toast.LENGTH_LONG).show() }
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }*/
}