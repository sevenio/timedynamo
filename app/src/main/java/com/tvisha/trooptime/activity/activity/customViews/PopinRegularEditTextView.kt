package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by tvisha on 21/6/18.
 */
class PopinRegularEditTextView : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf")
    }
}