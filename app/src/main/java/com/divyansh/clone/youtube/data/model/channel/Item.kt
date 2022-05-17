package com.divyansh.clone.youtube.data.model.channel

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)