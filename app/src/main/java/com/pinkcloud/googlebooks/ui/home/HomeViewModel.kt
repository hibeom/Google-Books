package com.pinkcloud.googlebooks.ui.home

import androidx.lifecycle.*
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val repository: BookRepository) : ViewModel() {

    val books = repository.books.asLiveData()

    private val _isNetworkError = MutableLiveData(false)
    val isNetworkError: LiveData<Boolean>
        get() = _isNetworkError

    init {
        viewModelScope.launch {
            try {
                repository.refreshBooks()
                _isNetworkError.value = false
            } catch (networkException: IOException) {
                _isNetworkError.value = true
            }
        }
    }

    fun changeFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        viewModelScope.launch {
            bookDao.update(book)
        }
    }
}