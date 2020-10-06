package com.example.goulot.classes.characters

import com.example.goulot.R
import com.example.goulot.classes.CharacterStatus
import com.example.goulot.classes.CharacterName
import com.example.goulot.classes.Player
import com.example.goulot.classes.PlayerState
import java.util.HashMap

class Businessman : Character() {

    init {
        mClassName = CharacterName.BUSINESSMAN
        mStartingMoney = 9
        mMoneyNewTurn = 4
        mStatus = CharacterStatus.EMPLOYEE
        mDice = listOf(2, 2, 2, 4, 4, 4)
        mBackOfCard = "card_bm_0"
        mDeck = "deck_bm"
        mIcon = R.drawable.icon_businessman
        mColor = R.color.color_businessman
    }

}