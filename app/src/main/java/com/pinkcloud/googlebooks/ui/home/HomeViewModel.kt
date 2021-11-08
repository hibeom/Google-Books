package com.pinkcloud.googlebooks.ui.home

import androidx.lifecycle.*
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.network.BooksResponse
import com.pinkcloud.googlebooks.network.NetworkResult
import com.pinkcloud.googlebooks.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookDao: BookDao,
    private val repository: BookRepository
) : ViewModel() {

    // distinctUntilChanged() address blink issue when submitting books to adapter by clicking star.
    val books = repository.books
        .distinctUntilChanged()
        .asLiveData()

    private val _networkEvent =
        MutableLiveData<NetworkResult<BooksResponse>>(NetworkResult.Loading())
    val networkEvent: LiveData<NetworkResult<BooksResponse>>
        get() = _networkEvent

    init {
        refreshBooks()
    }

    fun changeFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        viewModelScope.launch {
            bookDao.update(book)
        }
    }

    fun refreshBooks() {
        viewModelScope.launch {
            _networkEvent.value = NetworkResult.Loading()
            val networkResult = repository.refreshBooks()
            _networkEvent.value = networkResult
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("oncleared")
    }
}