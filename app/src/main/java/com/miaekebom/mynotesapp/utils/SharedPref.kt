package com.miaekebom.mynotesapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.Role
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.view.MainActivity
import kotlinx.coroutines.CoroutineScope

const val fullName = "USER_FULL_NAME"
const val email = "USER_EMAIL"
const val id = "USER_ID"
const val token = "USER_TOKEN"
const val listId = "LIST_ID"

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
            .putInt(id, user.id)
            .putString(fullName, user.fullName)
            .putString(email, user.email)
            .apply() }

    fun getUser(): User {
        val id = sharePref.getInt(id, 0)
        val email = sharePref.getString(email, "")
        val fullName = sharePref.getString(fullName, "")
        return User( id ,email.toString(), fullName.toString(),Role.USER, "", "", "") }

    fun setUserToken(jwtToken: String){
        sharePref.edit()
            .putString(token, jwtToken)
            .apply() }

    fun getUserToken(): String {
        return sharePref.getString(token,null).toString() }

    fun setListId(id: Int){
        sharePref.edit()
            .putInt(listId, id)
            .apply()
    }

    fun getListId(): Int{
        return sharePref.getInt(listId, 0)
    }
}