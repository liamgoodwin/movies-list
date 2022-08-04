package com.liamgoodwin.movieslist.model

import com.google.gson.annotations.SerializedName

data class SearchResults (
    @SerializedName("Search") val movies: List<Movie>?,
    val totalResults: Int,
    @SerializedName("Response") val response: String,
)
