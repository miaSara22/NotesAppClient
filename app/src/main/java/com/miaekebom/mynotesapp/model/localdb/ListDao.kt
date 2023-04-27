package com.miaekebom.mynotesapp.model.localdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.miaekebom.mynotesapp.model.data.List

@Dao
interface ListDao {

    @Insert
    fun insertList(list: List)

    @Delete
    fun deleteList(list: List)

    @Update
    fun updateList(list: List)

    @Query(value = "SELECT * FROM lists")
    fun getAllLists(): LiveData<kotlin.collections.List<List>>
}