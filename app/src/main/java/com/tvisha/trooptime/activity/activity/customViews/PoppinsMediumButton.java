package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

public class PoppinsMediumButton extends androidx.appcompat.widget.AppCompatButton {
    public PoppinsMediumButton(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }

    public PoppinsMediumButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }

    public PoppinsMediumButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Medium.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Medium.ttf"));
    }
}
