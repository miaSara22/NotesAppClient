package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("token") val token: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String)
