package com.tvisha.trooptime.activity.activity.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.Window
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.helper.Helper
import com.tvisha.trooptime.databinding.DialogCallConfirmationBinding

/**
 * Created by koti on 9/5/17.
 */
class DialogCallConfirmation(context: Context, var mobileNumber: String) : Dialog(
    context, R.style.common_dialog
), View.OnClickListener {

    private val binding by lazy {
        DialogCallConfirmationBinding.inflate(layoutInflater)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        binding.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            message.text = Html.fromHtml("<span>would you like to call <b>$mobileNumber</b><span>")
            no.setOnClickListener(this@DialogCallConfirmation)
            yes.setOnClickListener(this@DialogCallConfirmation)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.no -> dismiss()
            R.id.yes -> {
                Helper.getInstance().call(context, mobileNumber)
                dismiss()
            }
        }
    }
}