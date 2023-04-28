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

    @Query(value = "SELECT * FROM lists WHERE ownerId = :ownerId")
    fun getUserLists(ownerId: Int): LiveData<kotlin.collections.List<List>>

    @Query(value = "DELETE FROM lists WHERE ownerId = :ownerId")
    fun deleteLists(ownerId: Int)
}