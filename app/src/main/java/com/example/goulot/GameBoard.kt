package com.example.goulot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.goulot.classes.adapters.CustomGridViewAdapter
import com.example.goulot.classes.Game
import com.example.goulot.classes.Player
import com.example.goulot.classes.PlayerState
import com.example.goulot.classes.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.template_draw_card.*
import kotlinx.android.synthetic.main.template_play_turn.*
import kotlin.math.roundToInt


class GameBoard : FullscreenActivity() {

    private lateinit var user: User
    private lateinit var game: Game

    private lateinit var gameRef: DatabaseReference
    private lateinit var mDatabase: DatabaseReference

    private lateinit var customGridAdapter: CustomGridViewAdapter
    private lateinit var bitmapDeck: Bitmap

    private lateinit var animationDrawCard: LottieAnimationView
    private lateinit var animationShowDeck: LottieAnimationView
    private lateinit var animationThrowDice: LottieAnimationView

    private lateinit var constraintPlayTurn: ConstraintLayout
    private lateinit var constraintLayoutDrawCard: ConstraintLayout
    private lateinit var linearLayoutDiceResult: LinearLayout

    private lateinit var textViewDiceResult: TextView
    private lateinit var textViewCardMoney: TextView
    private lateinit var textViewCardSip: TextView
    private lateinit var gridView: GridView
    private lateinit var playTurnButton: Button

    private var cardSip: Int = 0
    private var cardMoney: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameboard)

        user = intent.getSerializableExtra("user") as User
        game = intent.getSerializableExtra("game") as Game
        mDatabase = FirebaseDatabase.getInstance().reference
        gameRef = mDatabase.child("games")

        animationDrawCard = findViewById(R.id.animation_drawCard)
        animationShowDeck = findViewById(R.id.animation_show_deck)
        animationThrowDice = findViewById(R.id.animation_throw_dice)

        constraintPlayTurn = findViewById(R.id.constraintLayout_play_turn)
        constraintLayoutDrawCard = findViewById(R.id.constraintLayout_draw_card)
        constraintLayoutDrawCard = findViewById(R.id.constraintLayout_draw_card)
        linearLayoutDiceResult = findViewById(R.id.linearLayout_dice_result)

        gridView = findViewById(R.id.gridView_players)
        playTurnButton = findViewById(R.id.button_playTurn)

        textViewDiceResult = findViewById(R.id.textView_diceResult)
        textViewCardMoney = findViewById(R.id.textView_cardMoney)
        textViewCardSip = findViewById(R.id.textView_cardSip)

        textViewCardMoney.text = getString(R.string.count, 0)
        textViewCardSip.text = getString(R.string.count, 0)


        initGameBoard()
        setListenerOnGame()
    }

    private fun initGameBoard() {
        val deck = game.mPlayers[user.mUid]!!.mCharacter.mDeck
        bitmapDeck = getBitmapOfString("drawable/${deck}")
        updateGridView()
    }

    private fun updateGridView(){
        val arrayListPlayer = ArrayList<Player>()
        for ((_, value) in game.mPlayers) {
            arrayListPlayer.add(value)
        }
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val displayWidth = metrics.widthPixels
        val displayHeight = metrics.heightPixels
        customGridAdapter = CustomGridViewAdapter(this, arrayListPlayer, displayWidth, displayHeight)
        gridView.adapter = customGridAdapter
    }

    private fun playTurn() {
        ShowDice()
        playTurnButton.visibility = View.GONE
    }

    //------------------------------ dice ------------------------------

    private fun ShowDice() {
        constraintPlayTurn.visibility = View.VISIBLE
        animationThrowDice.visibility = View.VISIBLE
        animationThrowDice.playAnimation()
    }

    fun throwDice(view: View) {
        val sipArray: List<Int> = game.mPlayers[user.mUid]!!.mCharacter.mDice
        val sipToDrink = sipArray.random()
        game.mPlayers[user.mUid]!!.addAlcoholism(sipToDrink)
        textView_diceResult.text = getString(R.string.sip_to_drink, sipToDrink)
        linearLayoutDiceResult.visibility = View.VISIBLE
        animationThrowDice.visibility = View.GONE
    }

    fun onClickOkDiceButton(view: View) {
        updateGameInDatabase()
        linearLayoutDiceResult.visibility = View.GONE
        showDeck()
    }

    //------------------------------ card ------------------------------

    private fun showDeck(){
        animationShowDeck.updateBitmap("image_0", bitmapDeck.resizeByWidth(1056))
        animationShowDeck.visibility = View.VISIBLE
        animationShowDeck.playAnimation()
    }

    fun drawCard(view: View) {
        val cardToDraw = (1..20).random()
        val backOfCard = game.mPlayers[user.mUid]!!.mCharacter.mBackOfCard
        val card = game.mPlayers[user.mUid]!!.mCharacter.getCard(cardToDraw)

        val bitmapBackCard = getBitmapOfString("drawable/$backOfCard")
        val bitmapFrontCard = getBitmapOfString("drawable/$card")
        animationDrawCard.updateBitmap("image_0", bitmapBackCard.resizeByWidth(1000))
        animationDrawCard.updateBitmap("image_1", bitmapFrontCard.resizeByWidth(1000))
        animationDrawCard.updateBitmap("image_2", bitmapDeck.resizeByWidth(1056))

        //animationShowDeck.visibility = View.VISIBLE
        constraintLayoutDrawCard.visibility = View.VISIBLE
        animationDrawCard.playAnimation()
    }

    fun closeDrawCardAnimation(view: View) {
        animationShowDeck.visibility = View.GONE
        constraintPlayTurn.visibility = View.GONE
        constraintLayoutDrawCard.visibility = View.GONE

        game.mPlayers[user.mUid]!!.addAlcoholism(cardSip)
        game.mPlayers[user.mUid]!!.addMoney(cardMoney)
        cardMoney = 0
        cardSip = 0
        textViewCardSip.text = getString(R.string.count, cardSip)
        textView_cardMoney.text = getString(R.string.count, cardMoney)

        endTurn()
    }

    //------------------------------------------------------------------

    private fun updateGameInDatabase() {
        gameRef.child(game.mUid).setValue(game)
    }

    private fun setListenerOnGame() {
        val ref = FirebaseDatabase.getInstance().getReference("games").child(game.mUid)
        val menuListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                game = dataSnapshot.getValue(Game::class.java)!!
                updateGridView()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addValueEventListener(menuListener)
    }

    private fun endTurn() {
        game.mPlayers[game.mPlayerPlayingUid]!!.mState = PlayerState.WAITING
        game.changePlayerTurn()
        game.mPlayers[game.mPlayerPlayingUid]!!.mState = PlayerState.PLAYING
        updateGameInDatabase()
        playTurnButton.visibility = View.VISIBLE
    }

    private fun getBitmapOfString(str: String): Bitmap {
        val drawable = resources.getIdentifier(str, null, packageName)
        return BitmapFactory.decodeResource(resources, drawable)
    }

    // Extension function to resize bitmap using new width value by keeping aspect ratio
    private fun Bitmap.resizeByWidth(width:Int):Bitmap{
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val height:Int = (width / ratio).roundToInt()
        return Bitmap.createScaledBitmap(this, width, height, false)
    }

    fun onClickAddMoneyButton(view: View) {
        cardMoney++
        textViewCardMoney.text = getString(R.string.count, cardMoney)
    }

    fun onClickMinusMoneyButton(view: View) {
        cardMoney--
        textViewCardMoney.text = getString(R.string.count, cardMoney)
    }

    fun onClickAddSipButton(view: View) {
        cardSip++
        textViewCardSip.text = getString(R.string.count, cardSip)
    }

    fun onClickMinusSipButton(view: View) {
        cardSip--
        textViewCardSip.text = getString(R.string.count, cardSip)
    }

    fun onClickPlayTurnButton(view: View) {
        if(game.mPlayers[user.mUid]!!.mState == PlayerState.PLAYING){
            playTurn()
        } else {
            Toast.makeText(applicationContext, "C'est au tour de: " + game.mPlayers[game.mPlayerPlayingUid]!!.mName, Toast.LENGTH_LONG ).show()
        }
    }

    /*private fun getId(resourceName: String, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resourceName)
            idField.getInt(idField)
        } catch (e: Exception) {
            throw RuntimeException("No resource ID found for: $resourceName / $c", e)
        }
    }*/
}
