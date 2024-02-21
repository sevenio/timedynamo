package com.tvisha.trooptime.activity.activity.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.helper.Navigation
import com.tvisha.trooptime.activity.activity.helper.ToastUtil
import com.tvisha.trooptime.databinding.DialogRequestBinding

/**
 * Created by koti on 22/5/17.
 */
class RequestDialog @JvmOverloads constructor(
    context: Context,
    style: Int = R.style.common_dialog
) : Dialog(context, style),
    View.OnClickListener {

    private val binding by lazy {
        DialogRequestBinding.inflate(layoutInflater)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_request)
        binding.requestClose.setOnClickListener(this)
        binding.request.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.requestClose -> dismiss()
            R.id.request -> {
                val checkedID =
                    (findViewById<View>(R.id.requestGroup) as RadioGroup).checkedRadioButtonId
                if (checkedID == R.id.leaveRequest) {
                    Navigation.getInstance().leaveRequest(
                        context
                    )
                    dismiss()
                } else if (checkedID == R.id.permissionRequest) {
                    Navigation.getInstance().permissionRequest(
                        context
                    )
                    dismiss()
                } else if (checkedID == R.id.directClientVistRequest) {
                    Navigation.getInstance().directClientLocation(
                        context
                    )
                    dismiss()
                } else if (checkedID == R.id.compoffrequest) {
                    Navigation.getInstance().compoffRequest(
                        context
                    )
                    dismiss()
                } else if (checkedID == R.id.callManagerRequest) {
                    //ToastUtil.showToast(getContext(), "Comming Soon..!");
                    //Helper.getInstance().callConfirmation(getContext(),"9441255989");
                    Navigation.getInstance().openCallLog(
                        context
                    )
                    dismiss()
                } else {
                    ToastUtil.showToast(context, "Select request type to continue")
                }
            }
        }
    }
}