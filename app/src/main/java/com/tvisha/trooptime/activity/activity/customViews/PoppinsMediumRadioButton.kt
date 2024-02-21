package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class PoppinsMediumRadioButton : AppCompatRadioButton {
    constructor(context: Context) : super(context) {
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf")
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf")
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf")
    }
}