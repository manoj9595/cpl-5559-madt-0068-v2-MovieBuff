package com.moviebuff.data.network.response

import com.moviebuff.data.model.Movie
import com.moviebuff.data.model.Video

data class VideoListResponse (
    val results : ArrayList<Video>?
)