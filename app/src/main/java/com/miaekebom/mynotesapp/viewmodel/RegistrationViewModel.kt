package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.LoginResponse
import com.miaekebom.mynotesapp.model.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    suspend fun addNewUser(user: User): Call<User> {
        return repository.addNewUser(user)
    }

    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return repository.loginUser(loginRequest)
    }

    fun deleteUser(userId: String){
        repository.deleteUser(userId)
    }
}