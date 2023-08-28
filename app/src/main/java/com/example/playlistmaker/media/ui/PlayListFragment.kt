package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentPlayListBinding
import com.example.playlistmaker.media.addPlayList.presention.ui.PlaylistAdapter
import com.example.playlistmaker.utility.toPlaylistModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment() {
    private val binding: FragmentPlayListBinding by lazy {
        FragmentPlayListBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<PlaylistViewModel>()
    private val playlistAdapter = PlaylistAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerPlaylist.layoutManager = GridLayoutManager(requireContext(),2)

        binding.recyclerPlaylist.adapter = playlistAdapter

        viewModel.getAllPlaylist()


        lifecycleScope.launch {
            viewModel.state.collect{state->
                when (state){
                    is PlaylistState.Empty -> {
                        binding.linearLayout.visibility=View.VISIBLE
                    }
                    is PlaylistState.PlaylistLoaded -> {
                        val tracks = state.tracks.map { it.toPlaylistModel() }
                        playlistAdapter.setTrackList(tracks)
                        binding.linearLayout.visibility=View.GONE
                        binding.recyclerPlaylist.visibility=View.VISIBLE
                    }

                    else -> {
                        //
                    }
                }
            }
        }

        binding.newPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_addFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllPlaylist()
    }

    companion object {

        fun newInstance() = PlayListFragment().apply {}
    }
}