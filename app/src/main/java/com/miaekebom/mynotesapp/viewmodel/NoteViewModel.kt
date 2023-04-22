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

    fun deleteNote(note: Note){
        repository.deleteNote(note)
    }

    fun updateNote(noteId: Int, note: Note){
        repository.updateNote(noteId,note)
    }

    fun getAllNotes(listId: Int){
        repository.getAllNotes(listId)
    }

    fun listenToNoteChanges(){}
}