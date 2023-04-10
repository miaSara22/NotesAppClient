package com.miaekebom.mynotesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.logging.Level
import java.util.logging.Logger
import okhttp3.MediaType.Companion.toMediaType


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        displayLoginPage()
        displayRegisterPage()
    }

//    private fun initializeComponents() {
//        //login dialog
//        val loginEmail: TextInputEditText? = ET_email_login
//        val loginPass: TextInputEditText? = ET_password_login
//        val loginButton: Button? = B_login
////        //register dialog
////        val registerEmail: EditText? = ET_email_register
////        val registerFullName: EditText? = ET_user_name_register
////        val registerPass1: EditText? = ET_password_register1
////        val registerPass2: EditText? = ET_password_register2
////        val registerButton: Button? = B_register
//    }

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



//            val email = view.findViewById<EditText>(R.id.ET_email_register).text.toString()
//            val fullName = view.ET_user_name_register.text.toString()
//            val pass1 = view.ET_password_register1.text.toString()
//            val pass2 = view.ET_password_register2.text.toString()
//            val registerB = view.B_register

            val email = "mia@gmail.com"
            val fullName = "mia"
            val pass1 = "123456"
            val pass2 = "123456"
            val registerB = view.B_register



            registerB.setOnClickListener {

                val user = User(2,email ,fullName,pass1, pass2)




                if (user.email.isNotEmpty() && user.fullName.isNotEmpty() && user.userPwd.isNotEmpty() && user.confirmUserPwd.isNotEmpty() && user.userPwd == user.confirmUserPwd ){

                    val mediaType = "application/json".toMediaType()
                    val requestBody = RequestBody.create(mediaType, Gson().toJson(user))
                    Log.d("Registration", "hi i passed")

                    registrationViewModel.addNewUser(requestBody).enqueue(object : Callback<RegisterResponse> {
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
                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) { error(t) }})

//                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                                if (t is SocketTimeoutException || t is ConnectException) {
//                                    print("Network error $t")
//                                } else if (t is HttpException) {
//                                    val response = t.response()
//                                    if (response != null && response.isSuccessful && response.body() != null) {
//                                        print("Server returned a response with a body $t")
//                                    } else {
//                                        print("Server returned a response without a body $t")
//                                    }
//                                } else {
//                                    print("other error $t")
//                                }
//                            }})


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
        Logger.getLogger(RegistrationActivity::class.java.name).log(Level.SEVERE, "Error occurred", t)
        displayToast("An error occurred. Please try again later")
    }
}