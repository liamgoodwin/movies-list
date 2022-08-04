package com.liamgoodwin.movieslist.view.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.liamgoodwin.movieslist.R
import com.liamgoodwin.movieslist.databinding.ListItemMovieBinding
import com.liamgoodwin.movieslist.model.Movie
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_movie.view.*
import java.lang.Exception
import java.util.*

class MoviesAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Set on click listener and pass the current item
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        // Bind the movie information to the view
        fun bind(movie: Movie) {
            binding.apply {
                Picasso.get().load(movie.poster).into(moviePoster, object : Callback {
                    override fun onSuccess() {
                        moviePosterLoadingIcon.visibility = View.GONE
                        noImageView.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        moviePosterLoadingIcon.visibility = View.GONE
                        noImageView.visibility = View.VISIBLE
                    }
                })

                movieName.text = movie.title
                movieType.text = movie.type.capitalize()
                movieYear.text = movie.year
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.imdbId == newItem.imdbId

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }
}