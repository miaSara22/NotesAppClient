package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    var token: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("fullName")
    var fullName: String
)