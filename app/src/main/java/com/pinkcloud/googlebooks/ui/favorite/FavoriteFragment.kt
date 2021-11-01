package com.pinkcloud.googlebooks.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pinkcloud.googlebooks.databinding.FragmentFavoriteBinding
import com.pinkcloud.googlebooks.ui.component.BookAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null

    @Inject
    lateinit var adapter: BookAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter
        adapter.onStarClicked = { book ->
            favoriteViewModel.changeFavorite(book)
        }

        favoriteViewModel.favoriteBooks.observe(viewLifecycleOwner, { books ->
            adapter.submitList(books)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}