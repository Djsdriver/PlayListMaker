package com.example.playlistmaker.media.playlistcontent.presentation.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.common.TrackAdapter
import com.example.playlistmaker.databinding.FragmentPlaylistContentBinding
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toPlaylistEntity
import com.example.playlistmaker.utility.toTrackEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistContentFragment : Fragment(),TrackAdapter.ClickListener {

    private val binding: FragmentPlaylistContentBinding by lazy {
        FragmentPlaylistContentBinding.inflate(layoutInflater)
    }

    private var playlistModel:PlaylistModel?=null
    private val viewModel by viewModel<PlaylistContentFragmentViewModel>()

    private val adapter =  TrackAdapter(this)

    val bottomSheetBehaviorMenu
        get() = BottomSheetBehavior.from(binding.bottomSheetContainerMenu.additionMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    val bottomSheetBehaviorTracks
        get() = BottomSheetBehavior.from(binding.bottomSheetContainerTracks.bottomSheetTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(Const.PUT_EXTRA_PLAYLIST, PlaylistModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(Const.PUT_EXTRA_PLAYLIST) as PlaylistModel?
        }
        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_album")
        val imageFile = playlistModel?.imagePath?.let { File(filePath, it) }

        Glide.with(requireContext())
            .load(imageFile)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.coverPlaylistContent)

        binding.descriptionPlaylist.text= playlistModel?.description ?: ""

        binding.namePlaylist.text=playlistModel?.name?: ""

        viewModel.stateCountTracks.observe(viewLifecycleOwner){ count->
            binding.tracksCount.text=resources.getQuantityString(
                R.plurals.plural_tracks, count.toInt(), count.toInt()
            )
        }

        viewModel.stateDuration.observe(viewLifecycleOwner) { duration ->
            binding.tracksDuration.text = resources.getQuantityString(
                R.plurals.plural_minutes, duration.toInt(), duration.toInt()
            )
        }
        playlistModel?.let { viewModel.updatePlaylist(it) }

        val longClickListener = TrackAdapter.LongClickListener { track ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setMessage("Удалить трек?")
                .setPositiveButton("ОК") { _, _ ->
                   val position = adapter.tracks.indexOfFirst { it.trackId == track.trackId }
                    if (playlistModel?.tracks?.isNotEmpty() == true) {
                        viewModel.removeTrackFromPlaylist(track.toTrackEntity(), playlistModel?.id ?: 0)

                        adapter.removeTrack(position)
                        adapter.notifyItemChanged(position)
                        playlistModel!!.tracks.remove(track)
                        if (playlistModel?.tracks?.isEmpty() == true){
                            binding.bottomSheetContainerTracks.linearLayout.visibility=View.VISIBLE
                            adapter.notifyDataSetChanged()

                        }
                    } else {
                        //
                    }
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialogBuilder.show()
        }

        adapter.setClickListener(longClickListener)


        binding.bottomSheetContainerTracks.rvPlaylists.layoutManager=LinearLayoutManager(requireContext())
        binding.bottomSheetContainerTracks.rvPlaylists.adapter=adapter
        if (playlistModel?.tracks?.isEmpty() == true){
            binding.bottomSheetContainerTracks.linearLayout.visibility=View.VISIBLE
            adapter.notifyDataSetChanged()

        } else{
            val tracks = playlistModel?.tracks?.reversed() ?: emptyList()
            adapter.setTrackList(tracks)
            adapter.notifyDataSetChanged()

        }

        binding.toolbarPlaylistContent.setOnClickListener {
            findNavController().popBackStack(R.id.mediatekaFragment,false)
        }

        binding.menuPlaylistContent.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {

                        binding.bottomSheetContainerMenu.playlistItem.textTitle.text=playlistModel?.name?: ""

                        viewModel.stateCountTracks.observe(viewLifecycleOwner){ count->
                            binding.bottomSheetContainerMenu.playlistItem.textTracksQuantity.text=resources.getQuantityString(
                                R.plurals.plural_tracks, count.toInt(), count.toInt()
                            )
                        }
                        Glide.with(requireContext())
                            .load(imageFile)
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .into(binding.bottomSheetContainerMenu.playlistItem.ivPlaylistBigImage)

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

        binding.bottomSheetContainerMenu.textDeletePlaylist.setOnClickListener {
            showDeletePlaylistDialog()
        }

        bottomSheetBehaviorTracks.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {


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

        binding.bottomSheetContainerMenu.textSharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        binding.sharePlaylistContent.setOnClickListener {
            sharePlaylist()
        }

        binding.bottomSheetContainerMenu.textEditInfoPlaylist.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(Const.PUT_EXTRA_PLAYLIST, playlistModel)
            }
            findNavController().navigate(R.id.action_playlistContentFragment_to_editPlaylistFragment,bundle)
        }
    }

    private fun sharePlaylist() {
        if (adapter.tracks.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                resources.getText(R.string.there_is_nothing_to_share),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getTextForPlaylistSharing())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun getTextForPlaylistSharing():String {
        val str = mutableListOf<String>()
        playlistModel?.name?.let { str.add(it) }
        if (playlistModel?.description?.isNotBlank() == true) {
            str.add(playlistModel!!.description)
        }
        playlistModel?.tracks?.size?.let {
            this.resources.getQuantityString(
                R.plurals.plural_tracks,
                it,
                playlistModel!!.tracks.size
            )
        }?.let {
            str.add(
                it
            )
        }
        for (track in playlistModel?.tracks!!) {
            str.add("${playlistModel!!.tracks.indexOf(track) + 1}. ${track.trackName} ${track.artistName} ${track.trackTimeMillis}")
        }

        return str.joinToString("\n")
    }



    companion object {


    }
    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(this@PlaylistContentFragment.resources.getText(R.string.quitting_question_playlist))
            .setNegativeButton(this@PlaylistContentFragment.resources.getText(R.string.no)) { dialog, which ->

            }
            .setPositiveButton(this@PlaylistContentFragment.resources.getText(R.string.yes)) { dialog, which ->
                playlistModel?.let { viewModel.deletePlaylist(it) }
                findNavController().navigateUp()
            }.show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                myHandleOnBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun myHandleOnBackPressed() {

        findNavController().popBackStack(R.id.mediatekaFragment,false)

    }





    override fun onClick(track: Track) {
        val bundle = Bundle().apply {
            putSerializable(Const.PUT_EXTRA_TRACK, track)
        }
        findNavController().navigate(R.id.action_playlistContentFragment_to_audioPlayerFragment,bundle)

    }

    // В PlaylistContentFragment
    override fun onResume() {
        super.onResume()
        playlistModel?.let { viewModel.updatePlaylist(it) }
    }


}

