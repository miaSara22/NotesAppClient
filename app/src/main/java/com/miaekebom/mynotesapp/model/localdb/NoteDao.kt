package com.miaekebom.mynotesapp.model.localdb

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query(value = "SELECT * FROM notes")
    fun getAllNotes(): LiveData<kotlin.collections.List<Note>>

    @Query(value = "DELETE FROM notes WHERE ownerId = :ownerId")
    fun deleteNotes(ownerId: Int)
}