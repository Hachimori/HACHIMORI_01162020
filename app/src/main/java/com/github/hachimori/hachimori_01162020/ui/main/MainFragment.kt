package com.github.hachimori.hachimori_01162020.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hachimori.hachimori_01162020.R
import com.github.hachimori.hachimori_01162020.network.HachimoriService
import com.github.hachimori.hachimori_01162020.repository.HachimoriRepository
import com.github.hachimori.hachimori_01162020.util.singleArgViewModelFactory
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = singleArgViewModelFactory(::MainViewModel) (
          HachimoriRepository(HachimoriService.getService())
        )
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewModel.getVideoList()

        viewModel.videoList.observe(this, Observer { response ->
            Timber.i("Retrieved videos:")
            for (video in response) {
                Timber.i(video.getThumbnailUrl())
            }
            (main_video_list.adapter as VideoListAdapter).updateVideoList(response)
        })

        viewModel.playVideoId.observe(this, Observer { videoId ->
            (main_video_list.adapter as VideoListAdapter).playVideo(videoId)
        })

        viewModel.pauseVideoId.observe(this, Observer { videoId ->
            (main_video_list.adapter as VideoListAdapter).pauseVideo(videoId)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_video_list.layoutManager = LinearLayoutManager(context)
        main_video_list.adapter = VideoListAdapter({ index, video ->
            Timber.i("Clicked video index: ${index},  video_url: ${video.video_url}")
            viewModel.onClickVideo(index)
        },
            viewModel.videoList.value?.toMutableList() ?: mutableListOf())
    }
}
