package com.miaekebom.mynotesapp.model

import android.content.Context
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.localdb.RoomDB
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import retrofit2.*
import javax.inject.Inject

class MyServer @Inject constructor(
    @ApplicationContext val context: Context): IServerManager {

    //retrofit
    private val authToken = "h.j.sdglfIUJKSWEDbgvgfslwjedhnuvgkilydrebgl"
    private val api = IRetrofitApi.create(authToken)

    //localdb
    private val listDao = RoomDB.getDatabase(context).getListDao()
    private val userDao = RoomDB.getDatabase(context).getUserDao()


    override fun addNewUser(user: User): Call<RegisterResponse> {
        return api.saveUser(user)
    }

    override suspend fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return api.loginUser(loginRequest)
    }

    override fun setUserImage(userId: Int, image: String): Call<Unit> {
        return api.setUserImage(userId, image)
    }

    override fun deleteUserImage(userId: Int): Call<Unit> {
        return api.deleteUserImage(userId)
    }

    override fun deleteUser(userId: Int): Call<Unit> {
        return api.deleteUser(userId)
    }


    override fun addNote(listId: Int, note: Note) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteId: Int, listId: Int) {
        TODO("Not yet implemented")
    }

    override fun updateNote(listId: Int, noteId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllListNotes(listId: Int): kotlin.collections.List<Note>? {
        TODO("Not yet implemented")
    }

    override fun addList(
        ownerId: Int,
        list: List
    ): Call<Unit> {
        return api.saveList(ownerId, list)
    }

    override fun deleteList(listId: Int): Call<Unit> {
        return api.deleteList(listId)
    }

    override fun updateList(
        listId: Int,
        list: List
    ): Call<Unit> {
        return api.updateList(listId, list)
    }

    override fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return api.getLists(ownerId)
    }
}