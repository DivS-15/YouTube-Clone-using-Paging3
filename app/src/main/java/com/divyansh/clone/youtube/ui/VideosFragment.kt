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
import com.divyansh.clone.youtube.R
import com.divyansh.clone.youtube.databinding.FragmentPopularVideosListBinding
import com.divyansh.clone.youtube.ui.pagingloadstates.VideoLoadStateAdapter
import com.divyansh.clone.youtube.ui.recyclerview.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosFragment : Fragment(R.layout.fragment_popular_videos_list) {
    private lateinit var _binding: FragmentPopularVideosListBinding
    private val binding get() = _binding


    private val viewModel: VideoViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPopularVideosListBinding.bind(view)

        val adapter = VideoAdapter()
        binding.apply {
            videosList.adapter = adapter.withLoadStateFooter(
                footer = VideoLoadStateAdapter{adapter.retry()}
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.source.append is LoadState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    }
}
