package com.liamgoodwin.movieslist.view.movie

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.liamgoodwin.movieslist.R
import com.liamgoodwin.movieslist.databinding.FragmentMovieBinding
import com.liamgoodwin.movieslist.viewmodel.MovieViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie) {

    private val movieViewModel by viewModels<MovieViewModel>()
    private val args by navArgs<MovieFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMovieBinding.bind(view)

        // Get the movie we want to display
        binding.apply {
            val movieArgs = args.movie
            movieViewModel.getMovie(movieArgs.imdbId)
        }

        // When movie object is set, display all of the views
        movieViewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.apply {
                Picasso.get().load(movie.poster).into(moviePoster, object : Callback {
                    override fun onSuccess() {}

                    override fun onError(e: Exception?) {
                        noImageViewMovie.visibility = View.VISIBLE
                    }
                })

                setTextView(movie.title, movieNameLabel, movieNameText)
                setTextView(movie.released, movieYearLabel, movieYearText)
                setTextView(movie.rated, movieRatingLabel, movieRatingText)
                setTextView(movie.runtime, movieRuntimeLabel, movieRuntimeText)
                setTextView(movie.genre, movieGenreLabel, movieGenreText)
                setTextView(movie.director, movieDirectorLabel, movieDirectorText)
                setTextView(movie.writer, movieWritersLabel, movieWritersText)
                setTextView(movie.actors, movieActorsLabel, movieActorsText)
                setTextView(movie.plot, moviePlotLabel, moviePlotText)
                setTextView(movie.language, movieLanguageLabel, movieLanguageText)
                setTextView(movie.country, movieCountryLabel, movieCountryText)
                setTextView(movie.awards, movieAwardsLabel, movieAwardsText)
                setTextView(movie.metaScore, movieMetascoreLabel, movieMetascoreText)
                setTextView(movie.imdbRating, movieImdbRatingLabel, movieImdbRatingText)
                setTextView(movie.imdbVotes, movieImdbRatingLabel, movieImdbVotesText)
                setTextView(movie.dvd, movieDvdReleaseDateLabel, movieDvdReleaseDateText)
                setTextView(movie.boxOffice, movieBoxOfficeLabel, movieBoxOfficeText)
                setTextView(movie.production, movieProductionLabel, movieProductionText)
                setTextView(movie.website, movieWebsiteLabel, movieWebsiteText)
                movieTypeText.text = movie.type.capitalize(Locale.ROOT)

                // Divide the rating in half so we can just show a /5 rating
                movie.imdbRating?.let {
                    if (it.isNotEmpty() && it != "N/A") {
                        val rating = (it.toFloat() / 2)
                        ratingBar.rating = rating
                        movieImdbRatingText.text = rating.toString()
                    } else {
                        movieImdbRatingLabel.visibility = View.GONE
                        movieImdbRatingText.visibility = View.GONE
                        ratingBar.rating = 0.0F
                    }
                }
            }
        }
    }

    private fun setTextView(value: String?, label: TextView, textView: TextView) {
        // If the passed value is not empty or N/A, set the text, otherwise hide the label and textview
        value?.let {
            if (it.isNotEmpty() && it != "N/A") {
                textView.text = it
            } else {
                label.visibility = View.GONE
                textView.visibility = View.GONE
            }
        }
    }
}