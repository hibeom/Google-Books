package com.pinkcloud.googlebooks.network

import android.content.Context
import com.pinkcloud.googlebooks.R
import com.pinkcloud.googlebooks.database.Book
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class BooksResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<Item>
)

@JsonClass(generateAdapter = true)
data class Item(
    val id: String,
    val volumeInfo: VolumeInfo
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    val thumbnail: String?
)

private const val NO_TITLE = "NO TITLE"

suspend fun BooksResponse.asDomainModel() =
    withContext(Dispatchers.Default) {
        items.map { item ->
            Book(
                item.id,
                item.volumeInfo.title ?: NO_TITLE,
                item.volumeInfo.description ?: "",
                item.volumeInfo.authors ?: listOf(""),
                item.volumeInfo.imageLinks?.thumbnail ?: "",
                isFavorite = false
            )
        }
    }
