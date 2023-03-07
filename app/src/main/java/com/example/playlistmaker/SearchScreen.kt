package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding


class SearchScreen : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }


    companion object {
        const val KEY_EDIT_TEXT = "KEY_EDIT_TEXT"

        private fun viewVisible(s: CharSequence?): Int {
            return if (s.isNullOrBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }


//лучше создавать фиксированный список???
        val listTrackName = listOf(
            "Smells Like Teen Spirit",
            "Billie Jean",
            "Stayin' Alive",
            "Whole Lotta Love",
            "Sweet Child O'Mine"
        )
        val listArtistName =
            listOf("Nirvana", "Michael Jackson", "Bee Gees", "Led Zeppelin", "Guns N' Roses")
        val listTrackTime = listOf("5:01", "4:35", "4:10", "5:33", "5:03")
        val listArtworkUrl100 = listOf(
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )

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

        //или лучше держать массив в ресурсах?
        val TrackName =resources.getStringArray(R.array.listTrackName)
        val ArtistName =resources.getStringArray(R.array.listArtistName)
        val TrackTime =resources.getStringArray(R.array.listTrackTime)
        val ArtworkUrl100 =resources.getStringArray(R.array.listArtworkUrl100)





        binding.recyclerViewSearch.adapter = TrackAdapter(List(5) {
            Track(
                TrackName[it].toString(), ArtistName[it].toString(), TrackTime[it].toString(), ArtworkUrl100[it].toString()
            )
        } as ArrayList<Track>)

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)


        binding.imClearEditText.setOnClickListener {
            binding.editTextSearch.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
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






