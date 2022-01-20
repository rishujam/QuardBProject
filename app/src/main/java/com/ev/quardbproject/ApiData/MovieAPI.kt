package com.ev.quardbproject.ApiData

import com.ev.quardbproject.datamodels.MovieItem
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("search/shows?q=all")
    suspend fun getAllMovies():List<MovieItem>

    @GET("search/shows")
    suspend fun getMovie(
        @Query("q") term:String
    ):List<MovieItem>
}