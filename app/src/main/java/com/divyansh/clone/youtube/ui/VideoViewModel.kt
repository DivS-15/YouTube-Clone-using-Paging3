package com.divyansh.clone.youtube.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.divyansh.clone.youtube.data.model.video.Item
import com.divyansh.clone.youtube.data.paging.VideosPagingSource
import com.divyansh.clone.youtube.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    val items: Flow<PagingData<VideoWithChannelModel>> = Pager(
        PagingConfig(10, enablePlaceholders = false),
        pagingSourceFactory = { repository.videosPagingSource() }
    ).flow
        .map { value: PagingData<Item> -> //map function from kotlinx.coroutines.flow
            value.map {
                val channelUrl = onChannelImgRequired(it.snippet.channelId.toString())
                VideoWithChannelModel(it, channelUrl)
            }
        }
        .cachedIn(viewModelScope)


    private suspend fun onChannelImgRequired(channelId: String): String {
        return repository.loadChannelThumbnail(channelId)
    }

}

data class VideoWithChannelModel(
    val item: Item,
    val channelUrl: String
)