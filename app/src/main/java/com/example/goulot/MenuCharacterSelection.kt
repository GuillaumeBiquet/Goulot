package com.example.goulot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.goulot.classes.*
import com.example.goulot.classes.characters.Businessman
import com.example.goulot.classes.characters.Character
import com.example.goulot.classes.characters.Homeless
import com.example.goulot.classes.characters.Partier
import com.example.goulot.classes.characters.Trader
import com.google.firebase.database.*


class MenuCharacterSelection : FullscreenActivity() {

    private lateinit var imageButtonHomeless: ImageButton
    private lateinit var imageButtonPartier: ImageButton
    private lateinit var imageButtonBusinessman: ImageButton
    private lateinit var imageButtonTrader: ImageButton
    private lateinit var buttonReady: Button
    private lateinit var listViewPlayers: ListView
    private lateinit var playersList: ArrayList<String>
    private lateinit var textViewRoomID:  TextView

    private lateinit var mDatabase: DatabaseReference
    private lateinit var user: User
    private lateinit var game: Game
    private lateinit var gameRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_character_selection)

        imageButtonHomeless = findViewById(R.id.imageButton_homeless)
        imageButtonPartier = findViewById(R.id.imageButton_partier)
        imageButtonBusinessman = findViewById(R.id.imageButton_businessman)
        imageButtonTrader = findViewById(R.id.imageButton_trader)
        buttonReady = findViewById(R.id.button_ready)
        textViewRoomID = findViewById(R.id.editTextRoomID)
        listViewPlayers = findViewById(R.id.listView_players)

        mDatabase = FirebaseDatabase.getInstance().reference
        gameRef = mDatabase.child("games")

        user = intent.getSerializableExtra("user") as User
        game = intent.getSerializableExtra("game") as Game
        refreshGame()

        textViewRoomID.text = getString(R.string.room_id, game.mRoomId)
        playersList = arrayListOf()
    }


    private fun refreshGame() {
        var allPlayersReady = true
        val ref = FirebaseDatabase.getInstance().getReference("games").child(game.mUid)
        val menuListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                game = dataSnapshot.getValue(Game::class.java)!!
                playersList.clear()
                allPlayersReady = true
                game.mPlayers.forEach { (_, value) ->
                    playersList.add(value.mName + ", " + value.mCharacter.mClassName)
                    if(value.mState == PlayerState.WAITING){
                        allPlayersReady = false
                    }
                }
                val adapter = ArrayAdapter(this@MenuCharacterSelection, android.R.layout.simple_list_item_1, playersList)
                listViewPlayers.adapter = adapter
                if(allPlayersReady){
                    for ((_, value) in game.mPlayers) {
                        value.mState = PlayerState.WAITING
                    }
                    game.mPlayers.values.iterator().next().mState = PlayerState.PLAYING
                    game.mNumberOfTurn = 0
                    game.mPlayerPlayingUid = game.mPlayers.keys.toTypedArray()[0]
                    updateGameInDatabase()
                    ref.removeEventListener(this)

                    // start new intent
                    val nextIntent = Intent(applicationContext, GameBoard::class.java)
                    nextIntent.putExtra("game", game)
                    nextIntent.putExtra("user", user)
                    startActivity(nextIntent)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        ref.addValueEventListener(menuListener)
    }

    private fun changeCharacterInDatabase(characterChosen: CharacterName) {
        game.mPlayers[user.mUid]!!.setCharacter(characterChosen)
        updateGameInDatabase()
    }

    private fun changePlayerStateInDatabase(state: PlayerState) {
        game.mPlayers[user.mUid]!!.mState = state
        updateGameInDatabase()
    }

    private fun updateGameInDatabase() {
        gameRef.child(game.mUid).setValue(game)
    }

    fun onClickHomeless(view: View) {
        resetImageButtonCharacter()
        imageButtonHomeless.setBackgroundResource(R.drawable.character_selection_homeless_selected)
        changeCharacterInDatabase(CharacterName.HOMELESS)
    }

    fun onClickPartier(view: View) {
        resetImageButtonCharacter()
        imageButtonPartier.setBackgroundResource(R.drawable.character_selection_partier_selected)
        changeCharacterInDatabase(CharacterName.PARTIER)
    }

    fun onClickBusinessman(view: View) {
        resetImageButtonCharacter()
        imageButtonBusinessman.setBackgroundResource(R.drawable.character_selection_businessman_selected)
        changeCharacterInDatabase(CharacterName.BUSINESSMAN)
    }

    fun onClickTrader(view: View) {
        resetImageButtonCharacter()
        imageButtonTrader.setBackgroundResource(R.drawable.character_selection_trader_selected)
        changeCharacterInDatabase(CharacterName.TRADER)
    }

    private fun resetImageButtonCharacter(){
        imageButtonHomeless.setBackgroundResource(R.drawable.character_selection_homeless_unselected)
        imageButtonPartier.setBackgroundResource(R.drawable.character_selection_partier_unselected)
        imageButtonBusinessman.setBackgroundResource(R.drawable.character_selection_businessman_unselected)
        imageButtonTrader.setBackgroundResource(R.drawable.character_selection_trader_unselected)
    }

    fun onClickReadyButton(view: View) {
        if(game.mPlayers[user.mUid]!!.mCharacter.mClassName != CharacterName.NONE){
            when (game.mPlayers[user.mUid]!!.mState) {
                PlayerState.WAITING -> {
                    buttonReady.setBackgroundResource(R.color.green)
                    buttonReady.text = "Prêt"
                    changePlayerStateInDatabase(PlayerState.READY)
                }
                PlayerState.READY -> {
                    buttonReady.setBackgroundResource(R.color.red)
                    buttonReady.text = "Pas prêt"
                    changePlayerStateInDatabase(PlayerState.WAITING)
                }
                PlayerState.PLAYING -> TODO()
            }
        } else {
            Toast.makeText(applicationContext, "Pour continuer merci de bien vouloir selectionner un personnage...", Toast.LENGTH_LONG ).show()
        }
    }

}
