package com.pinkcloud.googlebooks.database

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<Book>)

    @Query("SELECT * FROM books")
    fun getBooks(): Flow<List<Book>>

    @Update
    suspend fun update(book: Book)

    @Query("SELECT * FROM books")
    fun pagingSource(): PagingSource<Int, Book>

    @Query("DELETE FROM books")
    suspend fun clearAll()
}