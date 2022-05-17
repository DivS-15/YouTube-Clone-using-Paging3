package com.divyansh.clone.youtube.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.divyansh.clone.youtube.data.model.video.Item
import com.divyansh.clone.youtube.databinding.VideoListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoAdapter @Inject constructor(
    private val coroutineScope: CoroutineScope
) : PagingDataAdapter<Item, VideoAdapter.VideoViewHolder>(DiffCallback) {

    private val displayBannerChannel = Channel<DisplayChannelImage>()
    val displayChannelFlow = displayBannerChannel.receiveAsFlow()

    companion object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    inner class VideoViewHolder(
        private val binding: VideoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if(absoluteAdapterPosition != -1){
                val currentVideoItem = getItem(absoluteAdapterPosition)
                coroutineScope.launch {
                    currentVideoItem?.let {
                        displayBannerChannel.send(
                            DisplayChannelImage.ChannelThumbnailImage(
                                currentVideoItem
                            )
                        )
                    }
                }
                coroutineScope.launch {

                }
            }
        }

        fun bind(video: Item) {
            binding.apply {
                videoTitle.text = video.snippet.title
                channelTitle.text = video.snippet.channelTitle
                viewCount.text = video.statistics.viewCount

                val imgUrl = video.snippet.thumbnails?.high?.url

                val imgUri = imgUrl?.let {
                    it.toUri().buildUpon().scheme("https").build()
                }



                videoThumbnailImage.load(imgUri)
            }
        }
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }
}

sealed class DisplayChannelImage {
    data class ChannelThumbnailImage(val video: Item) : DisplayChannelImage()
}