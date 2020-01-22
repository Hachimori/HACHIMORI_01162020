package com.github.hachimori.hachimori_01162020.ui.fullscreen_video


import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.hachimori.hachimori_01162020.R
import com.github.hachimori.hachimori_01162020.model.Video
import com.github.hachimori.hachimori_01162020.util.Constants
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fullscreen_video_activity.*
import java.util.*

class FullScreenVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen_video_activity)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        fullscreen_video_exit_ic.setOnClickListener {
            finish()
        }

        val player = SimpleExoPlayer.Builder(this).build()
        fullscreen_video_player_view.player = player

        val video = intent.getParcelableExtra<Video>(Constants.EXTRA_VIDEO)
        val audioUrl = video.audio_urls.getAudioUrlByLanguage(Locale.getDefault().language)
        val dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(this, this.packageName))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.video_url))
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(audioUrl))
        player.prepare(MergingMediaSource(videoSource, audioSource))

        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        fullscreen_video_player_view.player?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        fullscreen_video_player_view.player?.release()
    }
}