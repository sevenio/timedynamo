package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class PoppinsMediumTextView extends androidx.appcompat.widget.AppCompatTextView {
    public PoppinsMediumTextView(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Medium.ttf"));
    }

    public PoppinsMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Medium.ttf"));
    }

    public PoppinsMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceMedium(context), Typeface.NORMAL);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Medium.ttf"));
    }
}
