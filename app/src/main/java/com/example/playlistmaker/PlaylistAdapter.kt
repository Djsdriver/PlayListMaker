package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class PlaylistAdapter(): RecyclerView.Adapter<PlaylistViewHolder>() {
    var tracks = ArrayList<ItemPlaylist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun setTrackList(list: List<ItemPlaylist>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }
}
class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.text_title)
    private val description: TextView = view.findViewById(R.id.text_tracks_quantity)
    private val imagePlaylist: ImageView = view.findViewById(R.id.iv_playlist_big_image)

    fun bind(itemPlaylist: ItemPlaylist) {
        title.text = itemPlaylist.name
        description.text = itemPlaylist.tracksQuantity.toString()
        Glide.with(itemView)
            .load(itemPlaylist.playlistImagePath)
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.track_album_corner_radius
                    )
                )
            )
            .placeholder(R.drawable.placeholder)
            .into(imagePlaylist)
    }
}