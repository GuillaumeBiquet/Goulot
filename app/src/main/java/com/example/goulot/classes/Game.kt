package com.example.goulot.classes

import android.widget.Toast
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class Game constructor(uid: String, players: HashMap<String, Player>, creator: User, state: GameState) : Serializable{

    constructor() : this("", HashMap<String, Player>(), User(), GameState.JOINABLE)

    var mUid: String = ""
    var mState: GameState
    var mNumberOfTurn: Int = 0
    var mPlayerPlayingUid: String = ""
    var mRoomId: Int  = 0
    var mPlayers: HashMap<String, Player>
    var mCreator: User

    private var numberPlayerTurn: Int  = 0

    init {
        this.mUid = uid
        this.mPlayers = players
        this.mCreator = creator
        this.mState = state
        this.mRoomId = generateRandomRoomId();
        this.mNumberOfTurn  = 0
        this.mPlayerPlayingUid  = ""
    }

    private fun generateRandomRoomId(): Int {
        val m = Math.pow(10.0, 6 - 1.toDouble()).toInt()
        return m + Random().nextInt(9 * m)
    }

    fun changePlayerTurn(){
        numberPlayerTurn++
        if(numberPlayerTurn == mPlayers.keys.toTypedArray().size){
            addTurn()
            //Toast.makeText(applicationContext, "nombre de tour:" + game.mNumberOfTurn, Toast.LENGTH_LONG ).show()
        }
        mPlayerPlayingUid = mPlayers.keys.toTypedArray()[numberPlayerTurn]
    }

    fun addTurn(){
        numberPlayerTurn = 0
        mNumberOfTurn++
    }

}
