package com.pinkcloud.googlebooks.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") search: String,
        @Query("printType") printType: String,
        @Query("maxResults") limit: Int,
        @Query("startIndex") offset: Int
    ): Response<BooksResponse>
}