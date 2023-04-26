package com.miaekebom.mynotesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.miaekebom.mynotesapp.databinding.ActivityRegistrationBinding
import com.miaekebom.mynotesapp.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BToLoginPage.setOnClickListener {
            DialogsManager.displayLoginDialog(this, registrationViewModel) }

        binding.BToRegisterPage.setOnClickListener {
            DialogsManager.displayRegisterDialog(this, registrationViewModel) }
    }
}