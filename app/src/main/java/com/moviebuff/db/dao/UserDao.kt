package com.moviebuff.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.moviebuff.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE emailId = :emailId")
    fun getUserByEmailId(emailId : String)  :User

    @Insert
    fun addUser(user: User)

    @Query("UPDATE users SET firstName = :firstName ,lastName= :lastName,contact= :contact,emailId= :emailId,dob= :selectedDate,profilePic= :profilePic WHERE id LIKE :userId ")
    fun updateUser(firstName : String,
                   lastName : String,
                   contact : String,
                   emailId : String,
                   selectedDate: String,
    profilePic : String,userId : Int): Int

    @Query("SELECT * FROM users WHERE emailId = :emailId AND pass= :pass")
    fun authenticateUser(emailId: String,pass : String)  :User
}