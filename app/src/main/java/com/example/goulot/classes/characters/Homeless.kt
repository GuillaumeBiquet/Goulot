package com.example.goulot.classes.characters

import com.example.goulot.R
import com.example.goulot.classes.CharacterStatus
import com.example.goulot.classes.CharacterName

class Homeless : Character() {

    init {
        mClassName = CharacterName.HOMELESS
        mStartingMoney = 0
        mMoneyNewTurn = 1
        mStatus = CharacterStatus.NONE
        mDice = listOf(1, 1, 1, 3, 3, 3)
        mBackOfCard = "card_homeless_0"
        mDeck = "deck_homeless"
        mIcon = R.drawable.icon_homeless
        mColor = R.color.color_homeless
    }

}