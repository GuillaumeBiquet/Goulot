package com.example.goulot.classes.characters

import com.example.goulot.R
import com.example.goulot.classes.CharacterStatus
import com.example.goulot.classes.CharacterName
import com.example.goulot.classes.Player
import java.util.*

class Partier : Character() {

    init {
        mClassName = CharacterName.PARTIER
        mStartingMoney = 2
        mMoneyNewTurn = 2
        mStatus = CharacterStatus.INRELATIONSHIP
        mDice = listOf(4, 4, 4, 5, 5, 5)
        mBackOfCard = "card_partier_0"
        mDeck = "deck_partier"
        mIcon = R.drawable.icon_partier
        mColor = R.color.color_partier
    }

}