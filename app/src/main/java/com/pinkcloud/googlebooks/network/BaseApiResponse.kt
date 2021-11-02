package com.pinkcloud.googlebooks.network

import retrofit2.Response
import java.lang.Exception

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(it)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (exception: Exception) {
            return error(exception.message ?: exception.toString())
        }
    }

    private fun <T> error(message: String): NetworkResult<T> {
        return NetworkResult.Error("Network Request Error: $message")
    }
}

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null): NetworkResult<T>(data, message)
    class Loading<T>: NetworkResult<T>()
}