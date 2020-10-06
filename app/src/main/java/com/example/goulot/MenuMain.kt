package com.example.goulot

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.goulot.classes.User
import com.google.firebase.database.*
import kotlin.system.exitProcess

class MenuMain : FullscreenActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_main)

        mDatabase = FirebaseDatabase.getInstance().reference
        user = intent.getSerializableExtra("user") as User
    }


    fun onClickPlayButton(view: View) {
        val nextIntent = Intent(this, MenuPlay::class.java)
        nextIntent.putExtra("user", user)
        startActivity(nextIntent)
    }

    fun onClickSettingsButton(view: View) {
    }

    fun onClickRulesButton(view: View) {
    }

    fun onClickQuitButton(view: View) {
        exitProcess(0)
    }




}