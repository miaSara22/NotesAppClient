package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("message")
    var message: String
)
