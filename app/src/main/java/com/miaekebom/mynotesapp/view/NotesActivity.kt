package com.miaekebom.mynotesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.databinding.ActivityNotesBinding
import com.miaekebom.mynotesapp.model.data.Note
import com.miaekebom.mynotesapp.utils.SharedPref
import com.miaekebom.mynotesapp.view.DialogsManager.displayEditNoteNameDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayNoteDescDialog
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
        createRecyclerView()
        addNote()
    }

    private fun addNote(){
        binding.apply {

            val noteTitle = ETNoteName.text
            val noteDesc = ETNoteDescription.text

            BAddNote.setOnClickListener {
                val ownerId = sharedPrefs.getListId()
                val newNote = Note()
                newNote.ownerId = ownerId
                newNote.title = noteTitle.toString()
                newNote.description = noteDesc.toString()
                noteViewModel.viewModelScope.launch(Dispatchers.IO) {
                    noteViewModel.addNote(newNote)
                }
            }
        }
    }

    private fun createRecyclerView(){
        val recyclerView: RecyclerView = binding.RVNotes
        noteViewModel.notesLive.observe(this) { notes ->
            noteAdapter = NoteAdapter(
                notes.toMutableList(),
                onNoteTitleClick(),
                onNoteRemoveClick(),
                onNoteEditNameClick()
            )
            recyclerView.adapter = noteAdapter
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: androidx.appcompat.widget.SearchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(text: String): Boolean {
                searchItem(text)
                return false
            }
        })
        return true
    }

    private fun searchItem(text: String) {
        val filteredList: ArrayList<Note> = ArrayList()
        for (item in loadedNotes) {
            if (item.title.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            displayToast("No note found...")
        }else{
            noteAdapter.searchItem(filteredList)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> {  }
            R.id.menu_about -> {
                DialogsManager.displayAboutPage(this)
            }
            R.id.menu_delete_account -> {  }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}