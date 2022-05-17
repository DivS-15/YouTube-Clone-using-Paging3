package com.divyansh.clone.youtube.data.model.video

data class PopularVideos(
    val items: List<Item>,
    val nextPageToken: String?,
    val prevPageToken: String?,
    val pageInfo: PageInfo
)