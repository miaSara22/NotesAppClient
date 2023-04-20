package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String
)
