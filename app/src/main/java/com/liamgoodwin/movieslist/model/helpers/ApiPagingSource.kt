package com.liamgoodwin.movieslist.model.helpers

import androidx.paging.PagingSource
import com.liamgoodwin.movieslist.util.Constants
import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.model.repository.Api
import retrofit2.HttpException
import java.io.IOException

private const val API_STARTING_PAGE_INDEX = 1

class ApiPagingSource(
    private val api: Api,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>) : LoadResult<Int, Movie> {
        val position = params.key ?: API_STARTING_PAGE_INDEX

        return try {
            val response = api.getMovies(Constants.OMDB_API_KEY, query, position)
            val movies = response.movies

            LoadResult.Page(
                data = movies?: listOf(),
                prevKey = if (position == API_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (movies?.isEmpty() == true) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}