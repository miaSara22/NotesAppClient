package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import okhttp3.RequestBody
import retrofit2.Call

interface IServerManager {

    fun addNewUser(user: User): Call<RegisterResponse>
    suspend fun loginUser(loginRequest: LoginRequest): Call<LoginResponse>
    fun setUserImage(userId: Int, image: String): Call<Unit>
    fun deleteUserImage(userId: Int): Call<Unit>
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