package com.example.playlistmaker.common


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var trackName: TextView
    var artistName: TextView
    var trackTime: TextView
    var artworkUrl60: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackTime = itemView.findViewById(R.id.trackTime)
        artworkUrl60 = itemView.findViewById(R.id.imArtworkUrl100)
    }

    fun bind(track: Track, listener: TrackAdapter.ClickListener) {
        trackTime.text = track.trackTimeMillis?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        }



        trackName.text = track.trackName
        artistName.text = track.artistName

        Glide.with(itemView)
            .load(track.artworkUrl60.replaceAfterLast('/', "60x60bb.jpg"))
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.track_album_corner_radius
                    )
                )
            )
            .placeholder(R.drawable.placeholder)
            .into(artworkUrl60)
        itemView.setOnClickListener {
            listener.onClick(track)
        }

    }
}