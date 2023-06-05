package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(val listener: ClickListener) : RecyclerView.Adapter<TrackHolder>() {
    var tracks=ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackHolder((view))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(track = tracks[position],listener)
    }

    fun clear(){
        tracks.clear()
        notifyDataSetChanged()
    }

    fun setTrackList(list: List<Track>){
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }
}