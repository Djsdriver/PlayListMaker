package com.example.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigator(private val context: Context) {

    fun openTerms(link: String) {
        val nextIntent= Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(context,Intent.createChooser(nextIntent,""),null)
    }

    fun sendToSupport(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO);
        supportIntent.data=Uri.parse("mailto:")

        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.sender))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)

        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        startActivity(context,Intent.createChooser(supportIntent,""),null)
    }

    fun shareApp(link: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(context,Intent.createChooser(shareIntent,link),null)
    }
}