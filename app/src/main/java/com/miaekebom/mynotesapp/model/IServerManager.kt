package com.miaekebom.mynotesapp.model

import androidx.lifecycle.LiveData
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List


interface IServerManager {

    suspend fun addNewUser(user: User)
    suspend fun loginUser(loginRequest: LoginRequest)
    suspend fun deleteUser(user: User)
    suspend fun updateUserImage(user: User, imagePath: String)
    suspend fun deleteUserImage(user: User)
    suspend fun getUserImage(userId: Int): String

    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getListNotes(): kotlin.collections.List<Note>
    suspend fun updateNote(note: Note)
    fun listenToNotesChanges(): LiveData<kotlin.collections.List<Note>>

    suspend fun deleteList(list: List)
    suspend fun updateList(list: List)
    suspend fun getUserLists(): kotlin.collections.List<List>
    suspend fun addList(list: List)
    fun listenToListsChanges(): LiveData<kotlin.collections.List<List>>

}