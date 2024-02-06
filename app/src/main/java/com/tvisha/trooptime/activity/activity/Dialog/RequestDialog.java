package com.tvisha.trooptime.activity.activity.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.ToastUtil;
import com.tvisha.trooptime.R;

/**
 * Created by koti on 22/5/17.
 */

public class RequestDialog extends android.app.Dialog implements View.OnClickListener {

    ImageButton requestClose;
    TextView request;

    public RequestDialog(Context context) {
        super(context, R.style.common_dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_request);
        requestClose = (ImageButton) findViewById(R.id.requestClose);
        request = (TextView) findViewById(R.id.request);
        requestClose.setOnClickListener(this);
        request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.requestClose:
                dismiss();
                break;
            case R.id.request:
                int checkedID = ((RadioGroup) findViewById(R.id.requestGroup)).getCheckedRadioButtonId();
                if (checkedID == R.id.leaveRequest) {
                    Navigation.getInstance().leaveRequest(getContext());
                    dismiss();
                } else if (checkedID == R.id.permissionRequest) {
                    Navigation.getInstance().permissionRequest(getContext());
                    dismiss();
                } else if (checkedID == R.id.directClientVistRequest) {
                    Navigation.getInstance().directClientLocation(getContext());
                    dismiss();
                } else if (checkedID == R.id.compoffrequest) {
                    Navigation.getInstance().compoffRequest(getContext());
                    dismiss();
                } else if (checkedID == R.id.callManagerRequest) {
                    //ToastUtil.showToast(getContext(), "Comming Soon..!");
                    //Helper.getInstance().callConfirmation(getContext(),"9441255989");
                    Navigation.getInstance().openCallLog(getContext());
                    dismiss();
                } else {
                    ToastUtil.showToast(getContext(), "Select request type to continue");
                }
                break;
        }
    }
}
