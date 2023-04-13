package com.miaekebom.mynotesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.LoginResponse
import com.miaekebom.mynotesapp.model.data.RegisterResponse
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.model.utils.SharedPref
import com.miaekebom.mynotesapp.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.login_dialog.*
import kotlinx.android.synthetic.main.login_dialog.view.*
import kotlinx.android.synthetic.main.register_dialog.*
import kotlinx.android.synthetic.main.register_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.logging.Level
import java.util.logging.Logger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


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
                    login(loginRequest)
                } else { displayToast("Fields must not be empty.")}

            }
        }
    }

    private fun login(loginRequest: LoginRequest) {

        registrationViewModel.viewModelScope.launch(Dispatchers.IO) {
            registrationViewModel.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()
                        message?.let {
                            val jwtToken = message.token
                            val userEmail = message.email
                            val userFullName = message.fullName

                            runOnUiThread {
                                SharedPref.getInstance(this@RegistrationActivity).setUserToken(jwtToken)
                                displayListsActivity(userFullName)
                            }
                        }
                    } else {
                            displayToast("Login failed. Please try again later.") }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    error(t)
                }
            })
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

                val user = User(2,email ,fullName,"", pass1, pass2)

                if (user.email.isNotEmpty() &&
                    user.fullName.isNotEmpty() &&
                    user.pwd.isNotEmpty() &&
                    user.confirmPwd.isNotEmpty() &&
                    user.pwd == user.confirmPwd ){
                    register(user)

                } else { displayToast("All fields must not be empty. Passwords must match.")}
            }
        }
    }

    private fun register(user: User) {

        registrationViewModel.viewModelScope.launch(Dispatchers.IO) {

//            val mediaType = "application/json".toMediaType()
//            val requestBody = Gson().toJson(user).toRequestBody(mediaType)

            registrationViewModel.addNewUser(user).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>, response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val gson = Gson()
                                val registerResponse = gson.fromJson(
                                    responseBody.message,
                                    RegisterResponse::class.java)

                                    if (registerResponse.success) {
                                        displayToast("You Registered Successfully! please login.")

                                    } else {
                                        displayToast(registerResponse.message ?: "Registration failed. please try again later.")
                                }
                            } else {
                                displayToast("Registration failed. please try again later.")
                            }
                        }
                    }
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        error(t)
                    }
                })
        }
    }

    private fun displayListsActivity(username: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username",username)
        startActivity(intent)
    }
    fun displayToast(text: String){
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }

    fun error(t: Throwable){
        Logger.getLogger(RegistrationActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
        displayToast("An error occurred. Please try again later")
    }
}