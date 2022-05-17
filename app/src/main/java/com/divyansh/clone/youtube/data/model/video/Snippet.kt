package com.divyansh.clone.youtube.data.model.video

data class Snippet(
    val channelId: String?,
    val channelTitle: String?,
    val publishedAt: String?,
    val thumbnails: Thumbnails?,
    val title: String?
)