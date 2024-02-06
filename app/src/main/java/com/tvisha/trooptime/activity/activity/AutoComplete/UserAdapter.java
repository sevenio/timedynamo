package com.tvisha.trooptime.activity.activity.AutoComplete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserAdapter extends ArrayAdapter<User> implements Filterable {
    ArrayList<User> users, tempUser, suggestions;
    UserAdapterListener listener;
    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            User user = (User) resultValue;
            return user.getName();
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (final User user : tempUser) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                /*if (user.getName().toLowerCase().contains(constraint.toString().toLowerCase())
                                        || user.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())
                                        ) {
                                    suggestions.add(user);
                                }*/

                                if ((user.getName() != null && stringMatchWithFirstWordofString(constraint.toString().toLowerCase(), user.getName())) ||
                                        (user.getEmail() != null && stringMatchWithFirstWordofString(constraint.toString().toLowerCase(), user.getEmail()))) {
                                    suggestions.add(user);
                                }
                            } catch (Exception e) {

                            }

                        }
                    }).run();
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<User> c = (ArrayList<User>) results.values;
            if (results != null && results.count > 0) {
                clear();
                try {
                    addAll(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

    public UserAdapter(Context context, ArrayList<User> users, UserAdapterListener listener) {
        super(context, android.R.layout.simple_list_item_1, users);
        this.users = users;
        this.tempUser = new ArrayList<User>(users);
        this.suggestions = new ArrayList<User>(users);
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_autocomplete_row, parent, false);
        }


        TextView userName = (TextView) convertView.findViewById(R.id.userName);


        if (userName != null && user != null) {
            userName.setText(user.getName());
        }

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onResult(user);
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user1 = getItem(position);
                clear();
                notifyDataSetChanged();
                listener.onResult(user1);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    public boolean stringMatchWithFirstWordofString(String search, String name) {
        String regex = "(?i)\\b" + search + ".*?\\b";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return m.find();

    }

    public interface UserAdapterListener {
        public void onResult(User user);
    }
}

