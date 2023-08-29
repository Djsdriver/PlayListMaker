package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.common.TrackAdapter
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toTrack
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment(), TrackAdapter.ClickListener {
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val favoriteAdapter = TrackAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorite.adapter = favoriteAdapter

        viewModel.getAllTracks()

        lifecycleScope.launch {
            viewModel.state.collect{state->
                when (state){
                    is FavoriteTracksState.Empty -> {
                        binding.placeHolderFavorite.visibility = View.VISIBLE
                        binding.recyclerViewFavorite.visibility=View.GONE
                    }
                    is FavoriteTracksState.TracksLoaded -> {
                        binding.placeHolderFavorite.visibility = View.GONE
                        binding.recyclerViewFavorite.visibility=View.VISIBLE
                        val tracks = state.tracks.map { it.toTrack() }
                        favoriteAdapter.setTrackList(tracks)
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d("Resume", "ResumeFavor")
        viewModel.getAllTracks()
    }



    override fun onClick(track: Track) {
        val bundle = Bundle().apply {
            putSerializable(Const.PUT_EXTRA_TRACK, track)
        }
        val audioPlayerFragment = AudioPlayerFragment().apply {
            arguments = bundle
        }
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, bundle)
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment().apply {}
    }
}