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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
