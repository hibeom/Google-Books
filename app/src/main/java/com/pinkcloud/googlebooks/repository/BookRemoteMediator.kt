package com.pinkcloud.googlebooks.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.BookDao
import com.pinkcloud.googlebooks.database.RemoteKey
import com.pinkcloud.googlebooks.database.RemoteKeyDao
import com.pinkcloud.googlebooks.network.RemoteDataSource
import com.pinkcloud.googlebooks.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val STARTING_PAGE_INDEX = 0

@ExperimentalPagingApi
class BookRemoteMediator(
    private val bookDao: BookDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val remoteDataSource: RemoteDataSource
) : RemoteMediator<Int, Book>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Book>): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
                nextKey
            }
        }

        try {
            val response = remoteDataSource.getBooks(
                limit = state.config.pageSize,
                offset = loadKey * state.config.pageSize
            )
            if (!response.isSuccessful) {
                return MediatorResult.Error(Throwable("${response.code()} ${response.message()}"))
            }
            val body = response.body()!!
            val books = body.asDomainModel()
            val endOfPagingReached = books.isEmpty()
            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    bookDao.clearAll()
                }
                val nextKey = if (endOfPagingReached) null else loadKey + 1
                val keys = books.map {
                    RemoteKey(it.id, nextKey)
                }
                remoteKeyDao.insertAll(keys)
                bookDao.insertAll(books)
            }
            return MediatorResult.Success(endOfPagingReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Book>): RemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                // Get the remote keys of the last item retrieved
                remoteKeyDao.remoteKeyById(book.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Book>
    ): RemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { bookId ->
                remoteKeyDao.remoteKeyById(bookId)
            }
        }
    }
}