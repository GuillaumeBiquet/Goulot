package com.example.goulot.classes.characters

import com.example.goulot.classes.CharacterStatus
import com.example.goulot.classes.CharacterName
import java.io.Serializable

open class Character: Serializable {

    var mClassName: CharacterName = CharacterName.NONE
    var mStatus: CharacterStatus = CharacterStatus.NONE
    var mDice: List<Int> = listOf()
    var mStartingMoney = 0
    var mMoneyNewTurn = 0
    var mBackOfCard: String = ""
    var mDeck: String = ""
    var mIcon: Int = 0
    var mColor: Int = 0

    fun getCard(cardNumber: Int): String {
        return "card_${getCharacterName()}_$cardNumber"
    }

    private fun getCharacterName(): String {
        return when (mClassName) {
            CharacterName.PARTIER -> "partier"
            CharacterName.HOMELESS -> "homeless"
            CharacterName.BUSINESSMAN -> "bm"
            CharacterName.TRADER -> "trader"
            CharacterName.NONE -> ""
        }
    }



}