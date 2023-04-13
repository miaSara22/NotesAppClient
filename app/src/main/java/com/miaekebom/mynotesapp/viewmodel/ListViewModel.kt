package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.List
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    suspend fun addList(ownerId: Int, list: com.miaekebom.mynotesapp.model.data.List): Call<Unit> {
        return repository.addList(ownerId, list)
    }

    suspend fun deleteList(listId: Int): Call<Unit> {
        return repository.deleteList(listId)
    }

    suspend fun updateList(listId: Int, list: com.miaekebom.mynotesapp.model.data.List): Call<Unit> {
        return repository.updateList(listId, list)
    }

    suspend fun getAllUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return repository.getUserLists(ownerId)
    }
}