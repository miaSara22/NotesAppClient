package com.miaekebom.mynotesapp.model.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.LoginResponse
import com.miaekebom.mynotesapp.model.data.Note

class Converter {

    @TypeConverter
    fun toNotesList(notes: String): ArrayList<Note> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Note>>() {}.type
        return gson.fromJson(notes, type)
    }
    @TypeConverter
    fun fromNotesList(notes: ArrayList<Note>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Note>>() {}.type
        return gson.toJson(notes, type)
    }
    @TypeConverter
    fun toListsList(listsList: String): ArrayList<List> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<List>>() {}.type
        return gson.fromJson(listsList, type)
    }
    @TypeConverter
    fun fromListsList(listsList: ArrayList<List>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<List>>() {}.type
        return gson.toJson(listsList, type)
    }
}