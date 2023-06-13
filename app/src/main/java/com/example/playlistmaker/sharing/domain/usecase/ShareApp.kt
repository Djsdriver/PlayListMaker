package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.repository.SharingRepository

class ShareApp(private val sharingRepository: SharingRepository) {


    fun shareApp(link: String){
        sharingRepository.shareApp(link)
    }
}