package com.tvisha.trooptime.activity.activity.customViews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokenautocomplete.TokenCompleteTextView
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.model.Person

/**
 * Created by koti on 23/5/17.
 */
class ContactsCompletionView(context: Context?, attrs: AttributeSet?) :
    TokenCompleteTextView<Person?>(context, attrs) {
    override fun getViewForObject(person: Person?): View {
        val l = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = l.inflate(R.layout.item_chip, parent as ViewGroup, false) as TextView
        view.text = person!!.name
        return view
    }

    override fun defaultObject(completionText: String): Person? {
        /*int index = completionText.indexOf('@');
        if (index == -1) {
            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new Person(completionText.substring(0, index), completionText);
        }*/
        return null
    }

    fun clear() {
        this.clear()
    }
}