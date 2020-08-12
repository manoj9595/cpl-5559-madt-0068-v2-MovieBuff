package com.moviebuff.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moviebuff.data.model.Review
import com.moviebuff.data.model.User
import com.moviebuff.db.dao.ReviewDao
import com.moviebuff.db.dao.UserDao

@Database(entities = [User::class, Review::class],version = 1)
abstract class AppDataBase :RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun reviewDao() : ReviewDao


    companion object{
        var INSTANCE: AppDataBase? = null

        fun getAppDataBase(context: Context): AppDataBase? {
            if (INSTANCE == null){
                synchronized(AppDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "movieDb").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }

}