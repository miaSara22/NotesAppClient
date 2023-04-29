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

    @Query(value = "UPDATE notes SET title = :noteTitle, description = :noteDesc WHERE id = :noteId")
    fun updateNote(noteTitle: String, noteDesc: String, noteId: Int)

    @Query(value = "SELECT * FROM notes WHERE ownerId = :ownerId")
    fun getListNotes(ownerId: Int): LiveData<kotlin.collections.List<Note>>

    @Query(value = "DELETE FROM notes WHERE ownerId = :ownerId")
    fun deleteNotes(ownerId: Int)
}