package com.github.hachimori.hachimori_01162020.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Videos(
    val videos: List<Video>
)

@Parcelize
data class Video(
    val video_url: String,
    val audio_urls: AudioUrls,
    val thumb: String,
    val title: String
) : Parcelable {

    fun getThumbnailUrl(): String {
        val filename = video_url.split('/').last()
        return video_url.replaceFirst(Regex("${filename}$"), thumb)
    }
}

@Parcelize
data class AudioUrls(
    val en: String,
    val de: String?,
    val cn: String?,
    val zh: String?
) : Parcelable {
    fun getAudioUrlByLanguage(language: String): String {
        if (language == "de" && de != null) return de
        if (language == "cn" && cn != null) return cn
        if (language == "zh" && zh != null) return zh
        return en
    }
}