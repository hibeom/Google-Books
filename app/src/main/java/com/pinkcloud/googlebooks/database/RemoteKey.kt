package com.pinkcloud.googlebooks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    val bookId: String,
    val nextKey: Int?
)
