package com.pinkcloud.googlebooks.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    private val adapter: JsonAdapter<List<String>>

    init {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        adapter = moshi.adapter(type)
    }

    @TypeConverter
    fun listToJson(list: List<String>) = adapter.toJson(list)

    @TypeConverter
    fun listFromJson(json: String) = adapter.fromJson(json)
}