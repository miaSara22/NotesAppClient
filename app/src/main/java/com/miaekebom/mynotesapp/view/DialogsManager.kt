package com.miaekebom.mynotesapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.databinding.*
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.Note
import com.miaekebom.mynotesapp.model.data.Role
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.utils.ImagesManager
import com.miaekebom.mynotesapp.utils.SharedPref
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import com.miaekebom.mynotesapp.viewmodel.NoteViewModel
import com.miaekebom.mynotesapp.viewmodel.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DialogsManager {

    fun displayRegisterDialog(context: Context, registrationViewModel: RegistrationViewModel) {
        val binding = RegisterDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.BRegister.setOnClickListener {
            val email = binding.ETEmailRegister.text.toString()
            val fullName = binding.ETUserNameRegister.text.toString()
            val pass1 = binding.ETPasswordRegister1.text.toString()
            val pass2 = binding.ETPasswordRegister2.text.toString()

            val user = User(0, email, fullName,Role.USER, null, pass1, pass2)

            if (user.email.isNotEmpty() &&
                user.fullName.isNotEmpty() &&
                user.pwd.isNotEmpty() &&
                user.confirmPwd.isNotEmpty() &&
                user.pwd == user.confirmPwd ){

                registrationViewModel.viewModelScope.launch(Dispatchers.IO) {
                    registrationViewModel.addNewUser(user)
                }
            } else {
                displayToast("All fields must not be empty. Passwords must match.", context)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun displayLoginDialog(context: Context, registrationViewModel: RegistrationViewModel) {
        val binding = LoginDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.BLogin.setOnClickListener {
            val email = binding.ETEmailLogin.text.toString()
            val pwd = binding.ETPasswordLogin.text.toString()
            val loginRequest = LoginRequest(email, pwd)

            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                registrationViewModel.viewModelScope.launch(Dispatchers.IO) {
                    registrationViewModel.loginUser(loginRequest)
                }
            } else {
                displayToast("Fields must not be empty.", context)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun displayEditListNameDialog(context: Context, mainViewModel: MainViewModel, list: com.miaekebom.mynotesapp.model.data.List) {
        val binding = EditTextDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {

            ETNewName.setText(list.title)
            BSaveChanges.setOnClickListener {
                val updatedList = com.miaekebom.mynotesapp.model.data.List(
                    list.id,
                    list.ownerId,
                    ETNewName.text.toString()
                )
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.updateList(updatedList)
                }
            }
            BCancelChanges.setOnClickListener { dialog.dismiss() }

        }
    }

    fun displayEditNoteNameDialog(context: Context, notesViewModel: NoteViewModel, note: Note) {
        val binding = EditTextDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            ETNewName.setText(note.title)
            BSaveChanges.setOnClickListener {
                val updatedNote = Note(note.id, note.ownerId, ETNewName.text.toString(), note.description)
                notesViewModel.viewModelScope.launch(Dispatchers.IO) {
                    notesViewModel.updateNote(updatedNote)
                }
            }
            BCancelChanges.setOnClickListener { dialog.dismiss() }
        }
    }

    fun displayCreateListDialog(context: Context, mainViewModel: MainViewModel) {
        val binding = AddListDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            addListDialogB.setOnClickListener {
                val listName = listNameDialogET.text
                val list = com.miaekebom.mynotesapp.model.data.List(0, SharedPref.getInstance(context).getUser().id, listName.toString())
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.addList(list)}
                dialog.dismiss()
            }
        }
    }

    fun displayEnsureDialog(context: Context, mainViewModel: MainViewModel, user: User) {
        val binding = EnsureDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            BDeleteAccount.setOnClickListener {
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.deleteUser(user)
                    withContext(Dispatchers.Main) {
                        displayRegistrationActivity(context)
                    }
                }
            }
            BDoNotDeleteAccount.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun displayNoteDescDialog(context: Context, noteViewModel: NoteViewModel, note: Note) {
        val binding = OnNoteClickDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            ETNoteDec.setText(note.description)

            BNoteDescDelete.setOnClickListener {
                val updatedNote = Note()
                updatedNote.description = ""
                updatedNote.title = note.title
                updatedNote.ownerId = note.ownerId
                updatedNote.id = note.id
                noteViewModel.viewModelScope.launch(Dispatchers.IO) {
                    noteViewModel.updateNote(updatedNote)
                }
            }
            BNoteDescSave.setOnClickListener {
                val updatedNote = Note()
                updatedNote.description = ETNoteDec.text.toString()
                updatedNote.title = note.title
                updatedNote.ownerId = note.ownerId
                updatedNote.id = note.id
                noteViewModel.viewModelScope.launch(Dispatchers.IO) {
                    noteViewModel.updateNote(updatedNote)
                }
            }

            BNoteDescCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun displayImageDialog(user: User, context: Context, userProfile: ImageButton, mainViewModel: MainViewModel) {
        val binding = ImageDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            BGenerateImage.setOnClickListener {
                ImagesManager.getImageFromApi(user, userProfile, mainViewModel)
            }
            BDeleteImage.setOnClickListener {
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.deleteUserImage(user)
                    withContext(Dispatchers.Main) {
                        userProfile.apply {
                            layoutParams.width = 50.dpToPx()
                            layoutParams.height = 50.dpToPx()
                            setPadding(10.dpToPx(), 10.dpToPx(), 0, 0)
                            setImageResource(R.drawable.profile)
                        }
                    }
                }
            }
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun displayAboutPage(context: Context) {
        val binding = AboutBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun displayRegistrationActivity(context: Context){
        val intent = Intent(context, RegistrationActivity::class.java)
        context.startActivity(intent)
    }

    private fun displayToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
