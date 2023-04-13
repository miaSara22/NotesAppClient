package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    val id: Int,
    val email: String,
    val fullName: String,
    val image: String?,
    val pwd: String,
    val confirmPwd: String)
{ constructor(): this(0,"","", "", "", "" )}
