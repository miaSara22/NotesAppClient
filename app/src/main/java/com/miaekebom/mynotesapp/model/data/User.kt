package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var email: String,
    var fullName: String,
    var role: Role,
    var image: String?,
    var pwd: String,

    @Ignore
    val confirmPwd: String)
{ constructor(): this(0,"","", Role.USER, "","", "")}
