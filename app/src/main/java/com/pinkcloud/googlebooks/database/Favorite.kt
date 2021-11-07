package com.pinkcloud.googlebooks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "favorites")
@TypeConverters(Converters::class)
data class Favorite(
    @PrimaryKey
    val id: String,
    var title: String,
    var description: String,
    var authors: List<String>,
    var thumbnail: String
)

fun Favorite.asBook(isFavorite: Boolean): Book =
    Book(id, title, description, authors, thumbnail, isFavorite)