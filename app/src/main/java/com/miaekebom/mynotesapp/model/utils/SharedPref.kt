package com.miaekebom.mynotesapp.model.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.User

const val fullName = "FULL_NAME"
const val email = "EMAIL"
const val id = "ID"
const val token = "TOKEN"
const val noteTimestamp = "NOTE_TIMESTAMP"
const val listTimeStamp = "LIST_TIMESTAMP"
const val loginTimestamp = "LOGIN_TIMESTAMP"

class SharedPref private constructor(context: Context) {

    private val sharePref =
        context.getSharedPreferences(R.string.app_name.toString(), AppCompatActivity.MODE_PRIVATE)

    companion object {
        private lateinit var instance: SharedPref
        fun getInstance(context: Context): SharedPref {
            if (!Companion::instance.isInitialized) {
                instance = SharedPref(context)
            }
            return instance
        }
    }

    fun setUser(user: User) {
        sharePref.edit()
            .putString(fullName, user.fullName)
            .putString(email, user.email)
            .apply()
    }

    fun getUser(): User {
        val id = sharePref.getInt(id, 0)
        val email = sharePref.getString(email, "")
        val fullName = sharePref.getString(fullName, "")
        return User( 2,email.toString(), fullName.toString(), "", "", "")
    }

    fun setUserToken(jwtToken: String){
        sharePref.edit()
            .putString(token, jwtToken)
            .apply()
    }

    fun getUserToken(): String {
        return sharePref.getString(token,null).toString()
    }

    fun setLoginTimestamp() {
        sharePref.edit()
            .putLong(loginTimestamp, System.currentTimeMillis())
            .apply()
    }

    fun getLoginTimestamp(): Long {
        return sharePref.getLong(loginTimestamp, -1)
    }

    fun setNoteTimestamp() {
        sharePref.edit()
            .putLong(noteTimestamp, System.currentTimeMillis())
            .apply()
    }

    fun getNoteTimestamp(): Long {
        return sharePref.getLong(noteTimestamp, -1)
    }
}