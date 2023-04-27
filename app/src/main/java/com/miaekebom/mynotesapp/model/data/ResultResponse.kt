package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("message")
    var message: String
)
