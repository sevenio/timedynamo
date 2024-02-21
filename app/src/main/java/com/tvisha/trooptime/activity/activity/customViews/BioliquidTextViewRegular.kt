package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tvisha on 21/6/18.
 */

public class BioliquidTextViewRegular extends androidx.appcompat.widget.AppCompatTextView {
    public BioliquidTextViewRegular(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf"));
    }

    public BioliquidTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf"));
    }

    public BioliquidTextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceMedium(context));
        //  this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/bioliquid-Regular.ttf"));
        setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/bioliquid-Regular.ttf"));
    }
}
