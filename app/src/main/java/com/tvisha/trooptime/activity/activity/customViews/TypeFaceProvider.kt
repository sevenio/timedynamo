package com.tvisha.trooptime.activity.activity.customViews

import android.content.Context
import android.graphics.Typeface
import java.util.*

object TypeFaceProvider {
    const val TYPEFACE_FOLDER = "font"
    const val TYPEFACE_EXTENSION = ".ttf"
    private val sTypeFaces = Hashtable<String, Typeface?>(
        4
    )

    fun getTypeFace(context: Context, fileName: String): Typeface? {
        var tempTypeface = sTypeFaces[fileName]
        if (tempTypeface == null) {
            /*String fontPath = new StringBuilder().append(fileName).toString();*/
            tempTypeface = Typeface.createFromAsset(context.assets, fileName)
            sTypeFaces[fileName] = tempTypeface
        }
        return tempTypeface
    }
}