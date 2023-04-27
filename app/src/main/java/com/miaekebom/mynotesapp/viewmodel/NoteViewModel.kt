package com.miaekebom.mynotesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.data.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: IRepository): ViewModel(){

    private val _notesLive: MutableLiveData<List<Note>> = MutableLiveData()
    val notesLive: LiveData<List<Note>> = _notesLive

    init {
        viewModelScope.launch {
            repository.listenToNotesChanges().observeForever { notes ->
                _notesLive.value = notes
            }
        }
    }

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