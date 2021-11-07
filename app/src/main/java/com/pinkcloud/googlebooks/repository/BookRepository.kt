package com.pinkcloud.googlebooks.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pinkcloud.googlebooks.R
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.database.FavoriteDao
import com.pinkcloud.googlebooks.database.RemoteKeyDao
import com.pinkcloud.googlebooks.network.*
import com.pinkcloud.googlebooks.network.RemoteDataSource.Companion.NETWORK_PAGE_SIZE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val bookDao: BookDao,
    private val favoriteDao: FavoriteDao,
    private val remoteKeyDao: RemoteKeyDao
) : BaseApiResponse() {

    val books = bookDao.getBooks()
        .flowOn(Dispatchers.IO)
        .conflate()

    val favorites = favoriteDao.getFavorites()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshBooks() = withContext(Dispatchers.IO) {
        val networkResult = safeApiCall { remoteDataSource.getBooks() }
        if (networkResult is NetworkResult.Success) {
            val bookResponse = networkResult.data
            val books = bookResponse?.asDomainModel()
            books?.let {
                bookDao.insertAll(it)
            }
        }
        return@withContext networkResult
    }

    @ExperimentalPagingApi
    fun getBookCacheStream(): Flow<PagingData<Book>> {
        val pagingSourceFactory = { bookDao.pagingSource() }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = BookRemoteMediator(
                bookDao,
                remoteKeyDao,
                remoteDataSource
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}