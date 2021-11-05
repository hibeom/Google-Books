package com.pinkcloud.googlebooks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcloud.googlebooks.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private val _books = MutableLiveData<String>()
    val books: LiveData<String>
        get() = _books

    init {
        viewModelScope.launch {
            repository.refreshBooks()
        }
    }
}