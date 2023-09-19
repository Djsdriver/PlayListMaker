package com.example.playlistmaker.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(val listener: ClickListener) : RecyclerView.Adapter<TrackHolder>() {
    var tracks = ArrayList<Track>()
    var longClickListener: LongClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackHolder((view))
    }

    override fun getItemCount(): Int = tracks.size

    fun setClickListener(listener: LongClickListener) {
        longClickListener = listener
    }
    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
            holder.bind(track = tracks[position], listener)
            holder.itemView.setOnLongClickListener {
                val track = tracks[position]
                longClickListener?.onLongClick(track)
                true
            }
    }

    fun clear() {
        tracks.clear()
        notifyDataSetChanged()
    }

    fun setTrackList(list: List<Track>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }

    fun removeTrack(position: Int) {
        if (position in 0 until tracks.size) {
            tracks.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }

    fun interface LongClickListener {
        fun onLongClick(track: Track)
    }
}