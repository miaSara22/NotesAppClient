package com.miaekebom.mynotesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.databinding.ActivityMainBinding
import com.miaekebom.mynotesapp.view.DialogsManager.displayAboutPage
import com.miaekebom.mynotesapp.view.DialogsManager.displayChooseImageDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayCreateListDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayEditListNameDialog
import com.miaekebom.mynotesapp.view.adapters.ListAdapter
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var loadedLists: List<com.miaekebom.mynotesapp.model.data.List>
    private lateinit var listAdapter: ListAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.TVUserName.text = intent.getStringExtra("username")
        //val userId = intent.getStringExtra("userId")
        onUserImageClick()
        onAddListClick()
        loadLists()
    }

    private fun loadLists() {
        mainViewModel.viewModelScope.launch {
            try {
                val lists = mainViewModel.getUserLists()
                createRecyclerView(lists)
                loadedLists = lists
            } catch (t: Throwable) {
                error(t)
            }
        }
    }

    private fun createRecyclerView(list: List<com.miaekebom.mynotesapp.model.data.List>){
        val recyclerView: RecyclerView = binding.RVLists
        listAdapter = ListAdapter(
            list.toMutableList(),
            onListTitleClick(),
            onListRemoveClick(),
            onListEditNameClick()
        )
        recyclerView.adapter = listAdapter
    }

    private fun onListTitleClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit = {
        displayNotesActivity(it.title, it.id)
    }

    private fun onListRemoveClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit =  {
        mainViewModel.viewModelScope.launch(Dispatchers.IO) {
            mainViewModel.deleteList(it)
        }
    }

    private fun onListEditNameClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit = {
        displayEditListNameDialog(this, mainViewModel, it)
    }

    private fun onUserImageClick() {
        binding.IBUserProfile.setOnClickListener {
            displayChooseImageDialog(this)
        }
    }

    private fun onAddListClick() {
        binding.IBCreateList.setOnClickListener {
            displayCreateListDialog(this, mainViewModel)
        }
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
        val filteredList: ArrayList<com.miaekebom.mynotesapp.model.data.List> = ArrayList()
        for (item in loadedLists) {
            if (item.title.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            displayToast("No list found...")
        }else{
            listAdapter.searchItem(filteredList)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> { displayToast("Hi from logout") }
            R.id.menu_about -> { displayAboutPage(this) }
            R.id.menu_delete_account -> { displayToast("Hi from delete account") }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayNotesActivity(listName: String, listId: Int){
        runOnUiThread {
            val intent = Intent(this, NotesActivity::class.java)
            intent.putExtra("listName",listName)
            intent.putExtra("listId", listId)
            startActivity(intent) }
    }

    private fun displayToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show() }

    private fun error(t: Throwable){
        Logger.getLogger(MainActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
        displayToast("An error occurred. Please try again later") }
}