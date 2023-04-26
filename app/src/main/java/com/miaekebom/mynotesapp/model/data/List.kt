package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class List(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var ownerId: Int,
    var title: String)

{ constructor(): this(0,0,"")}

