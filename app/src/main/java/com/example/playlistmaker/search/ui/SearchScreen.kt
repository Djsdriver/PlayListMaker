package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.*
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.Resource
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named


class SearchScreen : AppCompatActivity(), TrackAdapter.ClickListener {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val searchViewModel by viewModel<SearchScreenViewModel>()


    val adapter = TrackAdapter(this)
    val adapterHistoryList = TrackAdapter(this)


    companion object {
        private var isClickAllowed = true

        private val handler = Handler(Looper.getMainLooper())

    }

    private val searchRunnable = Thread { search() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, Const.CLICK_DEBOUNCE_DELAY)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Const.KEY_EDIT_TEXT, binding.editTextSearch.toString())
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            val editText = savedInstanceState.getString(Const.KEY_EDIT_TEXT)
            binding.editTextSearch.setText(editText)
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.adapter = adapter

        binding.rcHistory.adapter = adapterHistoryList
        binding.rcHistory.layoutManager = LinearLayoutManager(this)



        displayingTheHistoryList() // отображение списка при загрузке

        buttonClearEditText()

        binding.updateButton.setOnClickListener {
            showPlaceholder(null,"")
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
                binding.txtYouSearch.visibility=View.VISIBLE
                adapterHistoryList.notifyDataSetChanged()
            }


        }

        binding.clearHistoryButton.setOnClickListener {
            binding.editTextSearch.setText("")
            searchViewModel.clearHistoryList()
            binding.showMessageHistory.visibility = View.VISIBLE
            binding.listHistory.visibility = View.VISIBLE
            if (searchViewModel.loadData().isEmpty()){
                binding.rcHistory.visibility=View.GONE
                binding.txtYouSearch.visibility=View.GONE

            } else{
                binding.rcHistory.visibility=View.VISIBLE
               binding.txtYouSearch.visibility=View.VISIBLE
            }
            binding.clearHistoryButton.visibility = View.GONE
            adapterHistoryList.notifyDataSetChanged()

        }

        binding.editTextSearch.doOnTextChanged { text, start, before, count ->
            searchDebounce()
            hideListBeforeUploading()
            adapterHistoryList.tracks = searchViewModel.loadData()
            binding.imClearEditText.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.listHistory.visibility = if (text.isNullOrEmpty().not() || !binding.editTextSearch.hasFocus()) View.GONE else View.VISIBLE
            binding.rcHistory.visibility = if (text.isNullOrEmpty().not() || !binding.editTextSearch.hasFocus()) View.GONE else View.VISIBLE
            binding.txtYouSearch.visibility = if (text.isNullOrEmpty().not() || !binding.editTextSearch.hasFocus()) View.GONE else View.VISIBLE

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
    private fun hideListBeforeUploading() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewSearch.visibility = View.INVISIBLE
        adapter.clear()
    }



    private fun displayingTheHistoryList() {
        if (searchViewModel.loadData().isEmpty()) {
            binding.listHistory.visibility = View.VISIBLE
            binding.showMessageHistory.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.INVISIBLE
            binding.txtYouSearch.visibility=View.GONE
        } else {
            adapterHistoryList.tracks = searchViewModel.loadData()
            binding.listHistory.visibility = View.VISIBLE
        }
    }

    private fun buttonClearEditText() {
        binding.imClearEditText.setOnClickListener {
            binding.editTextSearch.setText("")
            binding.txtYouSearch.visibility=View.VISIBLE
            adapter.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
            showPlaceholder(null)
        }
    }

    private fun search() {
        val searchTerm = binding.editTextSearch.text.toString()
        if (searchTerm.isEmpty()) return

        searchViewModel.searchTracks(searchTerm)

        searchViewModel.tracks.observe(this) { tracks ->
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


    fun showPlaceholder(flag: Boolean?, message: String = "") = with(binding) {
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


    private fun darkOrLightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_color)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_color)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(track: Track) {
        searchViewModel.addTrack(track = track)
        searchViewModel.saveData(track)
        if (clickDebounce()) {
            startActivity(Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(Const.PUT_EXTRA_TRACK, track)
            })
        }

    }
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Const.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        Log.d("Resume", "Resume")
        adapterHistoryList.tracks = searchViewModel.loadData()
        adapterHistoryList.notifyDataSetChanged()
    }



}
