package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.repository.SharingRepository

class SendToSupport(private  val sharingRepository: SharingRepository) {

    fun sendToSupport(emailData: EmailData){
        sharingRepository.sendToSupport(emailData = emailData)
    }
}