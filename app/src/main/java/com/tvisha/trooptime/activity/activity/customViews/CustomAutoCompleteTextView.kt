package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class CustomAutoCompleteTextView : AppCompatAutoCompleteTextView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing) {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputManager.hideSoftInputFromWindow(
                    findFocus().windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            ) {
                return true
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }
}