package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.ItemPlaylist
import com.example.playlistmaker.PlaylistAdapter

import com.example.playlistmaker.R
import com.example.playlistmaker.common.TrackAdapter

import com.example.playlistmaker.databinding.FragmentPlayListBinding
import com.example.playlistmaker.main.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PlayListFragment : Fragment() {
    private val binding: FragmentPlayListBinding by lazy {
        FragmentPlayListBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<PlayListsViewModel>()
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

        binding.recyclerPlaylist.layoutManager = GridLayoutManager(requireContext(), /*Количество столбцов*/ 2) //ориентация по умолчанию — вертикальная
        binding.recyclerPlaylist.adapter = PlaylistAdapter()




        binding.newPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_newPlaylistFragment)
        }
    }

    companion object {

        fun newInstance() = PlayListFragment().apply {}
    }
}