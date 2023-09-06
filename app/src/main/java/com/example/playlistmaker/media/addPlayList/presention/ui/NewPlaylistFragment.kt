package com.example.playlistmaker.media.addPlayList.presention.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel



class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val newPlaylistFragmentViewModel by viewModel<NewPlaylistFragmentViewModel>()
    private var uriImage: Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentNewPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newPlaylistFragmentViewModel.uriImage.observe(viewLifecycleOwner) { uri ->
            uriImage = uri
        }

        binding.namePlaylistEditText.doOnTextChanged { s, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = s?.isNotEmpty() == true
        }


        binding.toolbarNewPlaylistCreate.setOnClickListener {
            if (checkIfThereAreUnsavedData()){
                showDialog()
            } else{
                findNavController().navigateUp()
            }

        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.imageNewPlaylistImage.setImageURI(uri)
                    newPlaylistFragmentViewModel.setUriImage(uri)

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
            val name = binding.namePlaylistEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            lifecycleScope.launch {
                newPlaylistFragmentViewModel.createNewPlaylist(name = name, description = description)

                val message = resources.getString(R.string.playlist_created, name)
                findNavController().navigateUp()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }


        }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(this@NewPlaylistFragment.resources.getText(R.string.quitting_question))
            .setMessage(this@NewPlaylistFragment.resources.getText(R.string.unsaved_data_caution))
            .setNegativeButton(this@NewPlaylistFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(this@NewPlaylistFragment.resources.getText(R.string.finish)) { dialog, which ->
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
        _binding=null
    }


}

