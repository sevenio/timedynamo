package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PoppinsMediumTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        typeface = Typeface.createFromAsset(context.assets, "font/poppins/Poppins-Medium.ttf")
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        typeface = Typeface.createFromAsset(context.assets, "font/poppins/Poppins-Medium.ttf")
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        typeface = Typeface.createFromAsset(context.assets, "font/poppins/Poppins-Medium.ttf")
    }
}