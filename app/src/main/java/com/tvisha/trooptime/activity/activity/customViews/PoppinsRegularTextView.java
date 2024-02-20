package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

public class PoppinsRegularTextView extends androidx.appcompat.widget.AppCompatTextView {
    public PoppinsRegularTextView(Context context) {
        super(context);
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));

    }

    public PoppinsRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));
    }

    public PoppinsRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));
    }
}
