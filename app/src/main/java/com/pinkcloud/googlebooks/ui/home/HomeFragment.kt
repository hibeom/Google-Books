package com.pinkcloud.googlebooks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.snackbar.Snackbar
import com.pinkcloud.googlebooks.databinding.FragmentHomeBinding
import com.pinkcloud.googlebooks.network.NetworkResult
import com.pinkcloud.googlebooks.ui.component.BookAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    @Inject
    lateinit var adapter: BookAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = homeViewModel

        binding.recyclerView.adapter = adapter
        adapter.onStarClicked = { book ->
            homeViewModel.changeFavorite(book)
        }

        lifecycleScope.launch {
            homeViewModel.bookPagingFlow
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

        homeViewModel.networkEvent.observe(viewLifecycleOwner, { networkEvent ->
            if (networkEvent is NetworkResult.Error) {
                Snackbar.make(
                    binding.root,
                    networkEvent.message!!,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}