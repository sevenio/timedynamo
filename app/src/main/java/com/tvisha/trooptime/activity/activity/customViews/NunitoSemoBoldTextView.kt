package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by tvisha on 1/10/18.
 */
class NunitoSemoBoldTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        typeface = TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        typeface = TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        typeface = TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf")
    }
}