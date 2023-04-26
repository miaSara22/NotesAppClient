package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.List
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    suspend fun addList(list: com.miaekebom.mynotesapp.model.data.List) {
        return repository.addList(list)
    }

    suspend fun deleteList(list: List) {
        return repository.deleteList(list)
    }

    suspend fun updateList(list: com.miaekebom.mynotesapp.model.data.List) {
        return repository.updateList(list)
    }

    suspend fun getUserLists(): kotlin.collections.List<List> {
        return repository.getUserLists()
    }

    suspend fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return repository.setUserImage(userId, image)
    }

    suspend fun deleteUserImage(userId: Int): Call<ResponseBody> {
        return repository.deleteUserImage(userId)
    }


}