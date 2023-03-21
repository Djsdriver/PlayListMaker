package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ItemTrackBinding

class TrackHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

   // lateinit var binding: ItemTrackBinding

    var trackName: TextView
    var artistName: TextView
    var trackTime: TextView
    var artworkUrl100: ImageView
    init {
        trackName=itemView.findViewById(R.id.trackName)
        artistName=itemView.findViewById(R.id.artistName)
        trackTime=itemView.findViewById(R.id.trackTime)
        artworkUrl100=itemView.findViewById(R.id.imArtworkUrl100)
    // binding= ItemTrackBinding.bind(itemView)
    }

    fun bind(track: Track){
        trackName.text=track.trackName
        artistName.text=track.artistName
        trackTime.text=track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.imageCornerRadius)))
            .placeholder(R.mipmap.ic_launcher)
            .error(R.drawable.baseline_error_24)
            .into(artworkUrl100)

    }
}