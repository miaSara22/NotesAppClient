package com.miaekebom.mynotesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miaekebom.mynotesapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_notes.*

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        TV_list_name.text = intent.getStringExtra("listName")
    }
}