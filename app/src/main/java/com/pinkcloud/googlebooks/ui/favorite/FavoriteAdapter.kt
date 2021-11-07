package com.pinkcloud.googlebooks.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.googlebooks.database.Book
import com.pinkcloud.googlebooks.database.Favorite
import com.pinkcloud.googlebooks.database.asBook
import com.pinkcloud.googlebooks.databinding.ListItemBookBinding
import com.pinkcloud.googlebooks.util.setFavoriteTint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FavoriteAdapter @Inject constructor(): ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(FavoriteDiffCallback()) {

    lateinit var onStarClicked: (Favorite) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onStarClicked)
    }

    class ViewHolder(private val binding: ListItemBookBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: Favorite, callback: (Favorite) -> Unit) {
            binding.book = favorite.asBook(true)
            binding.imageFavorite.setOnClickListener {
                callback(favorite)
                setFavoriteTint(it as ImageView, false) // To update star view immediately
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

class FavoriteDiffCallback: DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}