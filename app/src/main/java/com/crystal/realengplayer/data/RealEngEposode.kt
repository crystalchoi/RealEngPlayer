package com.crystal.realengplayer.data



object RealEngEpisodeManager {
    private const val urlHeader = "https://v1.wisdomhouse.co.kr/mp3/realenglish/"   // + realenglish001.mp3

    fun getUri(episodeNum: Int) : String = urlHeader.plus("realenglish")
        .plus(String.format("%03d", episodeNum))
        .plus(".mp3")
}

const val TEST_MP4_URI_STRING = "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4"
//    "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
val TEST_MP3_URI_STRING = RealEngEpisodeManager.getUri(1)