package com.github.hachimori.hachimori_01162020.model


data class Videos(
    val videos: List<Video>
)

data class Video(
    val video_url: String,
    val audio_urls: AudioUrls,
    val thumb: String,
    val title: String
) {

    fun getThumbnailUrl(): String {
        val filename = video_url.split('/').last()
        return video_url.replaceFirst(Regex("${filename}$"), thumb)
    }
}

data class AudioUrls(
    val en: String,
    val de: String?,
    val cn: String?,
    val zh: String?
) {
    fun getAudioUrlByLanguage(language: String): String {
        if (language == "de" && de != null) return de
        if (language == "cn" && cn != null) return cn
        if (language == "zh" && zh != null) return zh
        return en
    }
}