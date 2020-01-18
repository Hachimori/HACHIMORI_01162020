package com.github.hachimori.hachimori_01162020.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hachimori.hachimori_01162020.model.Video
import com.github.hachimori.hachimori_01162020.model.Videos
import com.github.hachimori.hachimori_01162020.network.ApiResponse
import com.github.hachimori.hachimori_01162020.repository.HachimoriRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val repository: HachimoriRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var videoListHolder: MutableList<Video> = mutableListOf()
    private val _videoList = MutableLiveData<List<Video>>()
    val videoList : LiveData<List<Video>> = _videoList

    private var playingVideoId = -1

    private val _playVideoId = MutableLiveData<Int>()
    val playVideoId : LiveData<Int> = _playVideoId

    private val _pauseVideoId = MutableLiveData<Int>()
    val pauseVideoId : LiveData<Int> = _pauseVideoId

    fun getVideoList() {
        val ret: MutableList<Video> = mutableListOf()

        uiScope.launch {
            val response: ApiResponse<Videos> = repository.getVideoList()

            when (response) {
                is ApiResponse.Success -> {
                    ret.addAll(response.data.videos)
                }
                is ApiResponse.Error -> {
                    Timber.e(response.exception)
                }
            }

            videoListHolder = ret
            _videoList.value = ret
        }
    }

    fun onClickVideo(index: Int) {
        if (playingVideoId == index) {
            _pauseVideoId.value = index
            playingVideoId = -1
        } else {
            if (playingVideoId != -1) _pauseVideoId.value = playingVideoId
            _playVideoId.value = index
            playingVideoId = index
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
