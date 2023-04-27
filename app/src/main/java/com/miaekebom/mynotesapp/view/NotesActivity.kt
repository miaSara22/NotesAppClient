package com.miaekebom.mynotesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.databinding.ActivityNotesBinding
import com.miaekebom.mynotesapp.model.data.Note
import com.miaekebom.mynotesapp.model.utils.SharedPref
import com.miaekebom.mynotesapp.view.DialogsManager.displayEditNoteNameDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayNoteDescDialog
import com.miaekebom.mynotesapp.view.adapters.ListAdapter
import com.miaekebom.mynotesapp.view.adapters.NoteAdapter
import com.miaekebom.mynotesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var loadedNotes: List<Note>
    private lateinit var noteAdapter: NoteAdapter
    private val sharedPrefs = SharedPref.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.TVListName.text = intent.getStringExtra("listName")
        sharedPrefs.setListId(intent.getIntExtra("listId", 0))
        loadNotes()
        addNote()
    }

    private fun addNote(){
        binding.apply {

            val noteTitle = ETNoteName.text
            val noteDesc = ETNoteDescription.text

            BAddNote.setOnClickListener {
                val note = Note(0, sharedPrefs.getListId(), noteTitle.toString(), noteDesc.toString())
                println(sharedPrefs.getListId())
                noteViewModel.viewModelScope.launch(Dispatchers.IO) {
                    noteViewModel.addNote(note)
                }
            }
        }
    }

    private fun loadNotes() {
        noteViewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                val notes = noteViewModel.getListNotes()
                runOnUiThread {
                    createRecyclerView(notes)
                    loadedNotes = notes
                }
            } catch (t: Throwable) {
                error(t)
            }
        }
    }

    private fun createRecyclerView(list: List<Note>){
        val recyclerView: RecyclerView = binding.RVNotes
        noteViewModel.notesLive.observe(this) { notes ->
            noteAdapter = NoteAdapter(
                notes.toMutableList(),
                onNoteTitleClick(),
                onNoteRemoveClick(),
                onNoteEditNameClick()
            )
            recyclerView.adapter = noteAdapter
            noteAdapter.updateChanges(notes)
            loadedNotes = notes
        }

    }

    private fun onNoteTitleClick(): (Note) -> Unit = {
        displayNoteDescDialog(this, noteViewModel, it.description)
    }

    private fun onNoteRemoveClick(): (Note) -> Unit = {
        noteViewModel.viewModelScope.launch(Dispatchers.IO) {
            noteViewModel.deleteNote(it)
        }
    }

    private fun onNoteEditNameClick(): (Note) -> Unit  = {
        displayEditNoteNameDialog(this, noteViewModel, it)
    }
}