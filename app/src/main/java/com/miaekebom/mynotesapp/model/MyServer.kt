package com.miaekebom.mynotesapp.model

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.miaekebom.mynotesapp.model.data.*
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.localdb.RoomDB
import com.miaekebom.mynotesapp.utils.SharedPref
import com.miaekebom.mynotesapp.view.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                userDao.insertUser(user)
                withContext(Dispatchers.Main) { displayToast(response.message) }
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
                Logger.getLogger(MyServer::class.java.name).log(Level.INFO, "Auth token login method: $token")
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

    override suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteUser(user, authToken)
            if (response.isSuccessful) {
                userDao.deleteUser(user)

                val lists: kotlin.collections.List<List> = listDao.getUserLists(user.id)
                for (list in lists){
                    noteDao.deleteNotes(list.id)
                }
                listDao.deleteLists(user.id)
                withContext(Dispatchers.Main) { displayToast(response.message()) }

            } else {
                withContext(Dispatchers.Main) { displayToast(response.message()) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateUserImage(user: User, imagePath: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.updateUserImage(user.id, imagePath, authToken)
            if (response.success) {
                userDao.updateUserImage(imagePath, user.id)
                withContext(Dispatchers.Main) {displayToast(response.message)}
            } else {
                withContext(Dispatchers.Main) {displayToast(response.message)}
            }
        }  catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun deleteUserImage(user: User) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteUserImage(user.id, authToken)
            if (response.success) {
                userDao.deleteUserImage(user.id)
                withContext(Dispatchers.Main) {displayToast(response.message)}
            } else {
                withContext(Dispatchers.Main) {displayToast(response.message)}
            }

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getUserImage(userId: Int): String {

        return try {
            withContext(Dispatchers.IO) {
                val response = api.getUserImage(userId, authToken)
                if (!response.isNullOrBlank()) {
                    response
                } else {
                    ""
                }
            }
        } catch (e: Exception) {
            error(e)
            ""
        }
    }

    override suspend fun addList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.saveList(list, authToken)
            Logger.getLogger(MyServer::class.java.name).log(Level.INFO, "Auth token from add list method: $authToken")
            if (response.success) {
                listDao.insertList(list)
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

    override fun listenToListsChanges(): LiveData<kotlin.collections.List<List>> = listDao.getUserListsLiveData(sharedPref.getUser().id)

    override fun listenToNotesChanges(): LiveData<kotlin.collections.List<Note>> = noteDao.getListNotes(sharedPref.getListId())

    override suspend fun deleteList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteList(list, authToken)
            if (response.success) {
                listDao.deleteList(list)
                noteDao.deleteNotes(list.id)
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

    override suspend fun updateList(list: List) = withContext(Dispatchers.IO) {
        try {
            val response = api.updateList(list, authToken)
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

    override suspend fun addNote(note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.saveNote(note, authToken)

            if (response.success) {
                noteDao.insertNote(note)
                withContext(Dispatchers.Main) { displayToast(response.message) }

            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteNote(note, authToken)
            if (response.success) {
                noteDao.deleteNote(note)
                withContext(Dispatchers.Main) { displayToast(response.message) }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
        try {
            val response = api.updateNote(note, authToken)
            if (response.success) {
                noteDao.updateNote(note.title, note.description, note.id)
                withContext(Dispatchers.Main) { displayToast(response.message) }
            } else {
                withContext(Dispatchers.Main) { displayToast(response.message) }
            }
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getListNotes(): kotlin.collections.List<Note> {
        return try {
            withContext(Dispatchers.IO) {
                val ownerId = sharedPref.getListId()
                withContext(Dispatchers.Main) { displayToast("Current owner id is: $ownerId") }
                val response = api.getNotes(ownerId, authToken)
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