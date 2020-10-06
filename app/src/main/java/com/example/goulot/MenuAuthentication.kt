package com.example.goulot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.goulot.classes.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class MenuAuthentication: FullscreenActivity() {

    private val RC_SIGN_IN = 1
    private lateinit var mDatabase: DatabaseReference
    private lateinit var user: User
    private lateinit var editTextUsername: EditText
    private lateinit var buttonUsername: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_authentication)

        mDatabase = FirebaseDatabase.getInstance().reference
        editTextUsername = findViewById(R.id.editText_username)
        buttonUsername = findViewById(R.id.button_username)

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val userInfo = FirebaseAuth.getInstance().currentUser
                userInfo?.let {
                    checkIfUserExist(userInfo)
                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error
            }
        }
    }

    private fun checkIfUserExist(userInfo: FirebaseUser) {
        val uid = userInfo.uid
        val email = userInfo.email
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //check if user already in db
                if(dataSnapshot.value != null) {
                    user = dataSnapshot.getValue(User::class.java)!!
                    goToNextIntent()
                }
                else{
                    user = User(uid, "", email!!)
                    editTextUsername.visibility = View.VISIBLE
                    buttonUsername.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        val uidRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        uidRef.addListenerForSingleValueEvent(eventListener)
    }

    fun onClickValidateButton(view: View) {
        if(editTextUsername.text.isNotEmpty()) {
            val username = editTextUsername.text.toString()
            checkIfUsernameExist(username)
        }
        else{
            Toast.makeText(applicationContext, "Pour continuer merci de bien vouloir entrer un pseudo", Toast.LENGTH_LONG ).show()
            editTextUsername.backgroundTintList = getColorStateList(R.color.red)
        }
    }

    private fun checkIfUsernameExist(username: String) {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataValues in dataSnapshot.children) {
                    val userToCheck: User = dataValues.getValue(User::class.java)!!
                    if(userToCheck.mUsername == username){
                        Toast.makeText(applicationContext, "Ce pseudo est déjà pris", Toast.LENGTH_LONG ).show()
                        return
                    }
                }
                user.mUsername = username
                mDatabase.child("users").child(user.mUid).setValue(user)
                goToNextIntent()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        val usersRef = FirebaseDatabase.getInstance().reference.child("users")
        usersRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun goToNextIntent() {
        val nextIntent = Intent(applicationContext, MenuMain::class.java)
        nextIntent.putExtra("user", user)
        startActivity(nextIntent)
    }

}