package com.pinkcloud.googlebooks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "books")
@TypeConverters(Converters::class)
data class Book(
    @PrimaryKey
    val id: String,
    var title: String,
    var description: String,
    var authors: List<String>,
    var thumbnail: String,
    var isFavorite: Boolean
)

fun Book.asFavorite(): Favorite =
    Favorite(id, title, description, authors, thumbnail)