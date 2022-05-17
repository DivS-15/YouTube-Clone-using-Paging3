package com.divyansh.clone.youtube.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.divyansh.clone.youtube.data.model.video.Item
import com.divyansh.clone.youtube.data.remote.api.VideosRemoteInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

private const val STARTING_PAGE_TOKEN = " "

class VideosPagingSource @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val remoteApiService: VideosRemoteInterface
) : PagingSource<String, Item>() {

    override fun getRefreshKey(state: PagingState<String, Item>): String? {
        var current: String? = " "

        val anchorPosition = state.anchorPosition


        coroutineScope.launch {
            if (anchorPosition != null) {
                 current = state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                    remoteApiService.getMostPopularVideos(
                        it
                    ).nextPageToken
                }
            }
        }
        return current
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Item> {
        val start = params.key ?: STARTING_PAGE_TOKEN

        return try {
            val response = remoteApiService.getMostPopularVideos(start)

            val nextKey = if (response.items.isEmpty()) null else response.nextPageToken
            val prevKey = if (start == STARTING_PAGE_TOKEN) null else response.prevPageToken

            LoadResult.Page(
                data = response.items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (exception: HttpException){
            return LoadResult.Error(exception)
        }

    }
}