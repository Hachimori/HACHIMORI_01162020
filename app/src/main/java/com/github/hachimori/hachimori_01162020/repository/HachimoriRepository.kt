package com.github.hachimori.hachimori_01162020.repository

import androidx.lifecycle.ViewModel
import com.github.hachimori.hachimori_01162020.model.Videos
import com.github.hachimori.hachimori_01162020.network.ApiResponse
import com.github.hachimori.hachimori_01162020.network.HachimoriService

class HachimoriRepository(private val service: HachimoriService) : ViewModel() {

    suspend fun getVideoList(): ApiResponse<Videos> {
        return try {
            val videos: Videos = service.getVideoList().await()
            ApiResponse.Success(videos)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}
