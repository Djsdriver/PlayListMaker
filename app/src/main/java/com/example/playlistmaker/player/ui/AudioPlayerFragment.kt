package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.media.addPlayList.domain.models.PlaylistModel
import com.example.playlistmaker.media.addPlayList.presention.ui.PlaylistAdapter
import com.example.playlistmaker.media.ui.PlaylistState
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toPlaylistModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment(), PlaylistAdapter.ClickListener {


    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private val playlistAdapter = PlaylistAdapter(this, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetContainer.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.bottomSheetContainer.rvPlaylists.adapter = playlistAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.VISIBLE
                        viewModel.getAllPlaylist()

                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.state.collect { state ->
                                when (state) {
                                    is PlaylistState.Empty -> {
                                        //binding.linearLayout.visibility = View.VISIBLE
                                    }
                                    is PlaylistState.PlaylistLoaded -> {
                                        val tracks = state.tracks.map { it.toPlaylistModel() }
                                        playlistAdapter.setTrackList(tracks)
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1f
            }
        })

        binding.bottomSheetContainer.newPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_addFragment)
        }

        binding.playlistFab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.trackNamePlayer.isSelected = true


        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(Const.PUT_EXTRA_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(Const.PUT_EXTRA_TRACK) as Track?
        }

        binding.favoriteFab.setOnClickListener {
            if (track != null) {
                viewModel.onFavoriteClicked(track)
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.favoriteFab.setImageResource(
                if (isFavorite) R.drawable.filled_heart else R.drawable.favorite_button
            )
        }

        binding.playFab.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.playerState.observe(viewLifecycleOwner) { state ->
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
                    binding.timeTrack.text = Const.DEFAULT_TIME
                }
                else -> {
                }
            }
        }

        viewModel.playbackTime.observe(viewLifecycleOwner) { playbackTime ->
            if (!playbackTime.isNullOrEmpty()) {
                binding.timeTrack.text = playbackTime
            }
        }

        if (track != null) {
            viewModel.preparePlayer(track)

            Glide.with(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFabIcon(isPlaying: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.playFab.foreground =
                ResourcesCompat.getDrawable(
                    resources,
                    if (isPlaying) R.drawable.pause_button else R.drawable.play_button,
                    null
                )
        }
    }

    override fun onClick(playlistModel: PlaylistModel) {
        // Handle playlist item click here
    }
}