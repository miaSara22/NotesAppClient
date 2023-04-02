package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    val userId: Int,
    val email: String,
    val fullName: String,

){ constructor(): this(0,"","") }
