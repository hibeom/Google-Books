package com.pinkcloud.googlebooks.repository

import com.pinkcloud.googlebooks.network.BookService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(private val bookService: BookService) {

    suspend fun refreshBooks() {
        val bookResponse = bookService.getBooks()
        bookResponse.items.forEach { item ->
            Timber.d("${item.volumeInfo}")
        }
    }
}