package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    private val _listsLive: MutableLiveData<kotlin.collections.List<List>> = MutableLiveData()
    val listsLive: LiveData<kotlin.collections.List<List>> = _listsLive

    init {
        viewModelScope.launch {
            repository.listenToListsChanges().observeForever { lists ->
                _listsLive.value = lists
            }
        }
    }

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

    suspend fun updateUserImage(user: User, imagePath: String) {
        return repository.updateUserImage(user, imagePath)

    }

    suspend fun deleteUserImage(user: User) {
        return repository.deleteUserImage(user)
    }
}