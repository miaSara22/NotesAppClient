package com.miaekebom.mynotesapp.model

import android.content.Context
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.localdb.RoomDB
import com.miaekebom.mynotesapp.model.utils.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import retrofit2.*
import javax.inject.Inject

class MyServer @Inject constructor(
    @ApplicationContext val context: Context): IServerManager {

    //retrofit
    private val authToken = "hjsdglfIUJKSWEDbgvgfslwjedhnuvgkilydrebgl"
    private val api = IRetrofitApi.create(authToken)

    //localdb
    private val listDao = RoomDB.getDatabase(context).getListDao()
    private val userDao = RoomDB.getDatabase(context).getUserDao()



    override fun addNewUser(requestBody: RequestBody): Call<RegisterResponse> {
        return api.saveUser(requestBody)
    }

    override fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return api.loginUser(loginRequest)
    }

    override fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }


    override fun addNote(listId: String, note: Note) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteId: String, listId: String) {
        TODO("Not yet implemented")
    }

    override fun updateNote(listId: String, noteId: String) {
        TODO("Not yet implemented")
    }

    override fun getAllListNotes(listId: String): kotlin.collections.List<Note>? {
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