package com.example.playlistmaker

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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


class NewPlaylistFragment : Fragment() {

    private val binding: FragmentNewPlaylistBinding by lazy {
        FragmentNewPlaylistBinding.inflate(layoutInflater)
    }

    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var _uri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.namePlaylistEditText.doOnTextChanged { s, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = s?.isNotEmpty() == true
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(this@NewPlaylistFragment.resources.getText(R.string.quitting_question))
            setMessage(this@NewPlaylistFragment.resources.getText(R.string.unsaved_data_caution))
            setNegativeButton(this@NewPlaylistFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            setPositiveButton(this@NewPlaylistFragment.resources.getText(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
        }


        binding.toolbarNewPlaylistCreate.setOnClickListener {
            if (binding.namePlaylistEditText.text.toString().isNotEmpty()
                    || binding.descriptionEditText.text.toString().isNotEmpty()){
                confirmDialog.show()
            } else{
                findNavController().popBackStack()
            }

        }
        binding.btnCreatePlaylist.setOnClickListener {
            val message = try {
                if (_uri == null) {
                    generationNameImage()
                } else {
                    saveImageToPrivateStorage(_uri!!, generationNameImage())
                }
                findNavController().popBackStack()

            } catch (e: Exception) {
                "error"
            }
            //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.imageNewPlaylistImage.setImageURI(uri)
                    //saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        binding.imageNewPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }

    private fun generationNameImage(): String {
        val nameOfImage = UUID.randomUUID().toString()+ ".jpg"
        return nameOfImage
    }

    private fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum")
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
    companion object {
        @JvmStatic
        fun newInstance() = NewPlaylistFragment()
    }
}


