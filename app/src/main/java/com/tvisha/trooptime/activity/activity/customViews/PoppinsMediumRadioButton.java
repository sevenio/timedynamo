package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

public class PoppinsMediumRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    public PoppinsMediumRadioButton(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf"));
    }

    public PoppinsMediumRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf"));
    }

    public PoppinsMediumRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Light.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Light.ttf"));
    }
}
