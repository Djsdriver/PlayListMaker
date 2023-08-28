package com.example.playlistmaker.media.addPlayList.presention.ui

import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.models.PlaylistModel
import java.io.File

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {
    var tracks = ArrayList<PlaylistModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }



    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun setTrackList(list: List<PlaylistModel>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }
}
class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val filePath = File(view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_album")
    private val title: TextView = view.findViewById(R.id.text_title)
    private val trackCount: TextView = view.findViewById(R.id.text_tracks_quantity)
    private val imagePlaylist: ImageView = view.findViewById(R.id.iv_playlist_big_image)

    fun bind(playlistModel: PlaylistModel) {
        title.text = playlistModel.name
        trackCount.text = playlistModel.trackCount.toString()

        val imageFile = File(filePath, playlistModel.imagePath)
        Glide.with(itemView)
            .load(imageFile)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imagePlaylist)
    }
}