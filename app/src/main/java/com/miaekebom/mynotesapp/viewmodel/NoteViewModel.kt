package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    fun addNote(listId: String, note: Note){
        repository.addNote(listId, note)
    }

    fun deleteNote(listId: String, noteId: String){
        repository.deleteNote(listId, noteId)
    }

    fun updateNote(listId: String, noteId: String){
        repository.updateNote(listId, noteId)
    }

    fun getAllNotes(listId: String){
        repository.getAllNotes(listId)
    }

    fun listenToNoteChanges(){}
}