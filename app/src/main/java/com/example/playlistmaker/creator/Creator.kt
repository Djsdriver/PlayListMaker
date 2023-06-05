package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.sharing.data.repository.ExternalNavigator
import com.example.playlistmaker.sharing.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.SharingRepository

object Creator {

    fun provideSharing(externalNavigator: ExternalNavigator): SharingRepository {
        return SharingRepositoryImpl(externalNavigator)
    }
}