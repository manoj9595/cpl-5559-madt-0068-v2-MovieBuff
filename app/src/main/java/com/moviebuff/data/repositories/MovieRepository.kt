package com.moviebuff.data.repositories

import com.moviebuff.data.network.MyApi
import com.moviebuff.data.network.SafeApiRequest
import com.moviebuff.data.network.response.CastListResponse
import com.moviebuff.data.network.response.MovieListResponse
import com.moviebuff.data.network.response.VideoListResponse

class MovieRepository(
    private val api : MyApi
    ) : SafeApiRequest(){


    //API Services
    suspend fun getTopRatedMovies() : MovieListResponse {
        return apiRequset { api.getTopRatedMovies() }
    }
    suspend fun getUpComingMovies() : MovieListResponse {
        return apiRequset { api.getUpComingMovies() }
    }
    suspend fun getVideo(id : Int) : VideoListResponse {
        return apiRequset { api.getVideo(id) }
    }
    suspend fun getCastList(id : Int) : CastListResponse {
        return apiRequset { api.getCastList(id) }
    }
}