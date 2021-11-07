package com.pinkcloud.googlebooks.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE book_id = :bookId")
    suspend fun remoteKeyById(bookId: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE book_id = :bookId")
    suspend fun deleteById(bookId: String)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}