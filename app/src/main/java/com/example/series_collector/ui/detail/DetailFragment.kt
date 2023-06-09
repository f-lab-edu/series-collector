package com.example.series_collector.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.series_collector.databinding.FragmentDetailSeriesBinding
import com.example.series_collector.ui.adapters.SeriesVideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val adapter = SeriesVideoAdapter()
    lateinit private var binding: FragmentDetailSeriesBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailSeriesBinding.inflate(inflater, container, false)

        binding.apply {
            viewModel = detailViewModel
            lifecycleOwner = viewLifecycleOwner
            rvSeriesVideos.adapter = adapter

            callback = Callback { isFollowed ->
                if (isFollowed != null) {
                    detailViewModel.toggleSeriesFollowed(isFollowed)
                }
            }

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            seriesDetailPlayBtn.setOnClickListener {
                createYoutubeIntent(args.seriesId)
            }

        }

        searchSeriesVideoList(args.seriesId)

        return binding.root
    }

    private fun searchSeriesVideoList(seriesId: String) {
        lifecycleScope.launch {
            detailViewModel.searchSeriesVideoList(seriesId).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun createYoutubeIntent(playListId: String) {
        startActivity( Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://www.youtube.com/watch?list=$playListId"))
            .setPackage("com.google.android.youtube"));
    }


    fun interface Callback {
        fun toggle(isFollowed: Boolean?)
    }



}

