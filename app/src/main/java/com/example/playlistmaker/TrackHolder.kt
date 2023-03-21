package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ItemTrackBinding
import java.text.SimpleDateFormat
import java.util.*

class TrackHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var trackName: TextView
    var artistName: TextView
    var trackTime: TextView
    var artworkUrl100: ImageView
    init {
        trackName=itemView.findViewById(R.id.trackName)
        artistName=itemView.findViewById(R.id.artistName)
        trackTime=itemView.findViewById(R.id.trackTime)
        artworkUrl100=itemView.findViewById(R.id.imArtworkUrl100)
    }

    fun bind(track: Track){
        val data= SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        trackName.text=track.trackName
        artistName.text=track.artistName
        trackTime.text=data
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.placeholder)
            .into(artworkUrl100)

    }
}