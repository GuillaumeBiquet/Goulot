package com.example.goulot.classes

import com.example.goulot.R
import com.example.goulot.classes.characters.Businessman
import com.example.goulot.classes.characters.Character
import com.example.goulot.classes.characters.Homeless
import com.example.goulot.classes.characters.Partier
import com.example.goulot.classes.characters.Trader
import java.io.Serializable

class Player constructor(uid: String, name: String): Serializable{

    constructor(): this("", "")

    var mUid: String = ""
    var mName: String = ""
    var mMoney = 0
    var mAlcoholism = 0
    var mState: PlayerState = PlayerState.WAITING
    var mCharacter: Character = Character()

    init {
        mUid = uid
        mName = name
    }

    fun setCharacter(characterName: CharacterName) {
        mCharacter = when(characterName){
            CharacterName.PARTIER -> Partier()
            CharacterName.BUSINESSMAN -> Businessman()
            CharacterName.TRADER -> Trader()
            CharacterName.HOMELESS -> Homeless()
            CharacterName.NONE -> Character()
        }
        mMoney = mCharacter.mStartingMoney
    }

    fun addMoneyNewTurn() {
        mMoney += mCharacter.mMoneyNewTurn
    }

    fun addMoney(moneyToAdd: Int) {
        mMoney += moneyToAdd
    }

    fun multiplyMoney(factor: Int) {
        mMoney *= factor
    }

    fun addAlcoholism(alcoholismToAdd: Int) {
        mAlcoholism += alcoholismToAdd
    }

    fun multiplyAlcoholism(factor: Int) {
        mAlcoholism *= factor
    }
}