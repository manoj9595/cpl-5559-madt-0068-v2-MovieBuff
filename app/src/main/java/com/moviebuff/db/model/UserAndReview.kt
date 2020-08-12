package com.moviebuff.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.moviebuff.data.model.Review
import com.moviebuff.data.model.User

data class UserAndReview (
    @Embedded val review: Review,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
val user: User
)