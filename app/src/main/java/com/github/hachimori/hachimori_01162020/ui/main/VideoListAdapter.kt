package com.github.hachimori.hachimori_01162020.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hachimori.hachimori_01162020.R
import com.github.hachimori.hachimori_01162020.model.Video
import com.github.hachimori.hachimori_01162020.util.loadImage
import kotlinx.android.synthetic.main.video_item.view.*

class VideoListAdapter(private val onClickListener: (Int, Video) -> Unit,
                       private val videoList: MutableList<Video>) : RecyclerView.Adapter<VideoViewHolder>() {

    private val isPlaying: MutableList<Boolean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(onClickListener, view)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position], isPlaying[position])
    }

    fun updateVideoList(newVideoList: List<Video>) {
        videoList.clear()
        videoList.addAll(newVideoList)
        isPlaying.clear()
        repeat(newVideoList.size) { isPlaying.add(false) }

        notifyDataSetChanged()
    }

    fun playVideo(videoId: Int) {
        isPlaying[videoId] = true
        notifyItemChanged(videoId)
    }

    fun pauseVideo(videoId: Int) {
        isPlaying[videoId] = false
        notifyItemChanged(videoId)
    }
}

class VideoViewHolder(private val onClickListener: (Int, Video) -> Unit,
                       itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(video: Video, isPlaying: Boolean) {
        itemView.video_item_root.setOnClickListener {
            onClickListener(adapterPosition, video)
        }
        itemView.video_item_thumbnail.loadImage(video.getThumbnailUrl())
        itemView.video_item_title.text = video.title

         if (isPlaying) {
             // TODO: hide thumbnail & play video
         } else {
             // TODO: show thumbnail & pause video
         }
    }
}
