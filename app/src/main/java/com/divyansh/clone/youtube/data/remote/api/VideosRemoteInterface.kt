package com.divyansh.clone.youtube.data.remote.api

import com.divyansh.clone.youtube.BuildConfig
import com.divyansh.clone.youtube.data.model.channel.ChannelDetails
import com.divyansh.clone.youtube.data.model.video.PopularVideos
import retrofit2.http.GET
import retrofit2.http.Query


interface VideosRemoteInterface {
    companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
        private const val KEY = BuildConfig.ACCESS_KEY
    }

    @GET("videos")
    suspend fun getMostPopularVideos(
        @Query("pageToken") pageToken: String,
        @Query("part") part: String = "snippet",
        @Query("part") statistics: String = "statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") country: String = "IN",
        @Query("key") key: String = KEY
    ): PopularVideos

    @GET("/channels")
    suspend fun getChannelDetails(
        @Query("id") channelId: String,
        @Query("part") part: String = "snippet",
        @Query("key") key: String = KEY
    ): ChannelDetails
}