package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toTrack
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment(),TrackAdapter.ClickListener {
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TrackAdapter(this)

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
        binding.recyclerViewFavorite.adapter = adapter

        viewModel.getAllTracks()
        adapter.notifyDataSetChanged()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteTracksState.Empty -> {
                    adapter.notifyDataSetChanged()
                    binding.placeHolderFavorite.visibility = View.VISIBLE
                    binding.recyclerViewFavorite.visibility=View.GONE
                }

                is FavoriteTracksState.TracksLoaded -> {
                    adapter.notifyDataSetChanged()
                    binding.placeHolderFavorite.visibility = View.GONE
                    val tracks = state.tracks.map { it.toTrack() }
                    adapter.setTrackList(tracks)

                }
            }
        }
    }

    companion object {

        fun newInstance() = FavoriteTracksFragment().apply {}
    }

    override fun onResume() {
        super.onResume()
        Log.d("Resume", "ResumeFavor")
        viewModel.getAllTracks()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Resume", "PauseFavor")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Resume", "StartFavor")
        viewModel.getAllTracks()
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Resume", "StopFavor")

    }


    override fun onClick(track: Track) {
        startActivity(Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(Const.PUT_EXTRA_TRACK, track)
        })
    }
}