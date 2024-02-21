package com.tvisha.trooptime.activity.activity.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ldoublem.loadingviewlib.LVCircularCD
import com.ldoublem.loadingviewlib.view.LVCircularRing
import com.tvisha.trooptime.R
import com.tvisha.trooptime.databinding.CustomProgressBarBinding

class CustomProgressBar @JvmOverloads constructor(context: Context, style: Int = R.style.common_dialog_progrss) :
    Dialog(context, style) {

    private val binding by lazy {
        CustomProgressBarBinding.inflate(layoutInflater)
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        binding.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(root)
            Glide.with(context)
                .load(R.drawable.progressbar_rotate)
                .into(binding.image)
            val colorAccent = ContextCompat.getColor(context, R.color.colorAccent)

            lvCircularCD.setViewColor(colorAccent)
            lvCircularCD.startAnim()
            lvCircularRing.setBarColor(colorAccent)
            lvCircularRing.startAnim()
        }
        setCancelable(false)
    }
}