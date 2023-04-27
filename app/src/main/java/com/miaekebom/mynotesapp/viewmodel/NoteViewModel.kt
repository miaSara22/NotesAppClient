package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    suspend fun addNote(note: Note){
        repository.addNote(note)
    }

    suspend fun deleteNote(note: Note){
        repository.deleteNote(note)
    }

    suspend fun updateNote(note: Note){
        repository.updateNote(note)
    }

    suspend fun getListNotes(): List<Note>{
        return repository.getListNotes()
    }

    fun listenToNoteChanges(){}
}