package com.miaekebom.mynotesapp.model.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.miaekebom.mynotesapp.model.data.List

@Dao
interface ListDao {

    @Insert
    fun insertList(list: List)

    @Delete
    fun deleteList(list: List)
}