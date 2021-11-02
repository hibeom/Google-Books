package com.pinkcloud.googlebooks.repository

import android.content.Context
import com.pinkcloud.googlebooks.R
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.network.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val bookDao: BookDao
) : BaseApiResponse() {

    val books = bookDao.getBooks()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshBooks() = withContext(Dispatchers.IO) {
        val networkResult = safeApiCall { remoteDataSource.getBooks() }
        if (networkResult is NetworkResult.Success) {
            val bookResponse = networkResult.data
            val books = bookResponse?.asDomainModel(context)
            books?.let {
                bookDao.insertAll(it)
            }
        }
        return@withContext networkResult
    }
}