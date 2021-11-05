package com.pinkcloud.googlebooks.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") search: String = "android",
        @Query("printType") printType: String = "books"
    ): BooksResponse
}