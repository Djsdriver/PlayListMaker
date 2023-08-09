package com.example.playlistmaker.player.ui


import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.utility.toTrackEntity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity() : AppCompatActivity() {

    private val binding: ActivityAudioPlayerBinding by lazy {
        ActivityAudioPlayerBinding.inflate(layoutInflater)
    }

    //private lateinit var viewModel: AudioPlayerViewModel
    private val viewModel by viewModel<AudioPlayerViewModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPlayer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        binding.trackNamePlayer.isSelected = true
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Const.PUT_EXTRA_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getSerializableExtra(Const.PUT_EXTRA_TRACK) as Track
        }

        binding.playFab.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.favoriteFab.setOnClickListener {
            if (track != null) {
                viewModel.onFavoriteClicked(track)
            }
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            binding.favoriteFab.setImageResource(
                if (isFavorite) R.drawable.filled_heart else R.drawable.favorite_button
            )
        }

        viewModel.playerState.observe(this) { state ->
            Log.e("State", state.toString())
            when (state) {
                is PlayerState.Idle -> {
                    setFabIcon(false)
                }
                is PlayerState.Prepared -> {
                    binding.timeTrack.text = Const.DEFAULT_TIME
                    setFabIcon(false)
                    binding.trackNamePlayer.text = state.track.trackName
                    binding.artistNamePlayer.text = state.track.artistName
                    binding.duration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(state.track.trackTimeMillis.toLong())
                    binding.album.text = state.track.collectionName
                    binding.year.text = state.track.releaseDate.substringBefore("-")
                    binding.genre.text = state.track.primaryGenreName
                    binding.country.text = state.track.country

                }
                is PlayerState.Playing -> {
                    setFabIcon(true)
                }
                is PlayerState.Paused -> {
                    setFabIcon(false)
                }
                is PlayerState.Progress -> {

                }
                is PlayerState.Completed -> {
                    setFabIcon(false)
                    binding.timeTrack.text = Const.DEFAULT_TIME // Set the text here
                }
                else -> {
                }
            }
        }

        viewModel.playbackTime.observe(this) { playbackTime ->
            if (!playbackTime.isNullOrEmpty()) {
                binding.timeTrack.text = playbackTime
            }
        }

        if (track != null) {
            viewModel.preparePlayer(track)

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
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //viewModel.pausePlayer()
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
    private fun setFabIcon(isPlaying: Boolean) {
        binding.playFab.foreground =
            ResourcesCompat.getDrawable(
                resources,
                if (isPlaying) R.drawable.pause_button else R.drawable.play_button,
                null
            )
    }
}
