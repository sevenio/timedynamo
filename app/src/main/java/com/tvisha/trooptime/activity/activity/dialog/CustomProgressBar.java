package com.tvisha.trooptime.activity.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ldoublem.loadingviewlib.LVCircularCD;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.tvisha.trooptime.R;


public class CustomProgressBar extends Dialog {

    boolean isCancel;
    ImageView imageView;
    Context context;
    LVCircularCD lvCircularCD;
    LVCircularRing lvCircularRing;

    public CustomProgressBar(Context context) {
        super(context, R.style.common_dialog_progrss);
        this.context = context;
    }

    public CustomProgressBar(Context context, boolean isCancel) {
        super(context, R.style.common_dialog_progrss);
        this.isCancel = isCancel;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_progress_bar);
        imageView = findViewById(R.id.image);


        Glide.with(context)
                .load(R.drawable.progressbar_rotate)
                .into(imageView);

        int colorAccent = context.getResources().getColor(R.color.colorAccent);
        int colorPrimaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
        int colorPrimary = context.getResources().getColor(R.color.colorPrimary);

        lvCircularCD = findViewById(R.id.lvCircularCD);
        lvCircularCD.setViewColor(colorAccent);
        lvCircularCD.startAnim();

        lvCircularRing = findViewById(R.id.lvCircularRing);
        lvCircularRing.setBarColor(colorAccent);
        lvCircularRing.startAnim();

        setCancelable(isCancel);
    }

}
