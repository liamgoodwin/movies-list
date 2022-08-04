package com.liamgoodwin.movieslist.view.movies

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.liamgoodwin.movieslist.R
import com.liamgoodwin.movieslist.databinding.FragmentMoviesBinding
import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.view.extensions.hideKeyboard
import com.liamgoodwin.movieslist.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MoviesAdapter.OnItemClickListener {

    private val movieViewModel by viewModels<MovieViewModel>()

    private var _binding : FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MoviesAdapter(this)
        _binding = FragmentMoviesBinding.bind(view)

        // Setup the recyclerview
        binding.apply {
            moviesRecyclerView.setHasFixedSize(true)
            moviesRecyclerView.itemAnimator = null
            moviesRecyclerView.adapter = adapter
            moviesRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    moviesRecyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        // Observe the movies data, and pass it to our adapter
        movieViewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setupSearch()
        setupSearchButton()

        // Show loading icon, empty state, and recycler view based on the current state of our adapter
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                moviesRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading

                // Check for empty results
                if (loadState.source.refresh is LoadState.NotLoading &&
                    adapter.itemCount < 1) {
                    moviesRecyclerView.isVisible = false
                    emptyState.isVisible = true
                } else {
                    emptyState.isVisible = false
                }
            }
        }
    }

    // Pass the selected movie to our Movie Fragment to display
    override fun onItemClick(movie: Movie) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieFragment2(movie)
        findNavController().navigate(action)
    }

    private fun setupSearch() {
        // Setup the textedit listener to search when we press the enter key
        binding.apply {
            mediaName.setSelection(mediaName.text.length)
            mediaName.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchButton.performClick()
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    private fun setupSearchButton() {
        // When the user clicks the search button, attempt to fetch the data via the viewModel
        binding.apply {
            searchButton.setOnClickListener {
                val mediaQuery = mediaName.text.toString()

                // Perform a search, or error if not enough characters
                if (mediaQuery.isNotEmpty() && mediaQuery.length >= 2) {
                    moviesRecyclerView.scrollToPosition(0)
                    movieViewModel.searchMovies(mediaQuery)
                    moviesRecyclerView.hideKeyboard()
                } else {
                    Toast.makeText(
                        context,
                        "Please enter at least 2 characters before searching.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}