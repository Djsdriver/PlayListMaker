package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.repository.SharingRepository

class OpenTerms(private val sharingRepository: SharingRepository) {

    fun openTerms(link:String){
        sharingRepository.openTerms(link)
    }
}