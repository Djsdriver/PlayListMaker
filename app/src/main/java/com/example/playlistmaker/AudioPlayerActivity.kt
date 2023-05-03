package com.example.playlistmaker


import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {

    private val binding: ActivityAudioPlayerBinding by lazy {
        ActivityAudioPlayerBinding.inflate(layoutInflater)
    }
    private lateinit var track: Track


    companion object {

        private val handler = Handler(Looper.getMainLooper())
        private val mediaPlayer = MediaPlayer()
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

    }

    private var playerState = STATE_DEFAULT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarPlayer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        binding.trackNamePlayer.isSelected = true

        // val track=intent.getSerializableExtra("item") as Track

        /*val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Const.PUT_EXTRA_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getSerializableExtra(Const.PUT_EXTRA_TRACK) as Track
        }*/
        val track = Gson().fromJson(intent.getStringExtra("item"), Track::class.java)
        preparePlayer()

            Glide.with(this)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.track_album_corner_radius
                        )
                    )
                )
                .into(binding.coverTrack)

            with(binding) {
                timeTrack.text = track.trackTimeMillis.let {
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis.toLong())
                }
                trackNamePlayer.text = track.trackName
                artistNamePlayer.text = track.artistName
                duration.text = track.trackTimeMillis.let {
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis.toLong())
                }
                album.text = track.collectionName.let { track.collectionName }
                year.text = track.releaseDate.let { track.releaseDate.substringBefore("-") }
                genre.text = track.primaryGenreName.let { track.primaryGenreName }
                country.text = track.country.let { track.country }
            }




        binding.playFab.setOnClickListener {
            playbackControl()
            when (playerState) {
                STATE_PLAYING -> {
                    binding.timeTrack.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed({ this }, 300L)
                }
                STATE_PAUSED -> {
                    handler.removeCallbacks { this }
                }
                STATE_PREPARED -> {
                    handler.removeCallbacks({ this })
                    binding.timeTrack.text = "00:00"
                }
            }

        }

    }

    /*private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playFab.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playFab.setImageResource(R.drawable.play_button)

            playerState = STATE_PREPARED
        }

    }*/
    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                binding.playFab.setImageResource(R.drawable.play_button)
                playerState = STATE_PREPARED
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playFab.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playFab.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks { this }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
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
}




