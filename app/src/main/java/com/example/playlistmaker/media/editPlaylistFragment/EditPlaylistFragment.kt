package com.example.playlistmaker.media.editPlaylistFragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.databinding.FragmentPlaylistContentBinding
import com.example.playlistmaker.di.addPlaylistModule
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.presentation.ui.PlaylistContentFragmentViewModel
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toPlaylistEntity
import com.example.playlistmaker.utility.toTrackEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditPlaylistFragment : Fragment() {


    private val binding: FragmentEditPlaylistBinding by lazy {
        FragmentEditPlaylistBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<EditPlaylistFragmentViewModel>()
    private var playlistModel: PlaylistModel?=null

    private var uriImage: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(Const.PUT_EXTRA_PLAYLIST, PlaylistModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(Const.PUT_EXTRA_PLAYLIST) as PlaylistModel?
        }

       // val playlistModelEdit= playlistModel?.let { viewModel.getPlaylist(it.id) }



        binding.toolbarNewPlaylistCreate.setTitle("Редактировать")
        binding.btnCreatePlaylist.setText("Сохранить")
        binding.namePlaylistEditText.setText(playlistModel?.name)
        binding.descriptionEditText.setText(playlistModel?.description)
        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_album")
        val imageFile = playlistModel?.imagePath?.let { File(filePath, it) }

        Glide.with(requireContext())
            .load(imageFile)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.imageNewPlaylistImage)

        viewModel.uriImage.observe(viewLifecycleOwner) { uri ->
            uriImage = uri
        }

        binding.namePlaylistEditText.doOnTextChanged { s, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = s?.isNotEmpty() == true
        }


        binding.toolbarNewPlaylistCreate.setOnClickListener {
           findNavController().popBackStack()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.imageNewPlaylistImage.setImageURI(uri)
                    viewModel.setUriImage(uri)

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        binding.imageNewPlaylistImage.setOnClickListener {

            //по нажатию на кнопку pickImage запускаем photo picker
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        binding.btnCreatePlaylist.setOnClickListener {
            editPlaylist()
        }


    }

    private fun editPlaylist() {
        playlistModel?.let { deleteOldFile(it.imagePath) }
        val filepath = if (uriImage != null) {
            viewModel.saveImageToPrivateStorage(uriImage!!)
            viewModel.generationName
        } else {
            playlistModel!!.imagePath
        }

        val updatedPlaylist = playlistModel?.copy(
            name = binding.namePlaylistEditText.text.toString(),
            description = binding.descriptionEditText.text.toString(),
            imagePath = filepath,
            tracks= playlistModel!!.tracks,
            trackCount = 0

        )

        viewLifecycleOwner.lifecycleScope.launch {
            if (updatedPlaylist != null) {
                viewModel.insertPlaylistToDatabase(updatedPlaylist.toPlaylistEntity())
            }
        }


        if (updatedPlaylist != null) {
            val bundle=Bundle().apply {
                putSerializable(Const.PUT_EXTRA_PLAYLIST,updatedPlaylist)
            }
            findNavController().navigate(
                R.id.action_editPlaylistFragment_to_playlistContentFragment,bundle
            )

        }

    }

    private fun deleteOldFile(nameOfFile: String) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_album")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, nameOfFile)

        if (file.exists()) {
            file.delete()
        } else {
            Log.d("EditPlaylistFragment", "File not found")
        }
    }


    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(this@EditPlaylistFragment.resources.getText(R.string.quitting_question))
            .setMessage(this@EditPlaylistFragment.resources.getText(R.string.unsaved_data_caution))
            .setNegativeButton(this@EditPlaylistFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(this@EditPlaylistFragment.resources.getText(R.string.finish)) { dialog, which ->
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
        if (checkIfThereAreUnsavedData()) {
            showDialog()
        } else {
            findNavController().popBackStack()
        }
    }
    private fun checkIfThereAreUnsavedData(): Boolean {
        return binding.namePlaylistEditText.text.toString().isNotEmpty()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //_binding=null
    }
    companion object {
        private const val TAG = "EditPlaylistFragment"

        @JvmStatic
        fun newInstance() = EditPlaylistFragment().apply {}
    }

}
