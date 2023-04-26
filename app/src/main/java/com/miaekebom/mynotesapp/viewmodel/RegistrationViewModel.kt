package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    suspend fun addNewUser(user: User) {
        repository.addNewUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest) {
        return repository.loginUser(loginRequest)
    }

    suspend fun deleteUser(user: User) {
        return repository.deleteUser(user)
    }
}