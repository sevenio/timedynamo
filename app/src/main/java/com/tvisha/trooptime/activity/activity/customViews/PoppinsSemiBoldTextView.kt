package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;


public class PoppinsSemiBoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public PoppinsSemiBoldTextView(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceBold(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf"));
    }

    public PoppinsSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceBold(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf"));

    }

    public PoppinsSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceBold(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-SemiBold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-SemiBold.ttf"));
    }
}
