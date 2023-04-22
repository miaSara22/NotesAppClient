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

    fun addList(ownerId: Int, list: com.miaekebom.mynotesapp.model.data.List) {
        return repository.addList(ownerId, list)
    }

    fun deleteList(list: List) {
        return repository.deleteList(list)
    }

    fun updateList(listId: Int, list: com.miaekebom.mynotesapp.model.data.List) {
        return repository.updateList(listId, list)
    }

    fun getAllUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return repository.getUserLists(ownerId)
    }

    fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return repository.setUserImage(userId, image)
    }

    fun deleteUserImage(userId: Int): Call<ResponseBody> {
        return repository.deleteUserImage(userId)
    }


}