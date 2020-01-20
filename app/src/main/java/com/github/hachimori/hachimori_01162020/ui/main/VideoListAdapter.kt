package com.github.hachimori.hachimori_01162020.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hachimori.hachimori_01162020.R
import com.github.hachimori.hachimori_01162020.model.Video
import com.github.hachimori.hachimori_01162020.util.loadImage
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.video_item.view.*

class VideoListAdapter(private val onClickListener: (Int, Video) -> Unit,
                       private val videoList: MutableList<Video>) : RecyclerView.Adapter<VideoViewHolder>() {

    private val isPlaying: MutableList<Boolean> = mutableListOf()
    private val playerList: MutableList<SimpleExoPlayer?> = mutableListOf()
    private val isPrepared: MutableList<Boolean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(onClickListener, view)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (playerList[position] == null) {
            playerList[position] = SimpleExoPlayer.Builder(holder.itemView.context).build()
        }
        holder.bind(videoList[position], playerList[position]!!, isPlaying[position], isPrepared[position]) { isPrepared[position] = true }
    }

    fun updateVideoList(newVideoList: List<Video>) {
        videoList.clear()
        videoList.addAll(newVideoList)
        isPlaying.clear()
        playerList.clear()
        isPrepared.clear()
        repeat(newVideoList.size) {
            isPlaying.add(false)
            playerList.add(null)
            isPrepared.add(false)
        }

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


    fun bind(video: Video, player: SimpleExoPlayer, isPlaying: Boolean, isPrepared: Boolean, onDidPrepare: () -> Unit) {
        itemView.video_item_root.setOnClickListener {
            onClickListener(adapterPosition, video)
        }
        itemView.video_item_thumbnail.loadImage(video.getThumbnailUrl())
        itemView.video_item_title.text = video.title

        PlayerView.switchTargetView(player, null, itemView.video_item_player_view)

        if (isPlaying) {
            val dataSourceFactory = DefaultDataSourceFactory(itemView.context, Util.getUserAgent(itemView.context, itemView.context.packageName))
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.video_url))
            val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.audio_urls.en))

            if (!isPrepared) {
                if (!(player.isLoading || player.isPlaying)) {
                    player.prepare(MergingMediaSource(videoSource, audioSource))
                    onDidPrepare()
                }
            }

            player.playWhenReady = true

            itemView.video_item_thumbnail_group.visibility = View.INVISIBLE
            itemView.video_item_player_view.visibility = View.VISIBLE
        } else {
            if (isPrepared) {
                player.playWhenReady = false
            }
            itemView.video_item_thumbnail_group.visibility = View.VISIBLE
            itemView.video_item_player_view.visibility = View.INVISIBLE
         }
    }
}
