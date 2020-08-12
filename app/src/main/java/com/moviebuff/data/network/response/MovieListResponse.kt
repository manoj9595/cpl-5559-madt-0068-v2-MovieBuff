package com.moviebuff.data.network.response

import com.moviebuff.data.model.Movie

data class MovieListResponse (
    val results : ArrayList<Movie>?
)