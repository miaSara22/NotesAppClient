package com.miaekebom.mynotesapp.model

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.localdb.RoomDB
import com.miaekebom.mynotesapp.model.utils.SharedPref
import com.miaekebom.mynotesapp.view.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class MyServer @Inject constructor(
    @ApplicationContext val context: Context): IServerManager {

    private val sharedPref = SharedPref.getInstance(context)
    private val authToken = "Bearer " + sharedPref.getUserToken()
    private val api = IRetrofitApi.create(authToken)
    private val listDao = RoomDB.getDatabase(context).getListDao()
    private val noteDao = RoomDB.getDatabase(context).getNoteDao()
    private val userDao = RoomDB.getDatabase(context).getUserDao()

    override suspend fun addNewUser(user: User) = withContext(Dispatchers.IO) {
        try {
            val response = api.saveUser(user)
            if (response.success) {
                withContext(Dispatchers.Main) {
                    displayToast(response.message)
                    userDao.insertUser(user)
                }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun loginUser(loginRequest: LoginRequest) = withContext(Dispatchers.IO) {
        try {
            val response = api.loginUser(loginRequest)
            if (response.success) {
                val token = response.token
                val user =
                    User(response.id, response.email, response.fullName, Role.USER, null, "", "")
                sharedPref.setUserToken(token)
                sharedPref.setUser(user)
                withContext(Dispatchers.Main) {
                    displayToast(response.message)
                    displayMainActivity(user.fullName)
                }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    //***************************

    override suspend fun setUserImage(userId: Int, image: String): Call<ResponseBody> {
        return api.setUserImage(userId, image)
    }

    override suspend fun deleteUserImage(userId: Int): Call<ResponseBody> {
        return api.deleteUserImage(userId)
    }

    override suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteUser(user.id, authToken)
            if (response.isSuccessful) {
                userDao.deleteUser(user)
                withContext(Dispatchers.Main) { displayToast(response.message()) }

            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun addList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.saveList(list, authToken)
            if (response.success) {
                listDao.insertList(list)
                sharedPref.setListTimestamp()
                withContext(Dispatchers.Main) {
                    displayToast(response.message)
                }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun deleteList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteList(list.id, authToken)
            if (response.isSuccessful) {
                listDao.deleteList(list)
                withContext(Dispatchers.Main) {
                    displayToast(response.message())
                }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.updateList(list.id, list, authToken)
            if (response.isSuccessful) {
                listDao.updateList(list)
                withContext(Dispatchers.Main) { displayToast(response.message()) }

            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getUserLists(): kotlin.collections.List<List> {
        return try {
            withContext(Dispatchers.IO) {
                val userId = sharedPref.getUser().id
                val response = api.getLists(userId, authToken)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    override suspend fun addNote(listId: Int, note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.saveNote(listId, note, authToken)
            if (response.isSuccessful) {
                noteDao.insertNote(note)
                sharedPref.setNoteTimestamp()
                withContext(Dispatchers.Main) { displayToast(response.message()) }

            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteNote(note.id, authToken)
            if (response.isSuccessful) {
                noteDao.deleteNote(note)
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.updateNote(note.id, note, authToken)
            if (response.isSuccessful) {
                noteDao.updateNote(note)
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getListNotes(): kotlin.collections.List<Note> {
        return try {
            withContext(Dispatchers.IO) {
                val listId = sharedPref.getListId()
                val response = api.getNotes(listId, authToken)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    private fun error(t: Throwable) {
        Logger.getLogger(MyServer::class.java.name).log(Level.SEVERE, "Error occurred", t)
    }

    private fun displayToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun displayMainActivity(username: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}