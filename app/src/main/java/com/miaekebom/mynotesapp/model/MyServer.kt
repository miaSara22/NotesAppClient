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

    private val sharedPref = SharedPref.getInstance(context)
    private val authToken = sharedPref.getUserToken()
    private val api = IRetrofitApi.create(authToken)
    private val listDao = RoomDB.getDatabase(context).getListDao()
    private val noteDao = RoomDB.getDatabase(context).getNoteDao()
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
                            println(registerResponse.message)
                            displayToast("You Registered Successfully! please login.")

                        } else {
                            displayToast(registerResponse.message)
                            println(registerResponse.message) }
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
                    val user = User(response.id, response.email, response.fullName,null, "","")

                    sharedPref.setUserToken(token)
                    sharedPref.setUser(user)

                    displayToast(response.message)
                    println(response.message)

                } else {
                    displayToast(response?.message.toString())
                    println(response?.message)
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

    override fun deleteUser(user: User) {
        api.deleteUser(user.id).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    userDao.deleteUser(user)
                    displayToast("User deleted successfully") }
                else {
                    displayToast("Couldn't delete user. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun addList(ownerId: Int, list: List) {
        api.saveList(ownerId, list).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    listDao.insertList(list)
                    sharedPref.getListTimestamp()
                    displayToast("List added successfully")
                } else {
                       displayToast("Couldn't save list. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun deleteList(list: List) {
        api.deleteList(list.id).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    listDao.deleteList(list)
                    displayToast("List deleted successfully") }
                else {
                    displayToast("Couldn't delete list. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun updateList(listId: Int, list: List) {
        api.updateList(listId, list).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    listDao.updateList(list)
                    displayToast("List updated successfully") }
                else {
                    displayToast("Couldn't update list. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun getUserLists(ownerId: Int): Call<kotlin.collections.List<List>> {
        return api.getLists(ownerId)
    }


    override fun addNote(listId: Int, note: Note) {
        api.saveNote(listId, note).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    noteDao.insertNote(note)
                    displayToast("Note saved successfully")
                } else {
                    displayToast("Couldn't save note. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun deleteNote(note: Note) {
        api.deleteNote(note.id).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    noteDao.deleteNote(note)
                    displayToast("Note deleted successfully")
                } else {
                    displayToast("Couldn't delete note. Please try again later")
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun updateNote(noteId: Int, note: Note) {
        api.updateNote(noteId, note).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    noteDao.updateNote(note)
                    displayToast("Note updated successfully")
                } else {
                    displayToast("Couldn't update note. Please try again later") }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) } })
    }

    override fun getAllListNotes(listId: Int) {
        api.getNotes(listId).enqueue(object : Callback<kotlin.collections.List<Note>>{
            override fun onResponse(
                call: Call<kotlin.collections.List<Note>>,
                response: Response<kotlin.collections.List<Note>>
            ) {

            }
            override fun onFailure(call: Call<kotlin.collections.List<Note>>, t: Throwable) { error(t) } })
    }

    fun error(t: Throwable){
        Logger.getLogger(RegistrationActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
    }

    fun displayToast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}