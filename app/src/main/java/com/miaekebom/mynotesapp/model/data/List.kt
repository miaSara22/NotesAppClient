package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class List(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var ownerId: Int,
    var title: String,
    var createdAt: Long = System.currentTimeMillis())

{ constructor(): this(0,0,"", System.currentTimeMillis())}

