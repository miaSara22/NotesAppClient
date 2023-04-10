package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    @SerializedName("userId")
    val userId: Int,
    val email: String,
    val fullName: String,
    val userPwd: String,
    val confirmUserPwd: String
){
constructor(): this(0,"","", "", ""
)}
