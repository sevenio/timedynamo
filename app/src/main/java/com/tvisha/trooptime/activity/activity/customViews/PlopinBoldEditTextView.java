package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;


public class PlopinBoldEditTextView extends androidx.appcompat.widget.AppCompatEditText {
    public PlopinBoldEditTextView(Context context) {
        super(context);
        //setTypeface(Utility.getTypeface(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));
    }

    public PlopinBoldEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setTypeface(Utility.getTypeface(context));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));
    }

    public PlopinBoldEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setTypeface(Utility.getTypeface(context));
        // this.setTypeface(Typeface.createFromAsset(context.getAssets(),"font/poppins/poppins_bold.ttf"));
        this.setTypeface(TypeFaceProvider.getTypeFace(context, "font/poppins/Poppins-Bold.ttf"));
    }
}
