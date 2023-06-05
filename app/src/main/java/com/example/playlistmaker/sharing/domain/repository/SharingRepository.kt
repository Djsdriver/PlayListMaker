package com.example.playlistmaker.sharing.domain.repository

import com.example.playlistmaker.sharing.domain.models.EmailData

interface SharingRepository {

    fun shareApp(link: String)

    fun sendToSupport(emailData: EmailData)

    fun openTerms(link: String)
}