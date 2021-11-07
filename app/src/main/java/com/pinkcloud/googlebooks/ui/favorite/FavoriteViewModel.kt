package com.pinkcloud.googlebooks.ui.favorite

import androidx.lifecycle.*
import com.pinkcloud.googlebooks.database.*
import com.pinkcloud.googlebooks.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val bookDao: BookDao,
    private val repository: BookRepository
) : ViewModel() {

    val favoriteBooks = repository.favorites
        .asLiveData()

    fun changeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteDao.delete(favorite)
            val book = favorite.asBook(false)
            bookDao.update(book)
        }
    }
}