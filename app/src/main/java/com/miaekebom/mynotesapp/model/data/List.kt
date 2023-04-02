package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class List(

    @PrimaryKey
    val listId: Int,
    val ownerId: Int,
    val listName: String,
    val createdAt: Long = System.currentTimeMillis()

){
    constructor(): this(0,0,"", System.currentTimeMillis()
    )}

