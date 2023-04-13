package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class List(

    @PrimaryKey
    val id: Int,
    val ownerId: Int,
    val title: String,
    val createdAt: Long = System.currentTimeMillis())
{ constructor(): this(0,0,"", System.currentTimeMillis())}

