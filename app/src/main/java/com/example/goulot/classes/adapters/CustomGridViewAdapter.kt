package com.example.goulot.classes.adapters

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.goulot.R
import com.example.goulot.classes.Player
import com.example.goulot.classes.PlayerState
import kotlin.math.roundToInt

class CustomGridViewAdapter(var context: Context, var arrayListPlayer: ArrayList<Player>, var displayWidth: Int, var displayHeight: Int) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val columnWidth: Int = (displayWidth * 0.5f).roundToInt()
        val columnHeight: Int = (displayHeight * 0.15f).roundToInt()
        var myView = convertView
        val holder: ViewHolder

        if (myView == null) {
            val mInflater = (context as Activity).layoutInflater
            myView = mInflater.inflate(R.layout.template_player_info, parent, false)

            holder = ViewHolder()
            holder.mConstraintLayout = myView.findViewById(R.id.constraintLayout_playerInfo)
            holder.mTextViewPlayerMoney = myView.findViewById(R.id.textView_playerMoney)
            holder.mTextViewPlayerName = myView.findViewById(R.id.textView_playerName)
            holder.mProgressBarSip = myView.findViewById(R.id.progressBar)
            holder.mImageViewPlayerIcon = myView.findViewById(R.id.player_icon)
            holder.mTextViewBackground = myView.findViewById(R.id.textView_background)

            myView.tag = holder
        } else {
            holder = myView.tag as ViewHolder
        }

        holder.mTextViewPlayerName.text = context.getString(R.string.player_name, arrayListPlayer[position].mName)
        holder.mTextViewPlayerMoney.text = context.getString(R.string.player_name, arrayListPlayer[position].mMoney.toString())
        holder.mProgressBarSip.progress = arrayListPlayer[position].mAlcoholism
        holder.mImageViewPlayerIcon.setImageResource(arrayListPlayer[position].mCharacter.mIcon)
        holder.mProgressBarSip.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, arrayListPlayer[position].mCharacter.mColor))

        if(arrayListPlayer[position].mState == PlayerState.PLAYING){
            holder.mTextViewBackground.visibility = View.VISIBLE
        } else {
            holder.mTextViewBackground.visibility = View.GONE
        }

        val params: AbsListView.LayoutParams = AbsListView.LayoutParams(columnWidth, columnHeight)
        holder.mConstraintLayout.layoutParams = params

        return myView!!
    }



    //Auto Generated Method
    override fun getItem(p0: Int): Any {
        return arrayListPlayer[p0]
    }

    //Auto Generated Method
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    //Auto Generated Method
    override fun getCount(): Int {
        return arrayListPlayer.size
    }

    //Create A class To hold over View like we taken in grid_item.xml
    class ViewHolder {
        lateinit var mConstraintLayout: ConstraintLayout
        lateinit var mTextViewPlayerName: TextView
        lateinit var mTextViewPlayerMoney: TextView
        lateinit var mProgressBarSip: ProgressBar
        lateinit var mImageViewPlayerIcon: ImageView
        lateinit var mTextViewBackground: TextView
    }

}