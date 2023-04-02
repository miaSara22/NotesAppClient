package com.miaekebom.mynotesapp.model.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.miaekebom.mynotesapp.model.data.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

}