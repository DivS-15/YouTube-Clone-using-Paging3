package com.divyansh.clone.youtube.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.divyansh.clone.youtube.data.model.video.Item
import com.divyansh.clone.youtube.data.paging.VideosPagingSource
import com.divyansh.clone.youtube.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {
    private lateinit var channelUrl: String

    val items: Flow<PagingData<Item>> = Pager(
        PagingConfig(10, enablePlaceholders = false),
        pagingSourceFactory = { repository.videosPagingSource() }
    ).flow.cachedIn(viewModelScope)

    suspend fun onChannelImgRequired(channelId: String?): String {
        viewModelScope.launch {
            channelId?.let {
                channelUrl = repository.loadChannelThumbnail(channelId)
            }

        }
        return channelUrl
    }

}