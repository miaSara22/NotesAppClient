package com.miaekebom.mynotesapp.model

import android.content.Context
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class Repository@Inject constructor(
    @ApplicationContext context: Context,
    private val serverManager: IServerManager
): IRepository {

    override fun addNewUser(user: User): Call<RegisterResponse> {
        return serverManager.addNewUser(user)
    }

    override suspend fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return serverManager.loginUser(loginRequest)
    }

    override fun setUserImage(userId: Int, image: String): Call<Unit> {
        return serverManager.setUserImage(userId, image)
    }

    override fun deleteUserImage(userId: Int): Call<Unit> {
        return serverManager.deleteUserImage(userId)
    }

    override fun deleteUser(userId: Int): Call<Unit> {
        return serverManager.deleteUser(userId)
    }

    override fun addNote(listId: Int, note: Note) {
        serverManager.addNote(listId, note)
    }

    override fun deleteNote(listId: Int, noteId: Int) {
        serverManager.deleteNote(listId, noteId)
    }

    override fun updateNote(listId: Int, noteId: Int) {
        serverManager.updateNote(listId, noteId)
    }

    override fun getAllNotes(listId: Int): kotlin.collections.List<Note>? {
        return serverManager.getAllListNotes(listId)
    }

    override fun addList(ownerId: Int, list: List): Call<Unit> {
        return serverManager.addList(ownerId, list)
    }

    override fun deleteList(listId: Int): Call<Unit> {
        return serverManager.deleteList(listId)
    }

    override fun updateList(listId: Int, list: List): Call<Unit> {
        return serverManager.updateList(listId, list)
    }

    override fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return serverManager.getUserLists(ownerId)
    }
}