package com.miaekebom.mynotesapp.model

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.localdb.RoomDB
import com.miaekebom.mynotesapp.model.utils.SharedPref
import com.miaekebom.mynotesapp.view.RegistrationActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class MyServer @Inject constructor(
    @ApplicationContext val context: Context): IServerManager {

    //shared prefs
    private val sharedPref = SharedPref.getInstance(context)

    //retrofit
    private val authToken = sharedPref.getUserToken()
    private val api = IRetrofitApi.create(authToken)

    //localdb
    private val listDao = RoomDB.getDatabase(context).getListDao()
    //private val noteDao = RoomDB.getDatabase(context).getNoteDao()
    private val userDao = RoomDB.getDatabase(context).getUserDao()


    override fun addNewUser(user: User) {
        api.saveUser(user).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val gson = Gson()
                        val registerResponse = gson.fromJson(
                            responseBody.message,
                            RegisterResponse::class.java)

                        if (registerResponse.success) {
                            userDao.insertUser(user)
                            displayToast("You Registered Successfully! please login.")

                        } else {
                            displayToast(registerResponse.message ?: "Registration failed. please try again later.")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) { error(t) } })
    }

    override suspend fun loginUser(loginRequest: LoginRequest) {
        api.loginUser(loginRequest).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val response = response.body()
                if (response != null && response.success){
                    val token = response.token
                    sharedPref.setUserToken(token)
                    displayToast(response.message)

                } else {
                    displayToast(response?.message.toString() ?: "Login failed")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) { error(t) } })
    }

    override fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return api.setUserImage(userId, image)
    }

    override fun deleteUserImage(userId: Int): Call<ResponseBody> {
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

    fun error(t: Throwable){
        Logger.getLogger(RegistrationActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
    }

    fun displayToast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}