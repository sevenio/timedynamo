package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;


public class PoppinsBoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public PoppinsBoldTextView(Context context) {
        super(context);
        // setTypeface(Utility.getTypefaceBold(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Bold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));
    }

    public PoppinsBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceBold(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Bold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));

    }

    public PoppinsBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceBold(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Bold.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));
    }
}
