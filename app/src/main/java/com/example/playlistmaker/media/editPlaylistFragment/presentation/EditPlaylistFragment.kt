package com.example.playlistmaker.media.editPlaylistFragment.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toPlaylistEntity
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
            viewModel.deleteImageFromStorage(playlistModel!!.imagePath)
            val name = binding.namePlaylistEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            viewModel.editPlaylist(
                name,
                description,
                playlistModel,
                uriImage
            ) { updatedPlaylist ->

                    // Здесь запускаете navigateToPlaylistContent только в случае выбора новой картинки
                    val bundle = Bundle().apply {
                        putSerializable(Const.PUT_EXTRA_PLAYLIST, updatedPlaylist)
                    }
                    findNavController().navigate(
                        R.id.action_editPlaylistFragment_to_playlistContentFragment,
                        bundle
                    )

            }
        }


    }



    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(),R.style.MyDialogTheme)
            .setTitle(this@EditPlaylistFragment.resources.getText(R.string.quitting_question_edit))
            .setMessage(this@EditPlaylistFragment.resources.getText(R.string.unsaved_data_caution))
            .setNegativeButton(this@EditPlaylistFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(this@EditPlaylistFragment.resources.getText(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }.show()
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.dialog_background)
        dialog.window?.setBackgroundDrawable(backgroundDrawable)
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
            findNavController().popBackStack()
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditPlaylistFragment().apply {}
    }

}