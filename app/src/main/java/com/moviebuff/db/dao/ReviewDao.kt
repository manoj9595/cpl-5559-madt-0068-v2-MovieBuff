package com.moviebuff.db.dao

import androidx.room.*
import com.moviebuff.data.model.Review
import com.moviebuff.data.model.User
import com.moviebuff.db.model.UserAndReview
import retrofit2.http.DELETE

@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE movieId = :movieId")
    fun getReviewsForMovie(movieId : Int)  :List<Review>

    @Insert
    fun addReview(review: Review)

    @Transaction
    @Query("SELECT * FROM reviews as r WHERE r.movieId = :movieId")
    fun getReviewsAndUsers(movieId : Int): List<UserAndReview>


    @Delete
    fun deleteReview(review: Review)


}