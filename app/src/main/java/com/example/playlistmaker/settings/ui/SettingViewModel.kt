package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.IsDarkThemeEnabled
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp

class SettingViewModel(
    private val openTerms: OpenTerms,
    private val sendToSupport: SendToSupport,
    private val shareApp: ShareApp,
    private val changeAppTheme: ChangeAppTheme,
    private val isDarkThemeEnabled: IsDarkThemeEnabled,
) : ViewModel() {

    private val _isTurn = MutableLiveData<Boolean>()
    val isTurn: LiveData<Boolean> = _isTurn

    init {
        _isTurn.value = isDarkThemeEnabled.isDarkEnabled()
    }



    fun changeAppTheme(current: Boolean){
        changeAppTheme.changeAppTheme(current)
        _isTurn.value=current
    }



    fun shareApp(link: String) {
        shareApp.shareApp(link)
    }

    fun openTerms(link: String) {
        openTerms.openTerms(link)
    }

    fun sendToSupport(emailData: EmailData) {
        sendToSupport.sendToSupport(emailData)
    }

}