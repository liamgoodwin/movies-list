package com.liamgoodwin.movieslist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.model.helpers.ApiPagingSource
import com.liamgoodwin.movieslist.model.repository.Api
import com.liamgoodwin.movieslist.model.repository.MovieRepository
import com.liamgoodwin.movieslist.util.Constants
import com.liamgoodwin.movieslist.utils.getOrAwaitValueTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieRepository: MovieRepository
    private val state: SavedStateHandle = SavedStateHandle()

    @Mock
    lateinit var api: Api

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var movie = Movie("Movie Title", "2000", "5.0", "2001", "60", "Comedy", "ABC Director", "ABC Writer", "ABC Actor", "Plot.", "English", "Canada", "Emmy", "www.poster.com", null, "1", "5.0", "100", "12345", "movie", "2002", "$1", "Produced", "www.move.com", "true")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        movieRepository = MovieRepository(api)
        movieViewModel = MovieViewModel(movieRepository, state)
    }

    @Test
    fun getMovieTest() {
        runBlocking {
            Mockito.`when`(movieRepository.getMovie("12345"))
                .thenReturn(movie)
            movieViewModel.getMovie("12345")
            val result = movieViewModel.movie.getOrAwaitValueTest()
            assertEquals(movie, result)
        }
    }

    @Test
    @Ignore
    fun getMoviesTest() {
        var movies : LiveData<PagingData<Movie>> = Pager(
            config = PagingConfig(
                pageSize = Constants.PER_PAGE,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ApiPagingSource(api, "movie") }
        ).liveData

        runBlocking {
            Mockito.`when`(movieRepository.getMovies("movie"))
                .thenReturn(movies)
            movieViewModel.searchMovies("movie")
            val result = movieViewModel.movies.getOrAwaitValueTest()
            assertEquals(listOf<Movie>(movie), result)
        }
    }
}