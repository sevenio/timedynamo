package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by tvisha on 21/6/18.
 */
class BioliquidTextViewRegular : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        //setTypeface(Utility.getTypefaceMedium(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //setTypeface(Utility.getTypefaceMedium(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        typeface = TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf")
    }
}