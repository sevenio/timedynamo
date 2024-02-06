package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;


public class PopinMediumEditTextView extends androidx.appcompat.widget.AppCompatEditText {
    public PopinMediumEditTextView(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }

    public PopinMediumEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }

    public PopinMediumEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceMedium(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }
}
