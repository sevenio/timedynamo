package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tvisha on 21/6/18.
 */

public class PopinRegularEditTextView extends androidx.appcompat.widget.AppCompatEditText {
    public PopinRegularEditTextView(Context context) {
        super(context);
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));
    }

    public PopinRegularEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));
    }

    public PopinRegularEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypefaceMedium(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/Poppins-Regular.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Regular.ttf"));
    }
}
