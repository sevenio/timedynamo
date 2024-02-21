package com.tvisha.trooptime.activity.activity.customViews;

import android.content.Context;
import android.util.AttributeSet;

import com.tvisha.trooptime.activity.activity.helper.Utility;

/**
 * Created by koti on 18/4/17.
 */

public class PoppinsMediumMultipleAutoCompleteTextView extends androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView {
    public PoppinsMediumMultipleAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utility.getTypefaceMedium(context));
        this.setTokenizer(new CommaTokenizer());
        this.setThreshold(1);
    }
}
