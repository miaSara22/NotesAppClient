package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IServerManager {

    fun addNewUser(user: User)
    suspend fun loginUser(loginRequest: LoginRequest)
    fun setUserImage(userId: Int, image: String): Call<ResponseBody>
    fun deleteUserImage(userId: Int): Call<ResponseBody>
    fun deleteUser(userId: Int): Call<Unit>

    fun addNote(listId: Int, note: Note)
    fun deleteNote(listId: Int, noteId: Int)
    fun updateNote(listId: Int, noteId: Int)
    fun getAllListNotes(listId: Int): kotlin.collections.List<Note>?

    fun addList(ownerId: Int, list: List): Call<Unit>
    fun deleteList(listId: Int): Call<Unit>
    fun updateList(listId: Int, list: List): Call<Unit>
    fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>>
}