package com.divyansh.clone.youtube.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.divyansh.clone.youtube.R
import com.divyansh.clone.youtube.databinding.FragmentPopularVideosListBinding
import com.divyansh.clone.youtube.ui.recyclerview.DisplayChannelImage
import com.divyansh.clone.youtube.ui.recyclerview.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosFragment : Fragment(R.layout.fragment_popular_videos_list) {
    private lateinit var _binding: FragmentPopularVideosListBinding
    private val binding get() = _binding

    private val channelImageToAdapter = Channel<ChannelToAdapterClass>()
    val passToAdapterFlow = channelImageToAdapter.receiveAsFlow()

    private val viewModel: VideoViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPopularVideosListBinding.bind(view)

        val adapter = VideoAdapter(viewLifecycleOwner.lifecycleScope)
        binding.apply {
            videosList.adapter = adapter
        }

        adapter.addLoadStateListener {
            binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.displayChannelFlow.collectLatest {
                when (it) {
                    is DisplayChannelImage.ChannelThumbnailImage -> {
                        val channelImgUrl =
                            viewModel.onChannelImgRequired(it.video.snippet.channelId)
                        channelImageToAdapter.send(
                            ChannelToAdapterClass.PassDataToAdapter(
                                channelImgUrl
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class ChannelToAdapterClass {
    data class PassDataToAdapter(val channelIdToAdapter: String) : ChannelToAdapterClass()
}
