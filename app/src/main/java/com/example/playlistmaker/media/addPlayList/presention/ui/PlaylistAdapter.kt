package com.example.playlistmaker.media.addPlayList.presention.ui

import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.models.PlaylistModel

import java.io.File

class PlaylistAdapter(val listenerItemPlaylist: ClickListener, private val isVertical: Boolean) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    var tracks = ArrayList<PlaylistModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutId = if (isVertical) R.layout.item_playlist else R.layout.item_playlist_horizontal
        val itemView = layoutInflater.inflate(layoutId, parent, false)

        return PlaylistViewHolder(itemView)
    }


    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position], listenerItemPlaylist)
    }

    fun setTrackList(list: List<PlaylistModel>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onClick(playlistModel: PlaylistModel)
    }
}

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val filePath =
        File(view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_album")
    private val title: TextView = view.findViewById(R.id.text_title)
    private val trackCount: TextView = view.findViewById(R.id.text_tracks_quantity)
    private val imagePlaylist: ImageView = view.findViewById(R.id.iv_playlist_big_image)

    fun bind(playlistModel: PlaylistModel, listenerItemPlaylist: PlaylistAdapter.ClickListener) {
        title.text = playlistModel.name
        trackCount.text = "${playlistModel.tracksId.size} треков"

        val imageFile = File(filePath, playlistModel.imagePath)
        Glide.with(itemView)
            .load(imageFile)
            .placeholder(R.drawable.ic_launcher_background)
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.track_album_corner_radius
                    )
                )
            )
            .apply(
                RequestOptions().override(
                    1600
                )
            )
            .into(imagePlaylist)


        itemView.setOnClickListener {
            listenerItemPlaylist.onClick(playlistModel)
        }
    }
}