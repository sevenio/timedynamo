package com.tvisha.trooptime.activity.activity.customViews;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;
import com.tvisha.trooptime.activity.activity.model.Person;
import com.tvisha.trooptime.R;

/**
 * Created by koti on 23/5/17.
 */

public class ContactsCompletionView extends TokenCompleteTextView<Person> {
    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Person person) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.item_chip, (ViewGroup) getParent(), false);
        view.setText(person.getName());

        return view;
    }

    @Override
    protected Person defaultObject(String completionText) {
        /*int index = completionText.indexOf('@');
        if (index == -1) {
            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new Person(completionText.substring(0, index), completionText);
        }*/
        return null;
    }
    public void clear(){
        this.clear();
    }
}
