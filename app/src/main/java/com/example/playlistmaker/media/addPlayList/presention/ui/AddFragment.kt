package com.example.playlistmaker.media.addPlayList.presention.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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

import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.ui.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class AddFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private var uriImage: Uri? = null
    lateinit var confirmDialog: MaterialAlertDialogBuilder



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentNewPlaylistBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.namePlaylistEditText.doOnTextChanged { s, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = s?.isNotEmpty() == true
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(this@AddFragment.resources.getText(R.string.quitting_question))
            setMessage(this@AddFragment.resources.getText(R.string.unsaved_data_caution))
            setNegativeButton(this@AddFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            setPositiveButton(this@AddFragment.resources.getText(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()

            }
        }


        binding.toolbarNewPlaylistCreate.setOnClickListener {
            if (binding.namePlaylistEditText.text.toString().isNotEmpty()
                || binding.descriptionEditText.text.toString().isNotEmpty()){
                confirmDialog.show()
            } else{
                findNavController().navigateUp()
            }

        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.imageNewPlaylistImage.setImageURI(uri)
                    uriImage=uri
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
                if (uriImage == null) {

                } else {
                    saveImageToPrivateStorage(uriImage!!, addPlaylistToDatabase())
                    //addPlaylistToDatabase()


                }
               val message= this.resources.getString(R.string.playlist_created, binding.namePlaylistEditText.text)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }


        }

    private fun addPlaylistToDatabase(): String {
        val name = binding.namePlaylistEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val generationName= "$name-${playlistViewModel.generateImageNameForStorage()}"

        playlistViewModel.insertPlaylistToDatabase(
            PlaylistEntity(
            name = name,
            description = description,
            imagePath = generationName,
            tracksId = emptyList(),
            trackCount = 0
        )
        )
        return generationName
    }


    private fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"my_album")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, nameOfImage)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }



}
