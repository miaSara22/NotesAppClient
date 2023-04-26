package com.miaekebom.mynotesapp.model.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("email")
    var email: String,
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("role")
    var role: Role,
    @SerializedName("image")
    var image: String?,
    @SerializedName("pwd")
    var pwd: String,

    @Ignore
    @SerializedName("confirmPwd")
    val confirmPwd: String)
{ constructor(): this(0,"","", Role.USER, "","", "")}
