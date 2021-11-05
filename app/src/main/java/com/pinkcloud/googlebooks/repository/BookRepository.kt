package com.pinkcloud.googlebooks.repository

import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.network.BookService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookService: BookService,
    private val bookDao: BookDao
) {

    val books = bookDao.getBooks()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshBooks() {
        val bookResponse = bookService.getBooks()
        val books = bookResponse.items.map { item ->
            Book(
                item.id,
                item.volumeInfo.title,
                item.volumeInfo.description,
                item.volumeInfo.authors,
                item.volumeInfo.imageLinks.thumbnail
            )
        }
        bookDao.insertAll(books)
    }
}