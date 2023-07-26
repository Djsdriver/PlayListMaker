package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(), TrackAdapter.ClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModel<SearchScreenViewModel>()

    private val adapter = TrackAdapter(this)
    private val adapterHistoryList = TrackAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearch.adapter = adapter

        binding.rcHistory.adapter = adapterHistoryList
        binding.rcHistory.layoutManager = LinearLayoutManager(requireContext())

        displayingTheHistoryList() // отображение списка при загрузке

        buttonClearEditText()

        binding.updateButton.setOnClickListener {
            //showPlaceholder(null, "")
            search()
        }

        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            binding.listHistory.visibility =
                if (hasFocus && binding.editTextSearch.text.isEmpty()) View.VISIBLE else View.GONE

            adapterHistoryList.tracks = searchViewModel.loadData()
            if (searchViewModel.loadData().isNullOrEmpty()) {
                binding.showMessageHistory.visibility = View.VISIBLE
                binding.clearHistoryButton.visibility = View.INVISIBLE
            } else {
                binding.showMessageHistory.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.VISIBLE
                binding.txtYouSearch.visibility = View.VISIBLE
                adapterHistoryList.notifyDataSetChanged()
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            binding.editTextSearch.setText("")
            searchViewModel.clearHistoryList()
            binding.showMessageHistory.visibility = View.VISIBLE
            binding.listHistory.visibility = View.VISIBLE
            if (searchViewModel.loadData().isEmpty()) {
                binding.rcHistory.visibility = View.GONE
                binding.txtYouSearch.visibility = View.GONE
            } else {
                binding.rcHistory.visibility = View.VISIBLE
                binding.txtYouSearch.visibility = View.VISIBLE
            }
            binding.clearHistoryButton.visibility = View.GONE
            adapterHistoryList.notifyDataSetChanged()
        }

        binding.editTextSearch.doOnTextChanged { text, start, before, count ->
            if (!isInternetAvailable()) {
                showPlaceholder(false, getString(R.string.noInternet))
                return@doOnTextChanged
            }
            if (text?.isNotEmpty() == true) {
                search()
            }

            adapterHistoryList.tracks = searchViewModel.loadData()
            binding.imClearEditText.visibility =
                if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.listHistory.visibility = if (text.isNullOrEmpty()
                    .not() || !binding.editTextSearch.hasFocus()
            ) View.GONE else View.VISIBLE
            binding.rcHistory.visibility = if (text.isNullOrEmpty()
                    .not() || !binding.editTextSearch.hasFocus()
            ) View.GONE else View.VISIBLE
            binding.txtYouSearch.visibility =
                if ((text?.isEmpty() == true || binding.editTextSearch.hasFocus()) && searchViewModel.loadData()
                        .isEmpty()
                ) View.GONE else View.VISIBLE

            if (text?.isEmpty() == true) {
                binding.recyclerViewSearch.visibility = View.INVISIBLE
                adapter.clear()
            } else {
                binding.recyclerViewSearch.visibility = View.VISIBLE
            }

            if (searchViewModel.loadData().isEmpty() && text.isNullOrEmpty()) {
                binding.showMessageHistory.visibility = View.VISIBLE
                binding.clearHistoryButton.visibility = View.INVISIBLE
            } else {
                binding.clearHistoryButton.visibility = View.VISIBLE
                binding.rcHistory.visibility = View.VISIBLE
                binding.showMessageHistory.visibility = View.GONE
            }
            adapterHistoryList.notifyDataSetChanged()
            showPlaceholder(null, "false")
        }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
    }


    private fun displayingTheHistoryList() {
        if (searchViewModel.loadData().isEmpty()) {
            binding.listHistory.visibility = View.VISIBLE
            binding.showMessageHistory.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.INVISIBLE
            binding.txtYouSearch.visibility = View.GONE
        } else {
            adapterHistoryList.tracks = searchViewModel.loadData()
            binding.listHistory.visibility = View.VISIBLE
        }
    }

    private fun buttonClearEditText() {
        binding.imClearEditText.setOnClickListener {
            binding.editTextSearch.setText("")

            if (searchViewModel.loadData().isEmpty()) {
                binding.txtYouSearch.visibility = View.GONE
            } else {
                binding.txtYouSearch.visibility = View.VISIBLE
                binding.rcHistory.visibility = View.VISIBLE
                binding.listHistory.visibility = View.VISIBLE
            }
            adapter.clear()
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
            showPlaceholder(null)
        }
    }

    private fun search() {
        val searchTerm = binding.editTextSearch.text.toString()
        if (searchTerm.isNullOrEmpty()) return

        if (!isInternetAvailable()) {
            showPlaceholder(false, getString(R.string.noInternet))
            return
        }

        if (_binding == null) {
            Log.e("SearchFragment", "_binding is null")
            return
        }
        showPlaceholder(null)
        searchViewModel.searchDebounce(searchTerm)



        searchViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            when (tracks.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    adapter.clear()
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (tracks.data?.isNotEmpty() == true) {
                        adapter.setTrackList(tracks.data)
                        showPlaceholder(null)
                    } else {
                        showPlaceholder(true)
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    showPlaceholder(false, tracks.message ?: getString(R.string.server_error))
                }
            }
        }
    }


    private fun showPlaceholder(flag: Boolean?, message: String = "") {
        with(binding) {
            if (flag != null) {
                if (flag == true) {
                    badConnectionWidget.visibility = View.GONE
                    notFoundWidget.visibility = View.VISIBLE
                } else {
                    notFoundWidget.visibility = View.GONE
                    badConnectionWidget.visibility = View.VISIBLE
                    badConnection.text = message
                }
                adapter.clear()
            } else {
                notFoundWidget.visibility = View.GONE
                badConnectionWidget.visibility = View.GONE
            }
        }
    }


    override fun onClick(track: Track) {
        searchViewModel.addTrack(track = track)
        searchViewModel.saveData(track)
        if (searchViewModel.clickDebounce()) {
            startActivity(Intent(requireContext(), AudioPlayerActivity::class.java).apply {
                putExtra(Const.PUT_EXTRA_TRACK, track)
            })
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    override fun onResume() {
        super.onResume()
        Log.d("Resume", "Resume")
        if (searchViewModel.loadData().isNotEmpty()) {
            binding.rcHistory.visibility = View.VISIBLE
            binding.listHistory.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        binding.editTextSearch.text = null
    }
}

