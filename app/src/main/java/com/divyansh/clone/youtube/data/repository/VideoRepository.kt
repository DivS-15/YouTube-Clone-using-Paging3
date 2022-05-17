package com.divyansh.clone.youtube.data.repository

import com.divyansh.clone.youtube.data.paging.VideosPagingSource
import com.divyansh.clone.youtube.data.remote.api.VideosRemoteInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val apiService: VideosRemoteInterface
) {
    fun videosPagingSource() = VideosPagingSource(coroutineScope, apiService)

    suspend fun loadChannelThumbnail(channelId: String): String {
        return apiService.getChannelDetails(channelId).items[0].snippet.thumbnails.default.url
    }
}