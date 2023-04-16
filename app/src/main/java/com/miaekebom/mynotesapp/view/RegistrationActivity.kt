package com.miaekebom.mynotesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.login_dialog.*
import kotlinx.android.synthetic.main.login_dialog.view.*
import kotlinx.android.synthetic.main.register_dialog.*
import kotlinx.android.synthetic.main.register_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        displayLoginPage()
        displayRegisterPage()
    }

    private fun displayLoginPage(){
        B_to_login_page.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.login_dialog, LL_login_dialog, false)
            val dialog = AlertDialog.Builder(this)
            dialog.setView(view).create().show()
            val loginB = view.B_login

            loginB.setOnClickListener {

                val email = view.ET_email_login.text.toString()
                val pwd = view.ET_password_login.text.toString()
                val loginRequest = LoginRequest(email, pwd)
                println(loginRequest)

                if (email.isNotEmpty() && pwd.isNotEmpty()) {
                    registrationViewModel.viewModelScope.launch(Dispatchers.IO) {
                        registrationViewModel.loginUser(loginRequest)
                    }
                } else { displayToast("Fields must not be empty.")}

            }
        }
    }

    private fun displayRegisterPage(){
        B_to_register_page.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.register_dialog, LL_register_dialog, false)
            val dialog = AlertDialog.Builder(this)
            dialog.setView(view).create().show()
            val registerB = view.B_register

            registerB.setOnClickListener {

                val email = view.ET_email_register.text.toString()
                val fullName = view.ET_user_name_register.text.toString()
                val pass1 = view.ET_password_register1.text.toString()
                val pass2 = view.ET_password_register2.text.toString()

                val user = User(2,email ,fullName,null, pass1, pass2)

                if (user.email.isNotEmpty() &&
                    user.fullName.isNotEmpty() &&
                    user.pwd.isNotEmpty() &&
                    user.confirmPwd.isNotEmpty() &&
                    user.pwd == user.confirmPwd ){

                    registrationViewModel.viewModelScope.launch(Dispatchers.IO) {
                        registrationViewModel.addNewUser(user)
                    }

                } else { displayToast("All fields must not be empty. Passwords must match.")}
            }
        }
    }

    private fun displayToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}