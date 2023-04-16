package com.miaekebom.mynotesapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.model.utils.SharedPref
import com.miaekebom.mynotesapp.view.adapters.ListAdapter
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_list_dialog.*
import kotlinx.android.synthetic.main.add_list_dialog.view.*
import kotlinx.android.synthetic.main.choose_image_dialog.*
import kotlinx.android.synthetic.main.choose_image_dialog.view.*
import kotlinx.android.synthetic.main.edit_text_dialog.*
import kotlinx.android.synthetic.main.edit_text_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var loadedLists: List<com.miaekebom.mynotesapp.model.data.List>
    private lateinit var listAdapter: ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TV_user_name.text = intent.getStringExtra("username")
        onUserImageClick()
        onAddListClick()
        loadLists()
    }



    private fun onUserImageClick() {
        IB_user_profile.setOnClickListener {
            displayChooseImageDialog()
        }
    }

    private fun loadLists(){
        mainViewModel.viewModelScope.launch(Dispatchers.IO) {

            mainViewModel.getAllUserLists(SharedPref.getInstance(this@MainActivity).getUser().id).enqueue(object :Callback<List<com.miaekebom.mynotesapp.model.data.List>>{
                override fun onResponse(
                    call: Call<List<com.miaekebom.mynotesapp.model.data.List>>,
                    response: Response<List<com.miaekebom.mynotesapp.model.data.List>>
                ) {
                    response.body().let {
                        it?.let {
                            loadedLists = it
                            createRecyclerView(it)
                        }
                    }
                }
                override fun onFailure(
                    call: Call<List<com.miaekebom.mynotesapp.model.data.List>>,
                    t: Throwable) { error(t) }
            })
        }

    }

    private fun createRecyclerView(list: List<com.miaekebom.mynotesapp.model.data.List>){
        val recyclerView: RecyclerView = RV_lists
        listAdapter = ListAdapter(
            list.toMutableList(),
            onListTitleClick(),
            onListRemoveClick(),
            onListEditNameClick()
        )
        recyclerView.adapter = listAdapter
    }

    private fun onListTitleClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit = {
        displayNotesActivity(it.title)
    }

    private fun onListRemoveClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit =  {
        mainViewModel.viewModelScope.launch(Dispatchers.IO) {
            mainViewModel.deleteList(it.id).enqueue(object: Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful)
                        displayToast("Deleted successfully")
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) }
            })
        }
    }

    private fun onListEditNameClick(): (com.miaekebom.mynotesapp.model.data.List) -> Unit = {
        displayEditNameDialog(it)
    }

    private fun onAddListClick() {
        IB_create_list.setOnClickListener {
            displayCreateListDialog()
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
            R.id.menu_about -> { displayAboutPage() }
            R.id.menu_delete_account -> { displayToast("Hi from delete account") }
        }
        return super.onOptionsItemSelected(item)
    }

    //dialog functions:

    private fun displayAboutPage() {
        val view = layoutInflater.inflate(R.layout.about, about, false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun displayChooseImageDialog() {
        val view =
            layoutInflater.inflate(R.layout.choose_image_dialog, CV_choose_image_dialog, false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val galleryImage = view.B_image_from_gallery
        val cameraImage = view.B_image_from_camera
        val delImage = view.B_delete_image

        galleryImage.setOnClickListener {}
        cameraImage.setOnClickListener {}
        delImage.setOnClickListener {}

    }

    private fun displayCreateListDialog() {
        val view = layoutInflater.inflate(R.layout.add_list_dialog, LL_add_list_dialog, false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val listName = view.list_name_dialog_ET.text
        val createListB = view.add_list_dialog_B

        createListB.setOnClickListener {
            val list = com.miaekebom.mynotesapp.model.data.List(1, 1, listName.toString())
            mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                mainViewModel.addList(list.ownerId, list).enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful)
                            displayToast("Saved successfully")
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) }
                })
            }
            dialog.dismiss()
        }
    }

    private fun displayEditNameDialog(list: com.miaekebom.mynotesapp.model.data.List) {
        val view = layoutInflater.inflate(R.layout.edit_text_dialog, LL_edit_text_dialog, false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val editListName = view.ET_new_list_name.text.append(list.title)
        val saveB = view.B_save_changes
        val cancelB = view.B_cancel_changes

        saveB.setOnClickListener {
            val updatedList = com.miaekebom.mynotesapp.model.data.List(
                list.id,
                list.ownerId,
                editListName.toString()
            )
            mainViewModel.viewModelScope.launch(Dispatchers.IO){
                mainViewModel.updateList(list.id, updatedList).enqueue(object: Callback<Unit>{

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful)
                            displayToast("Updated successfully")
                        dialog.dismiss()
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) { error(t) }
                })
            }
        }
        cancelB.setOnClickListener { dialog.dismiss() }
    }



    private fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun saveImage(currentUser: User, uri: String, context: Context) {
        mainViewModel.viewModelScope.launch(Dispatchers.IO) {
            mainViewModel.setUserImage(currentUser.id, uri).enqueue(object: Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


    private fun displayNotesActivity(listName: String){
        runOnUiThread {
            val intent = Intent(this, NotesActivity::class.java)
            intent.putExtra("listName",listName)
            startActivity(intent)
        }
    }

    fun displayToast(text: String){
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }

    fun error(t: Throwable){
        Logger.getLogger(MainActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
        displayToast("An error occurred. Please try again later")
    }
}