package com.example.goulot.classes.characters

import com.example.goulot.R
import com.example.goulot.classes.CharacterStatus
import com.example.goulot.classes.CharacterName
import com.example.goulot.classes.Player
import java.util.HashMap

class Trader : Character() {

    init {
        mClassName = CharacterName.TRADER
        mStartingMoney = 5
        mMoneyNewTurn = 3
        mStatus = CharacterStatus.NONE
        mDice = listOf(2, 2, 2, 6, 6, 6)
        mBackOfCard = "card_trader_0"
        mDeck = "deck_trader"
        mIcon = R.drawable.icon_trader
        mColor = R.color.color_trader
    }

}