package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.retrofit.TrackApi
import com.example.playlistmaker.retrofit.TrackResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchScreen : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    val adapter=TrackAdapter()
    val listSong= ArrayList<Track>()


    companion object {
        const val KEY_EDIT_TEXT = "KEY_EDIT_TEXT"
        const val BASE_URL = "https://itunes.apple.com"

        private fun viewVisible(s: CharSequence?): Int {
            return if (s.isNullOrBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        val retrofit= Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val itemTrack=retrofit.create(TrackApi::class.java)



    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EDIT_TEXT, binding.editTextSearch.toString())
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            val editText = savedInstanceState.getString(KEY_EDIT_TEXT)
            binding.editTextSearch.setText(editText)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        darkOrLightTheme()

        binding.recyclerViewSearch.layoutManager=LinearLayoutManager(this)
        binding.recyclerViewSearch.adapter=adapter
        adapter.tracks=listSong


        binding.imClearEditText.setOnClickListener {
            binding.editTextSearch.setText("")
            listSong.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
        }

        binding.updateButton.setOnClickListener {
            search()
        }

        val simpleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    s.isNullOrEmpty() -> {
                        binding.imClearEditText.visibility = viewVisible(s)
                    }
                    else -> {
                        binding.imClearEditText.visibility = viewVisible(s)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        binding.editTextSearch.addTextChangedListener(simpleWatcher)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

    }

    private fun search() {
        itemTrack.getTrackByTerm(binding.editTextSearch.text.toString())
            .enqueue(object : Callback<TrackResultResponse> {
                override fun onResponse(
                    call: Call<TrackResultResponse>,
                    response: Response<TrackResultResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                listSong.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showPlaceholder(null)
                            } else {
                                showPlaceholder(true)
                            }
                        }
                        else -> {
                            showPlaceholder(false, getString(R.string.server_error))
                        }
                    }

                }

                override fun onFailure(call: Call<TrackResultResponse>, t: Throwable) {
                    showPlaceholder(false, getString(R.string.bad_connection))
                }

            })
        true
    }

    fun showPlaceholder(flag: Boolean?, message: String = "") = with(binding){
        if (flag != null) {
            if (flag == true) {
                badConnectionWidget.visibility = View.GONE
                notFoundWidget.visibility = View.VISIBLE
            } else {
                notFoundWidget.visibility = View.GONE
                badConnectionWidget.visibility = View.VISIBLE
                badConnection.text = message
            }
            listSong.clear()
            adapter.notifyDataSetChanged()
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
}






