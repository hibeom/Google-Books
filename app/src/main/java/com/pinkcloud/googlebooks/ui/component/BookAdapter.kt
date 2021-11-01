package com.pinkcloud.googlebooks.ui.component

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.databinding.ListItemBookBinding
import com.pinkcloud.googlebooks.util.setFavoriteTint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BookAdapter @Inject constructor(): ListAdapter<Book, BookAdapter.ViewHolder>(BookDiffCallback()) {

    lateinit var onStarClicked: (Book) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onStarClicked)
    }

    class ViewHolder(private val binding: ListItemBookBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book, callback: (Book) -> Unit) {
            binding.book = book
            binding.imageFavorite.setOnClickListener {
                callback(book)
                setFavoriteTint(it as ImageView, book.isFavorite) // To update star view immediately
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBookBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class BookDiffCallback: DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}