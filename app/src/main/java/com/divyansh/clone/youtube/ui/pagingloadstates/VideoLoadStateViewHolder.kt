package com.divyansh.clone.youtube.ui.pagingloadstates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.divyansh.clone.youtube.R
import com.divyansh.clone.youtube.databinding.LoadstateViewholderLayoutBinding

class VideoLoadStateViewHolder(
    private val binding: LoadstateViewholderLayoutBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState){
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.retryText.isVisible = loadState is LoadState.Error
    }

    companion object{
        fun create(parent: ViewGroup, retry: () -> Unit): VideoLoadStateViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loadstate_viewholder_layout, parent, false)
            val binding = LoadstateViewholderLayoutBinding.bind(view)
            return VideoLoadStateViewHolder(binding, retry)
        }
    }

}