package com.liamgoodwin.movieslist.model.repository

import com.liamgoodwin.movieslist.model.Movie
import com.liamgoodwin.movieslist.model.SearchResults
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/")
    suspend fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int
    ): SearchResults

    @GET("/")
    suspend fun getMovie(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String
    ): Movie
}