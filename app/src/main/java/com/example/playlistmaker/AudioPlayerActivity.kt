package com.example.playlistmaker


import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {

    private val binding: ActivityAudioPlayerBinding by lazy {
        ActivityAudioPlayerBinding.inflate(layoutInflater)
    }
    private val mediaPlayer = MediaPlayer()
    lateinit var handler: Handler


    companion object {

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

    }

    private var playerState = STATE_DEFAULT


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())


        setSupportActionBar(binding.toolbarPlayer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()


        binding.trackNamePlayer.isSelected = true
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Const.PUT_EXTRA_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getSerializableExtra(Const.PUT_EXTRA_TRACK) as Track
        }


        if (track != null) {
            preparePlayer(track)


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
        }

        binding.playFab.setOnClickListener {
            playbackControl()
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                handler.removeCallbacks { startTimer() }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun preparePlayer(track: Track) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                handler.removeCallbacks { startTimer() }
                playerState = STATE_PREPARED
                binding.timeTrack.text = Const.DEFAULT_TIME
                setFabIcon()
                Log.d("MyLog", "${setFabIcon()}")
            }
        }

    }

    private fun startTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.timeTrack.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, Const.DELAY_TIME)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        handler.post(startTimer())
        setFabIcon()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks { startTimer() }
        setFabIcon()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks { startTimer() }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setFabIcon() = when (playerState) {
        STATE_PLAYING -> binding.playFab.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
        STATE_PAUSED -> binding.playFab.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
        else -> binding.playFab.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
    }
}




