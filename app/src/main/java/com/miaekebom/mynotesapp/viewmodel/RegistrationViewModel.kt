package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.LoginResponse
import com.miaekebom.mynotesapp.model.data.RegisterResponse
import com.miaekebom.mynotesapp.model.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    fun addNewUser(user: User) {
        repository.addNewUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest) {
        return repository.loginUser(loginRequest)
    }

    fun deleteUser(userId: Int): Call<Unit>{
        return repository.deleteUser(userId)
    }
}