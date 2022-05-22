package com.divyansh.clone.youtube.ui.pagingloadstates

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class VideoLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<VideoLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: VideoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): VideoLoadStateViewHolder {
        return VideoLoadStateViewHolder.create(parent, retry)
    }
}