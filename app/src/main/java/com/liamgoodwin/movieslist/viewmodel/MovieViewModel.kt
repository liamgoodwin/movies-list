package com.liamgoodwin.movieslist.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.model.repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class MovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository,
    @Assisted state: SavedStateHandle
    ) : ViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
    var movie = MutableLiveData<Movie>()

    val movies = currentQuery.switchMap { queryString ->
        repository.getMovies(queryString).cachedIn(viewModelScope)
    }

    fun searchMovies(query: String) {
        currentQuery.value = query
    }

    fun getMovie(imdbId: String) {
        viewModelScope.launch { movie.postValue(repository.getMovie(imdbId)) }
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "movie"
    }
}
