package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TrackHolder>() {
    var tracks=ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackHolder((view))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(track = tracks[position])
    }
}