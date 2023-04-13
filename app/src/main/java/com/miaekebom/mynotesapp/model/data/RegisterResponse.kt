package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String?)