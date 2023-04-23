package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var ownerId: Int,
    var title: String,
    var description: String,
    var createdAt: Long = System.currentTimeMillis())

