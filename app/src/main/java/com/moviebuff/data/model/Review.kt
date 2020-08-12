package com.moviebuff.data.model

import androidx.room.*

@Entity(tableName = "reviews")
data class Review (
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val userId  :Int,
    val review  :String,
    val movieId  :Int
)