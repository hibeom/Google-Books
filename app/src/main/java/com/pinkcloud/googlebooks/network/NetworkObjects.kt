package com.pinkcloud.googlebooks.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<Item>
)

@JsonClass(generateAdapter = true)
data class Item(
    val volumeInfo: VolumeInfo
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val description: String,
    val imageLinks: ImageLinks
)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    val thumbnail: String
)