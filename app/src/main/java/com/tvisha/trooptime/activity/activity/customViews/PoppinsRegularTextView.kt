package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PoppinsRegularTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }
}