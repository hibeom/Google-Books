package com.pinkcloud.googlebooks.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<Book>)

    @Query("SELECT * FROM books")
    fun getBooks(): Flow<List<Book>>

    @Update
    suspend fun update(book: Book)
}