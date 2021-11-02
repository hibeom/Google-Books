package com.pinkcloud.googlebooks.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") search: String = "android",
        @Query("printType") printType: String = "books",
        @Query("maxResults") limit: Int = 30,
        @Query("startIndex") offset: Int = 0
    ): Response<BooksResponse>
}