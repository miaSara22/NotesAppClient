package com.miaekebom.mynotesapp.model.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.runtime.saveable.autoSaver
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.databinding.*
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import com.miaekebom.mynotesapp.viewmodel.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            val user = User(2, email, fullName, null, pass1, pass2)

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

    fun displayEditNameDialog(context: Context, mainViewModel: MainViewModel, list: com.miaekebom.mynotesapp.model.data.List) {
        val binding = EditTextDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            BSaveChanges.setOnClickListener {

                ETNewListName.setText(list.title)
                BSaveChanges.setOnClickListener {
                    val updatedList = com.miaekebom.mynotesapp.model.data.List(
                        list.id,
                        list.ownerId,
                        ETNewListName.text.toString()
                    )
                    mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                        mainViewModel.updateList(list.id, updatedList)
                    }
                }
                BCancelChanges.setOnClickListener { dialog.dismiss() }
            }
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
            val listName = listNameDialogET.text

            addListDialogB.setOnClickListener {
                val list = com.miaekebom.mynotesapp.model.data.List(0, SharedPref.getInstance(context).getUser().id, listName.toString())
                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.addList(list.ownerId, list)}
                dialog.dismiss()
            }
        }
    }

    fun displayAboutPage(context: Context) {
        val binding = AboutBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    fun displayChooseImageDialog(context: Context) {
        val binding = ChooseImageDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.apply {
            BImageFromGallery.setOnClickListener {}
            BImageFromCamera.setOnClickListener {}
            BDeleteImage.setOnClickListener {}
        }
    }

    private fun displayToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
