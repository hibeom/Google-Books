package com.pinkcloud.googlebooks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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

//TODO handle json parse exception
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