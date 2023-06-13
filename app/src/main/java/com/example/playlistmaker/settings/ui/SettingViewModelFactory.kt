package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.IsDarkThemeEnabled
import com.example.playlistmaker.sharing.data.repository.ExternalNavigator
import com.example.playlistmaker.sharing.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.SharingRepository
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp

class SettingViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val externalNavigator=ExternalNavigator(context)
    private var sharingRepositoryImpl= SharingRepositoryImpl(externalNavigator)
    private val openTerms by lazy { OpenTerms(sharingRepositoryImpl) }
    private val sendToSupport by lazy { SendToSupport(sharingRepositoryImpl) }
    private val shareApp by lazy { ShareApp(sharingRepositoryImpl) }

    private val settingsRepositoryImpl=SettingsRepositoryImpl(context)
    private val changeAppTheme by lazy { ChangeAppTheme(settingsRepositoryImpl) }
    private val isDarkThemeEnabled by lazy { IsDarkThemeEnabled(settingsRepositoryImpl) }



    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(
            openTerms = openTerms,
            sendToSupport = sendToSupport,
            shareApp = shareApp,
            changeAppTheme = changeAppTheme,
            isDarkThemeEnabled = isDarkThemeEnabled) as T

    }
}