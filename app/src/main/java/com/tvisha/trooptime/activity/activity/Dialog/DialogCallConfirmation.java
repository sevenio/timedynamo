package com.tvisha.trooptime.activity.activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.R;

/**
 * Created by koti on 9/5/17.
 */

public class DialogCallConfirmation extends Dialog implements View.OnClickListener {
    Button no, yes;
    TextView message;
    String mobileNumber;

    public DialogCallConfirmation(Context context, String num) {
        super(context, R.style.common_dialog);
        mobileNumber = num;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_call_confirmation);
        message = (TextView) findViewById(R.id.message);
        message.setText(Html.fromHtml("<span>would you like to call <b>" + mobileNumber + "</b><span>"));

        no = (Button) findViewById(R.id.no);
        yes = (Button) findViewById(R.id.yes);

        no.setOnClickListener(this);
        yes.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no:
                dismiss();
                break;
            case R.id.yes:
                Helper.getInstance().call(getContext(), mobileNumber);
                dismiss();
                break;
        }
    }

}
