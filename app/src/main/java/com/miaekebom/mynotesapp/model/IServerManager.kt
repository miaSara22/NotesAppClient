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
    fun deleteUser(user: User)

    fun addNote(listId: Int, note: Note)
    fun deleteNote(note: Note)
    fun getAllListNotes(listId: Int)
    fun updateNote(noteId: Int, note: Note)

    fun addList(ownerId: Int, list: List)
    fun deleteList(list: List)
    fun updateList(listId: Int, list: List)
    fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>>

}