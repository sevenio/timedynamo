package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.tvisha.trooptime.activity.activity.helper.Utility

class PoppinsTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL)
    }
}