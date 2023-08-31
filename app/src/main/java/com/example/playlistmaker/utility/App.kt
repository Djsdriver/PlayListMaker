package com.example.playlistmaker.utility

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.addPlaylistModule
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.playlist
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingModule
import com.example.playlistmaker.di.shareModule
import com.example.playlistmaker.utility.Const.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.playlistmaker.utility.Const.SWITCH_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    var darkTheme = false


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(
                playerModule,
                searchModule,
                settingModule,
                shareModule,
                dataModule,
                mediaModule,
                repositoryModule,
                playerModule,
                playlist,
                addPlaylistModule) )
        }

        val sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val isTurn = sharedPrefs.getBoolean(SWITCH_KEY, false)
        switchTheme(isTurn)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
