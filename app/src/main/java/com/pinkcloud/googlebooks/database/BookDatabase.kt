package com.pinkcloud.googlebooks.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase: RoomDatabase() {

    abstract val bookDao: BookDao
}