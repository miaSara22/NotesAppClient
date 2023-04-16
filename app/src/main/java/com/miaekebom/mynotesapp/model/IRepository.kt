package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

interface IRepository {

    fun addNewUser(user: User)
    suspend fun loginUser(loginRequest: LoginRequest)
    fun setUserImage(userId: Int, image: String): Call<ResponseBody>
    fun deleteUserImage(userId: Int): Call<ResponseBody>
    fun deleteUser(userId: Int): Call<Unit>

    fun addNote(listId: Int, note: Note)
    fun deleteNote(listId: Int, noteId: Int)
    fun updateNote(listId: Int, noteId: Int)
    fun getAllNotes(listId: Int): kotlin.collections.List<Note>?

    fun addList(ownerId: Int, list: List): Call<Unit>
    fun deleteList(listId: Int): Call<Unit>
    fun updateList(listId: Int, list: List): Call<Unit>
    fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>>
}