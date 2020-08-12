package com.moviebuff.data.network

import com.moviebuff.BuildConfig
import com.moviebuff.data.network.response.CastListResponse
import com.moviebuff.data.network.response.MovieListResponse
import com.moviebuff.data.network.response.VideoListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "991ff9ac5d1a2fdcc5085726f61181be" //TODO ADD themoviewdb Key -> https://www.themoviedb.org/documentation/api
interface MyApi {



    @GET("movie/top_rated?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getTopRatedMovies() : Response<MovieListResponse>

    @GET("movie/upcoming?api_key=$API_KEY&language=en-US&page=1")
    suspend fun getUpComingMovies() : Response<MovieListResponse>



    @GET("movie/{id}/videos?api_key=$API_KEY&language=en-US")
    suspend fun getVideo(
        @Path("id") id : Int
    ) : Response<VideoListResponse>

    @GET("movie/{id}/credits?api_key=$API_KEY&language=en-US")
    suspend fun getCastList(
        @Path("id") id : Int
    ) : Response<CastListResponse>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ):MyApi{
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .readTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(25,TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClient.addInterceptor(logging)
            }


            return Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}