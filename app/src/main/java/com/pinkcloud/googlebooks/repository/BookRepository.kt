package com.pinkcloud.googlebooks.repository

import android.content.Context
import com.pinkcloud.googlebooks.R
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.network.BookService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookService: BookService,
    private val bookDao: BookDao
) {

    // distinctUntilChanged() address blink issue when submitting books to adapter by clicking star.
    val books = bookDao.getBooks()
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshBooks() {
        // TODO pagination
        val bookResponse = bookService.getBooks()
        val books = bookResponse.items.map { item ->
            Book(
                item.id,
                item.volumeInfo.title ?: context.getString(R.string.no_title),
                item.volumeInfo.description ?: "",
                item.volumeInfo.authors ?: listOf(""),
                item.volumeInfo.imageLinks?.thumbnail ?: "",
                isFavorite = false
            )
        }
        bookDao.insertAll(books)
    }
}