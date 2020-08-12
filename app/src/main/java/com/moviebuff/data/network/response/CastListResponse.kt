package com.moviebuff.data.network.response

import com.moviebuff.data.model.Cast
import com.moviebuff.data.model.Movie

data class CastListResponse (
    val cast : ArrayList<Cast>?,
    val crew : ArrayList<Cast>?
)