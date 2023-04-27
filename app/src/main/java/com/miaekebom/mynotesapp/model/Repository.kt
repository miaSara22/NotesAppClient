package com.miaekebom.mynotesapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class Repository@Inject constructor(
    @ApplicationContext context: Context,
    private val serverManager: IServerManager
): IRepository {

    override suspend fun addNewUser(user: User) {
        return serverManager.addNewUser(user)
    }

    override suspend fun loginUser(loginRequest: LoginRequest){
        return serverManager.loginUser(loginRequest)
    }

    override suspend fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return serverManager.setUserImage(userId, image)
    }

    override suspend fun deleteUserImage(userId: Int): Call<ResponseBody> {
        return serverManager.deleteUserImage(userId)
    }

    override suspend fun deleteUser(user: User) {
        serverManager.deleteUser(user)
    }

    override suspend fun addNote(note: Note) {
        serverManager.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        serverManager.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        serverManager.updateNote(note)
    }

    override suspend fun getListNotes(): kotlin.collections.List<Note> {
        return serverManager.getListNotes()
    }

    override suspend fun addList(list: List) {
        return serverManager.addList(list)
    }

    override suspend fun deleteList(list: List) {
        return serverManager.deleteList(list)
    }

    override suspend fun updateList(list: List) {
        return serverManager.updateList(list)
    }

    override suspend fun getUserLists(): kotlin.collections.List<List> {
        return serverManager.getUserLists()
    }

    override fun listenToListsChanges(): LiveData<kotlin.collections.List<List>> {
        return serverManager.listenToListsChanges()
    }
}