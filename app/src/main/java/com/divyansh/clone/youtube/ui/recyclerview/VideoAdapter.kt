package com.divyansh.clone.youtube.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.divyansh.clone.youtube.databinding.VideoListItemBinding
import com.divyansh.clone.youtube.ui.VideoWithChannelModel
import com.divyansh.clone.youtube.utilities.getDateTimeDifference

class VideoAdapter :
    PagingDataAdapter<VideoWithChannelModel, VideoAdapter.VideoViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<VideoWithChannelModel>() {
        override fun areItemsTheSame(
            oldItem: VideoWithChannelModel,
            newItem: VideoWithChannelModel
        ): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(
            oldItem: VideoWithChannelModel,
            newItem: VideoWithChannelModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class VideoViewHolder(
        private val binding: VideoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(video: VideoWithChannelModel) {
            binding.apply {
                        val imgUrl = video.channelUrl.toUri().buildUpon().scheme("https").build()
                        channelThumbnailImage.load(imgUrl) {
                            transformations(CircleCropTransformation())
                        }


                        video.item.apply {
                            videoTitle.text = this.snippet.title
                            channelTitle.text = snippet.channelTitle
                            viewCount.text = statistics.viewCount

                            val imgUrl = snippet.thumbnails?.high?.url

                            val dateTime = getDateTimeDifference(snippet.publishedAt.toString())

                            if (dateTime.days.toInt() != 0) {
                                timePublished.text = "${dateTime.days.toInt()} days ago"
                            } else if (dateTime.days.toInt() == 0) {
                                timePublished.text = "${dateTime.hours.toInt()} hours ago"
                            }
                            if (dateTime.hours.toInt() == 0) {
                                timePublished.text = "${dateTime.minutes.toInt()} minutes ago"
                            }


                            val imgUri = imgUrl?.toUri()?.buildUpon()?.scheme("https")?.build()

                            videoThumbnailImage.load(imgUri)

                        }
            }
        }
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }
}
