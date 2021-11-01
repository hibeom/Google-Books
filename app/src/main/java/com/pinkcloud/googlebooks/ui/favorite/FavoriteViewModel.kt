package com.pinkcloud.googlebooks.ui.favorite

import androidx.lifecycle.*
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val repository: BookRepository
) : ViewModel() {

    val favoriteBooks = repository.books
        .map { books ->
            books.filter { book ->
                book.isFavorite
            }
        }.asLiveData()

    fun changeFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        viewModelScope.launch {
            bookDao.update(book)
        }
    }
}