package com.pinkcloud.googlebooks.network

import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val bookService: BookService
) {
    suspend fun getBooks(
        search: String = "android",
        printType: String = "books",
        limit: Int = 30,
        offset: Int = 0
    ): Response<BooksResponse> {
        val permittedLimit = if (limit > 40) {
            Timber.e("maxResults for book request must be 40 or less. Request with 40 maxResults")
            40
        } else limit
        return bookService.getBooks(search, printType, permittedLimit, offset)
    }
}