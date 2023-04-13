package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    private val email: String,
    @SerializedName("pwd")
    private val pwd: String)
{constructor():this ("","")}