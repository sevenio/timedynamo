package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PlopinBoldEditTextView : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {
        //setTypeface(Utility.getTypeface(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        //setTypeface(Utility.getTypeface(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //setTypeface(Utility.getTypeface(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf")
    }
}