package com.miaekebom.mynotesapp.model.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.Note

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}