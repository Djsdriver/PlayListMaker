package com.example.playlistmaker.sharing.data.repository

import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.repository.SharingRepository

class SharingRepositoryImpl(private val externalNavigator: ExternalNavigator): SharingRepository {
    override fun shareApp(link: String) {
        externalNavigator.shareApp(link)
    }

    override fun sendToSupport(emailData: EmailData) {
        externalNavigator.sendToSupport(emailData = emailData)
    }

    override fun openTerms(link: String) {

        externalNavigator.openTerms(link)

    }
}