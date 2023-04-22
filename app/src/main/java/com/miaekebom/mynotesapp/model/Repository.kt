package com.miaekebom.mynotesapp.model

import android.content.Context
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

    override fun addNewUser(user: User) {
        return serverManager.addNewUser(user)
    }

    override suspend fun loginUser(loginRequest: LoginRequest){
        return serverManager.loginUser(loginRequest)
    }

    override fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return serverManager.setUserImage(userId, image)
    }

    override fun deleteUserImage(userId: Int): Call<ResponseBody> {
        return serverManager.deleteUserImage(userId)
    }

    override fun deleteUser(user: User) {
        serverManager.deleteUser(user)
    }

    override fun addNote(listId: Int, note: Note) {
        serverManager.addNote(listId, note)
    }

    override fun deleteNote(note: Note) {
        serverManager.deleteNote(note)
    }

    override fun updateNote(noteId: Int, note: Note) {
        serverManager.updateNote(noteId, note)
    }

    override fun getAllNotes(listId: Int) {
        serverManager.getAllListNotes(listId)
    }

    override fun addList(ownerId: Int, list: List) {
        return serverManager.addList(ownerId, list)
    }

    override fun deleteList(list: List) {
        return serverManager.deleteList(list)
    }

    override fun updateList(listId: Int, list: List) {
        return serverManager.updateList(listId, list)
    }

    override fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return serverManager.getUserLists(ownerId)
    }
}