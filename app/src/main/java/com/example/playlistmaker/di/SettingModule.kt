package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.IsDarkThemeEnabled
import com.example.playlistmaker.settings.ui.SettingViewModel
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp
import com.example.playlistmaker.utility.Const
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingModule= module {
    single(named(Const.PRACTICUM_EXAMPLE_PREFERENCES)) {
        androidContext().getSharedPreferences(Const.PRACTICUM_EXAMPLE_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get(qualifier = named(Const.PRACTICUM_EXAMPLE_PREFERENCES)))
    }

    factory { ChangeAppTheme(settingsRepository = get()) }
    factory { IsDarkThemeEnabled(settingsRepository = get()) }

    viewModel { SettingViewModel(
        openTerms = get(),
        sendToSupport = get(),
        shareApp = get(),
        changeAppTheme = get(),
        isDarkThemeEnabled = get()) }
}