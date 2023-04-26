package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

interface IRepository {

    suspend fun addNewUser(user: User)
    suspend fun loginUser(loginRequest: LoginRequest)
    suspend fun setUserImage(userId: Int, image: String): Call<ResponseBody>
    suspend fun deleteUserImage(userId: Int): Call<ResponseBody>
    suspend fun deleteUser(user: User)

    suspend fun addNote(listId: Int, note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun getListNotes(): kotlin.collections.List<Note>

    suspend fun addList(list: List)
    suspend fun deleteList(list: List)
    suspend fun updateList(list: List)
    suspend fun getUserLists(): kotlin.collections.List<List>
}