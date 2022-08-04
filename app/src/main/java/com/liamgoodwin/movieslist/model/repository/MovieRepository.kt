package com.liamgoodwin.movieslist.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.liamgoodwin.movieslist.util.Constants
import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.model.helpers.ApiPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MovieRepository @Inject constructor(private val api: Api) {

    /**
     * Get a list of movies based on the user's input
     *
     * @param query the users search term
     */
    fun getMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = Constants.PER_PAGE,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ApiPagingSource(api, query) }
        ).liveData

    /**
     * Get the movie based on the passed id
     *
     * @param imdbId the movie id
     */
    suspend fun getMovie(imdbId: String): Movie {
        return api.getMovie(Constants.OMDB_API_KEY, imdbId)
    }
}