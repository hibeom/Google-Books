package com.pinkcloud.googlebooks.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insert(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<Favorite>>

    @Delete
    suspend fun delete(favorite: Favorite)
}