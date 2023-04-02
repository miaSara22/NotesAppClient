package com.miaekebom.mynotesapp.model.localdb

import android.content.Context
import androidx.room.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.model.utils.Converter

@Database(entities = [List::class, User::class], version = 3)
@TypeConverters(Converter::class)
abstract class RoomDB: RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getListDao(): ListDao

    companion object{
        fun getDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                "local_database"
            ).fallbackToDestructiveMigration().build()
        }
    }
}