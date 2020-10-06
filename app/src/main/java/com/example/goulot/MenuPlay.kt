package com.example.goulot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.goulot.classes.*
import com.example.goulot.classes.characters.Homeless
import com.google.firebase.database.*


class MenuPlay : FullscreenActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var user: User
    private lateinit var player: Player
    private lateinit var games: MutableList<Game>
    private lateinit var game: Game

    private lateinit var editTextRoomID: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_play)

        user = intent.getSerializableExtra("user") as User
        player = Player(user.mUid, user.mUsername)
        mDatabase = FirebaseDatabase.getInstance().reference
        editTextRoomID = findViewById(R.id.editText_roomId)
        getGamesAvailable()
    }

    fun onClickCreateRoomButton(view: View) {
        val gameRef = mDatabase.child("games")
        val players :HashMap<String,Player> = HashMap()
        players[player.mUid] = player

        /*val p1 = Player("playerUid1", "Thomas")
        p1.setCharacter(CharacterName.BUSINESSMAN)
        p1.mState = PlayerState.READY
        players["playerUid1"] = p1

        val p2 = Player("playerUid2", "Emilie")
        p2.setCharacter(CharacterName.HOMELESS)
        p2.mState = PlayerState.READY
        players["playerUid2"] = p2

        val p3 = Player("playerUid3", "Jean")
        p3.setCharacter(CharacterName.PARTIER)
        p3.mState = PlayerState.READY
        PlayerState.PLAYING
        players["playerUid3"] = p3

        val p4 = Player("playerUid4", "Alice")
        p4.setCharacter(CharacterName.TRADER)
        p4.mState = PlayerState.READY
        players["playerUid4"] = p4*/

        // Game
        val gameKey = gameRef.push().key.toString()
        game = Game(gameKey, players, user, GameState.JOINABLE)
        gameRef.child(gameKey).setValue(game)

        startNewActivity()
    }

    fun onClickJoinRoomButton(view: View) {
        val roomID: Int
        try{
            roomID = Integer.parseInt(editTextRoomID.text.toString())
        } catch(ex: NumberFormatException){ // handle your exception
            Toast.makeText(applicationContext, "L'ID entré n'est pas valide...", Toast.LENGTH_LONG).show()
            return
        }

        game = getGame(roomID) ?: return

        if(checkIfGameNotFull(game)){
            //add player to game
            player = Player(user.mUid, user.mUsername)
            game.mPlayers[player.mUid] = player
            val gameRef = mDatabase.child("games")
            gameRef.child(game.mUid).setValue(game)

            //Inform player
            Toast.makeText(applicationContext, "Vous rejoignez la partie de: " + game.mCreator.mUsername, Toast.LENGTH_LONG).show()
            startNewActivity()
        }
    }

    private fun getGamesAvailable() {
        val menuListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                games = mutableListOf()
                for (dataValues in dataSnapshot.children) {
                    val gameToAddToList: Game? = dataValues.getValue(Game::class.java)
                    games.add(gameToAddToList!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        val ref = FirebaseDatabase.getInstance().getReference("games")
        ref.addValueEventListener(menuListener)
    }

    private fun getGame(id: Int): Game? {
        for(game in games){
            if(id == game.mRoomId){
                return game
            }
        }
        Toast.makeText(applicationContext, "L'ID entré n'est pas valide...", Toast.LENGTH_LONG ).show()
        return null
    }

    private fun checkIfGameNotFull(game: Game): Boolean {
        if(game.mPlayers.size > 7){
            Toast.makeText(applicationContext, "8 joueurs sont déjà dans la partie...", Toast.LENGTH_LONG ).show()
            return false
        }
        return true
    }

    private fun startNewActivity(){
        //send game to new intent
        val nextIntent = Intent(this, MenuCharacterSelection::class.java)
        nextIntent.putExtra("game", game)
        nextIntent.putExtra("user", user)
        startActivity(nextIntent)
    }

}
