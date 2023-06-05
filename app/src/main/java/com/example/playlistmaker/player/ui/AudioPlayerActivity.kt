package com.example.playlistmaker.player.ui


import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Const
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {

    private val binding: ActivityAudioPlayerBinding by lazy {
        ActivityAudioPlayerBinding.inflate(layoutInflater)
    }
    private val mediaPlayer = MediaPlayer()
    lateinit var handler: Handler

    private val audioPlayerRepositoryImpl = AudioPlayerRepositoryImpl(mediaPlayer)
    private val startPlayerUseCase = StartPlayerUseCase(audioPlayerRepositoryImpl)
    private val pausePlayerUseCase = PausePlayerUseCase(audioPlayerRepositoryImpl)
    private val getCurrentStateUseCase = GetCurrentStateUseCase(audioPlayerRepositoryImpl)
    private val plaBackCurrentStateUseCase= PlayBackControlUseCase(audioPlayerRepositoryImpl)


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
            val prepareUseCase = PrepareUseCase(audioPlayerRepositoryImpl, track)
            prepareUseCase.prepare()
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
            plaBackCurrentStateUseCase.playBackControl()
            threadTime()
            setFabIcon()
        }

        audioPlayerRepositoryImpl.setOnCompletionListener {
            handler.removeCallbacks { threadTime() }
        }

    }

    private fun threadTime() {
        handler.postDelayed(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                if (audioPlayerRepositoryImpl.getCurrentState() == PlayerState.STATE_PLAYING) {
                    try {
                        binding.timeTrack.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(audioPlayerRepositoryImpl.getCurrentTime())
                        handler.postDelayed(this, Const.DELAY_TIME)
                    } catch (e: java.lang.Exception) {
                        binding.timeTrack.text = Const.DEFAULT_TIME
                    }
                } else if (audioPlayerRepositoryImpl.getCurrentState() == PlayerState.STATE_PREPARED) {
                    binding.timeTrack.text = Const.DEFAULT_TIME
                    setFabIcon()
                }

            }
        }, 0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPause() {
        super.onPause()
        pausePlayerUseCase.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks { threadTime() }
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
    private fun setFabIcon() {
        when (getCurrentStateUseCase.getCurrentState()) {
            PlayerState.STATE_PLAYING -> binding.playFab.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.pause_button, null)
            else -> binding.playFab.foreground =
                ResourcesCompat.getDrawable(resources, R.drawable.play_button, null)
        }
    }

}



