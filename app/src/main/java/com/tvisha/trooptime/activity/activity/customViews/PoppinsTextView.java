package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.tvisha.trooptime.activity.activity.Helper.Utility;


public class PoppinsTextView extends androidx.appcompat.widget.AppCompatTextView {
    public PoppinsTextView(Context context) {
        super(context);
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL);
    }

    public PoppinsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL);
    }

    public PoppinsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Utility.getTypeface(context), Typeface.NORMAL);
    }
}
