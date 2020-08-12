package com.moviebuff.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
class User (
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,

    val firstName : String,
    val lastName : String,
    val contact : String,
    val emailId : String,
    val dob : String,
    val profilePic : String?,
    val pass : String? = null
):Serializable