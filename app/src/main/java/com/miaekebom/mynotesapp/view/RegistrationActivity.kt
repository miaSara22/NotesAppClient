package com.miaekebom.mynotesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Level
import java.util.logging.Logger


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initializeComponents()
        displayLoginPage()
        displayRegisterPage()
    }

    private fun initializeComponents() {
        //login dialog
        val loginEmail: TextInputEditText? = ET_email_login
        val loginPass: TextInputEditText? = ET_password_login
        val loginButton: Button? = B_login
        //register dialog
        val registerEmail: TextInputEditText? = ET_email_register
        val registerFullName: TextInputEditText? = ET_user_name_register
        val registerPass1: TextInputEditText? = ET_password_register1
        val registerPass2: TextInputEditText? = ET_password_register2
        val registerButton: Button? = B_register
    }

    private fun displayLoginPage(){
        B_to_login_page.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.login_dialog, LL_login_dialog, false)
            val dialog = AlertDialog.Builder(this)
            dialog.setView(view).create().show()

            val email = view.ET_email_login.text
            val pwd = view.ET_password_login.text
            val loginB = view.B_login
            val loginRequest = LoginRequest(email.toString(), pwd.toString())

            loginB.setOnClickListener {
                if (!email.isNullOrEmpty() && !pwd.isNullOrEmpty()) {

                    val call = registrationViewModel.loginUser(loginRequest)
                    call.enqueue(object: Callback<LoginResponse>{
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                val message = response.body()
                                message?.let {
                                    val jwtToken = message.token
                                    val userEmail = message.email
                                    val userFullName = message.fullName

                                    SharedPref.getInstance(this@RegistrationActivity).setUserToken(jwtToken)
                                    displayListsActivity(userFullName)
                                }
                            } else {
                                displayToast("Login failed. Please try again later.")}
                        }
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) { error(t) } })

                } else { displayToast("Fields must not be empty.")}
            }
        }
    }

    private fun displayRegisterPage(){
        B_to_register_page.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.register_dialog, LL_register_dialog, false)
            val dialog = AlertDialog.Builder(this)
            dialog.setView(view).create().show()

            val email = view.ET_email_register.text
            val fullName = view.ET_user_name_register.text
            val pass1 = view.ET_password_register1.text
            val pass2 = view.ET_password_register2.text
            val registerB = view.B_register
            val user = User(2,email.toString() ,fullName.toString())

            registerB.setOnClickListener {

                if (!email.isNullOrEmpty() && !fullName.isNullOrEmpty() && !pass1.isNullOrEmpty()/* &&pass1 == pass2*/){

                        registrationViewModel.addNewUser(user).enqueue(object : Callback<RegisterResponse> {
                            override fun onResponse(
                                call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                                if (response.isSuccessful) {
                                    val responseBody = response.body()
                                    if (responseBody != null) {
                                        println("i'm not null")
                                        val gson = Gson()
                                        val registerResponse = gson.fromJson(responseBody.message, RegisterResponse::class.java)
                                        if (registerResponse.success) {
                                            displayToast("You Registered Successfully! please login.")
                                        } else {
                                            displayToast(registerResponse.message ?: "Registration failed. please try again later.")
                                        }
                                    } else {
                                        displayToast("Registration failed. please try again later.") }
                                }
                            }
                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) { error(t) } })

                } else { displayToast("All fields must not be empty. Passwords must match.")}
            }
        }
    }

    private fun displayListsActivity(username: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username",username)
        startActivity(intent)
    }
    fun displayToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun error(t: Throwable){
        Logger.getLogger(MainActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
        displayToast("An error occurred. Please try again later")
    }
}