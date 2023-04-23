package com.miaekebom.mynotesapp.model.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.miaekebom.mynotesapp.model.data.List

@Dao
interface ListDao {

    @Insert
    suspend fun insertList(list: List)

    @Delete
    suspend fun deleteList(list: List)

    @Update
    suspend fun updateList(list: List)
}