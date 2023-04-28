package com.miaekebom.mynotesapp.model.localdb

import androidx.room.*
import com.miaekebom.mynotesapp.model.data.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query(value = "UPDATE users SET image = :userImage WHERE id = :userId")
    fun updateUserImage(userImage: String, userId: Int)

    @Query(value = "UPDATE users SET image = NULL WHERE id = :userId")
    fun deleteUserImage(userId: Int)

}