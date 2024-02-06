package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tvisha on 1/10/18.
 */

public class NunitoSemoBoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public NunitoSemoBoldTextView(Context context) {
        super(context);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf"));
    }

    public NunitoSemoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf"));
    }

    public NunitoSemoBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Nunito_SemiBold.ttf"));
        //setTypeface(Utility.getTypefaceRegular(context), Typeface.NORMAL);
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "Nunito_SemiBold.ttf"));
    }
}
