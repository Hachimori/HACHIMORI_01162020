package com.github.hachimori.hachimori_01162020.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hachimori.hachimori_01162020.R
import com.github.hachimori.hachimori_01162020.model.Video
import com.github.hachimori.hachimori_01162020.util.loadImage
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.video_item.view.*
import timber.log.Timber
import java.util.*

class VideoListAdapter(private val videoList: MutableList<Video>) : RecyclerView.Adapter<VideoViewHolder>() {

    private val playerList: MutableList<SimpleExoPlayer?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.video_item, parent, false)
        return VideoViewHolder({ videoId, video ->
            playerList.forEachIndexed { idx, player ->
                if (player?.playWhenReady == true) {
                    Timber.i("Pauses video (another video is clicked): ${idx}, ${videoList[idx].getThumbnailUrl()}")
                    pauseVideo(idx)
                }
            }
            Timber.i("Plays video: ${videoId},  ${video.getThumbnailUrl()}")
            playVideo(videoId)
        }, view)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (playerList[position] == null) {
            playerList[position] = SimpleExoPlayer
                .Builder(holder.itemView.context)
                .build()

            playerList[position]!!.addListener(object: Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if ((!playWhenReady && playbackState == Player.STATE_READY) || playbackState == Player.STATE_ENDED) {
                        Timber.i("Pauses video (pause button is clicked / video finished): ${position}, ${videoList[position].getThumbnailUrl()}")
                        if (playbackState == Player.STATE_ENDED) {
                            playerList[position]?.seekTo(0)
                        }
                        pauseVideo(position)
                    }
                }
            })
        }
        holder.bind(videoList[position], playerList[position]!!)
    }

    fun updateVideoList(newVideoList: List<Video>) {
        videoList.clear()
        videoList.addAll(newVideoList)
        playerList.clear()
        repeat(newVideoList.size) {
            playerList.add(null)
        }

        notifyDataSetChanged()
    }

    fun playVideo(videoId: Int) {
        playerList[videoId]?.playWhenReady = true
        notifyItemChanged(videoId)
    }

    fun pauseVideo(videoId: Int) {
        playerList[videoId]?.playWhenReady = false
        notifyItemChanged(videoId)
    }
}

class VideoViewHolder(private val onThumbnailClicked: (Int, Video) -> Unit,
                      itemView: View) : RecyclerView.ViewHolder(itemView) {


    fun bind(video: Video, player: SimpleExoPlayer) {
        itemView.video_item_thumbnail.setOnClickListener {
            onThumbnailClicked(adapterPosition, video)
        }
        itemView.video_item_thumbnail.loadImage(video.getThumbnailUrl())
        itemView.video_item_title.text = video.title
        itemView.video_item_player_view.player = player

        if (player.playWhenReady) {
            if (player.playbackState == Player.STATE_IDLE) {
                val audioUrl = video.audio_urls.getAudioUrlByLanguage(Locale.getDefault().language)
                val dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(itemView.context, itemView.context.packageName))
                val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    Uri.parse(video.video_url))
                val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(audioUrl))

                player.prepare(MergingMediaSource(videoSource, audioSource))
            }

            itemView.video_item_thumbnail_group.visibility = View.INVISIBLE
            itemView.video_item_player_view.visibility = View.VISIBLE
        } else {
            itemView.video_item_thumbnail_group.visibility = View.VISIBLE
            itemView.video_item_player_view.visibility = View.INVISIBLE
        }
    }
}
