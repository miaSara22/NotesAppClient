package com.miaekebom.mynotesapp.model

import androidx.lifecycle.MutableLiveData
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import retrofit2.Call

interface IRepository {

    // *** User ***
    fun addNewUser(user: User): Call<String>
    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse>
    fun deleteUser(userId: String)

    // *** Note ***
    fun addNote(listId: String, note: Note)
    fun deleteNote(listId: String, noteId: String)
    fun updateNote(listId: String, noteId: String)
    fun getAllNotes(listId: String): kotlin.collections.List<Note>?

    // *** List ***
    fun addList(ownerId: Int, list: List): Call<Unit>
    fun deleteList(listId: Int): Call<Unit>
    fun updateList(listId: Int, list: List): Call<Unit>
    fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>>
}