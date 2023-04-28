package com.miaekebom.mynotesapp.model.localdb

import android.content.Context
import androidx.room.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.Note
import com.miaekebom.mynotesapp.model.data.User

@Database(entities = [List::class, User::class, Note::class], version = 6)
abstract class RoomDB: RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getListDao(): ListDao
    abstract fun getNoteDao(): NoteDao

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