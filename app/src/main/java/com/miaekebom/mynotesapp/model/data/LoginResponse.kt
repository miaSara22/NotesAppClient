package com.miaekebom.mynotesapp.model.data

data class LoginResponse(
    var token: String,
    var email: String,
    var fullName: String
)