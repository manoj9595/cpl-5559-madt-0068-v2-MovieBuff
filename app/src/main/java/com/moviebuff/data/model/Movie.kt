package com.moviebuff.data.model

import java.io.Serializable

class Movie (
    var genre_ids: List<Int>,
    var id: Int,
    var original_title: String,
    var overview: String,
    var poster_path: String,
    var release_date: String,
    var title: String
) : Serializable{
    fun getImageUrl():String{
        return "https://image.tmdb.org/t/p/w500$poster_path"
    }
}

