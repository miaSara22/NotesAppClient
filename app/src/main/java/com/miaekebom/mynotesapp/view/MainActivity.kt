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
import com.miaekebom.mynotesapp.utils.SharedPref
import com.miaekebom.mynotesapp.view.DialogsManager.displayAboutPage
import com.miaekebom.mynotesapp.view.DialogsManager.displayCreateListDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayEditListNameDialog
import com.miaekebom.mynotesapp.view.DialogsManager.displayImageDialog
import com.miaekebom.mynotesapp.view.adapters.ListAdapter
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var loadedLists: List<com.miaekebom.mynotesapp.model.data.List>
    private lateinit var listAdapter: ListAdapter
    private lateinit var binding: ActivityMainBinding
    private val sharedPref = SharedPref.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.TVUserName.text = intent.getStringExtra("username")
        onUserImageClick()
        onAddListClick()
        createRecyclerView()
        displayUserImageIfExists()
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.RVLists
        mainViewModel.listsLive.observe(this) { lists ->
            listAdapter = ListAdapter(
                lists.toMutableList(),
                onListTitleClick(),
                onListRemoveClick(),
                onListEditNameClick()
            )
            recyclerView.adapter = listAdapter
            loadedLists = lists
        }
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

    private fun displayUserImageIfExists(){
        mainViewModel.viewModelScope.launch {
            val image = mainViewModel.getUserImage(sharedPref.getUser().id)
            if (image.isNotEmpty()) {
                Picasso.get().load(image).into(binding.IBUserProfile)
            } else {
                return@launch
            }
        }
    }

    private fun onUserImageClick() {
        val userProfile = binding.IBUserProfile
        userProfile.setOnClickListener {
            displayImageDialog(sharedPref.getUser(),
                this,
                userProfile,
                mainViewModel)
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
            R.id.menu_logout -> {
                displayRegistrationActivity()
            }
            R.id.menu_about -> {
                displayAboutPage(this)
            }
            R.id.menu_delete_account -> {
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.deleteUser(sharedPref.getUser())
                    withContext(Dispatchers.Main) { displayRegistrationActivity() }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayNotesActivity(listName: String, listId: Int){
        val intent = Intent(this, NotesActivity::class.java)
        intent.putExtra("listName",listName)
        intent.putExtra("listId", listId)
        startActivity(intent)
    }

    private fun displayRegistrationActivity(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun displayToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}