package com.miaekebom.mynotesapp.model.data

data class LoginResponse(
    var success: Boolean,
    var message: String?,
    var token: String?
)