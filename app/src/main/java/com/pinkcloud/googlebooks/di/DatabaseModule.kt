package com.pinkcloud.googlebooks.di

import android.content.Context
import androidx.room.Room
import com.pinkcloud.googlebooks.database.BookDatabase
import com.pinkcloud.googlebooks.database.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase) = database.bookDao

    @Provides
    @Singleton
    fun provideFavoriteDao(database: BookDatabase) = database.favoriteDao

    @Provides
    @Singleton
    fun provideRemoteKeyDao(database: BookDatabase) = database.remoteKeyDao

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase {
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "book_database"
        ).build()
    }
}