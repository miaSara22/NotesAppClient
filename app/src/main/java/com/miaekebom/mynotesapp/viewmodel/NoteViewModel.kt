package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    fun addNote(listId: Int, note: Note){
        repository.addNote(listId, note)
    }

    fun deleteNote(listId: Int, noteId: Int){
        repository.deleteNote(listId, noteId)
    }

    fun updateNote(listId: Int, noteId: Int){
        repository.updateNote(listId, noteId)
    }

    fun getAllNotes(listId: Int){
        repository.getAllNotes(listId)
    }

    fun listenToNoteChanges(){}
}