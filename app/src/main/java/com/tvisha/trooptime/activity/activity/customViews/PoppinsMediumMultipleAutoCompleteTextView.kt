package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import com.tvisha.trooptime.activity.activity.helper.Utility

/**
 * Created by koti on 18/4/17.
 */
class PoppinsMediumMultipleAutoCompleteTextView(context: Context?, attrs: AttributeSet?) :
    AppCompatMultiAutoCompleteTextView(
        context!!, attrs
    ) {
    init {
        this.typeface = Utility.getTypefaceMedium(context)
        setTokenizer(CommaTokenizer())
        this.threshold = 1
    }
}