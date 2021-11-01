package com.pinkcloud.googlebooks.util

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pinkcloud.googlebooks.R
import com.pinkcloud.googlebooks.database.Book

@BindingAdapter("books", "isNetworkError")
fun setLoadingVisibility(view: View, books: List<Book>?, isNetworkError: Boolean) {
    view.visibility = if (books != null) View.GONE else View.VISIBLE
    if (isNetworkError) view.visibility = View.GONE
}

@BindingAdapter("textList")
fun setTextList(textView: TextView, strings: List<String>) {
    textView.text = strings.joinToString()
}

@BindingAdapter("isFavorite")
fun setFavoriteTint(imageView: ImageView, isFavorite: Boolean) {
    imageView.imageTintList = when (isFavorite) {
        true -> ColorStateList.valueOf(imageView.context.getColor(R.color.yellow))
        false -> ColorStateList.valueOf(imageView.context.getColor(R.color.gray))
    }
}