package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PoppinsSemiBoldTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        //setTypeface(Utility.getTypefaceBold(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        //setTypeface(Utility.getTypefaceBold(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //setTypeface(Utility.getTypefaceBold(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf")
    }
}