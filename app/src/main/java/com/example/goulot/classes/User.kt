package com.example.goulot.classes

import java.io.Serializable

class User constructor(uid: String, username: String, email: String) : Serializable{

    constructor() : this("", "", "")

    var mUid: String = ""
    var mUsername: String = ""
    var mEmail: String = ""

    init {
        this.mUid = uid
        this.mUsername = username
        this.mEmail = email
    }

}