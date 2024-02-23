package com.tvisha.trooptime.activity.activity.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpcomingEvent;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.HomeActivity;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EventsAdaper extends RecyclerView.Adapter<EventsAdaper.ViewHolder> {
    String dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    String dateFormat1 = "MM";
    String dateFormat2 = "dd";
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2, Locale.US);
    String month = "", dt = "";
    private List<UpcomingEvent> items = new ArrayList<>();
    private Context context;

    public EventsAdaper(Context context, List<UpcomingEvent> items) {
        this.items = items;
        this.context = context;

    }


    @Override
    public EventsAdaper.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final EventsAdaper.ViewHolder viewHolder, final int i) {

        try {
            if(i==0 && HomeActivity.eventsItemSize==0){
                viewHolder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // Ensure you call it only once :
                        viewHolder.itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        if(context instanceof HomeActivity){
                           ((HomeActivity) context).setEventItemSize(viewHolder.itemView.getHeight());
                       }

                        // Here you can get the size :)
                    }
                });
            }


            if (i == getItemCount() - 1) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }


            if (items.get(i).getDisplay_name() != null && (!items.get(i).getDisplay_name().equals(""))) {
                viewHolder.employeeName.setText(Helper.getInstance().capitalize(items.get(i).getDisplay_name()));
            } else {
                if (items.get(i).getName() != null && (!items.get(i).getName().equals(""))) {
                    viewHolder.employeeName.setText(Helper.getInstance().capitalize(items.get(i).getName()));
                }

            }

            String date = items.get(i).getDate();
            Calendar calendar = Calendar.getInstance();
            String currentMonth = sdf1.format(calendar.getTime());
            String currentDate = sdf2.format(calendar.getTime());
            Date d = null;
            try {
                d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                month = sdf1.format(c.getTime());
                dt = sdf2.format(c.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = calendar.get(Calendar.YEAR);
            try {
                String newDate = "";
                if (Integer.parseInt(month) < Integer.parseInt(currentMonth)) {
                    newDate = getDate(String.valueOf(year + 1) + date.substring(4));
                } else {
                    try {
                        newDate = getDate(String.valueOf(year) + date.substring(4));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                if (Integer.parseInt(month) == Integer.parseInt(currentMonth) && Integer.parseInt(currentDate) == Integer.parseInt(dt)) {
                    viewHolder.date.setText("Today");
                } else {
                    try {
                        viewHolder.date.setText(newDate.substring(0, 6) + ", " + newDate.substring(7) + " ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (items.get(i).getEvent_type().equals("2")) {
                int y = items.get(i).getYears();
                if (y == 1 || y == 21 || y == 31 || y == 41 || y == 51 || y == 61 || y == 71 || y == 81 || y == 91) {
                    viewHolder.eventMessage.setText(y + "st Anniversary");
                } else if (y == 2 || y == 22 || y == 32 || y == 42 || y == 52 || y == 62 || y == 72 || y == 82 || y == 92) {
                    viewHolder.eventMessage.setText(y + "nd Anniversary");
                } else if (y == 3 || y == 23 || y == 33 || y == 43 || y == 53 || y == 63 || y == 73 || y == 83 || y == 93) {
                    viewHolder.eventMessage.setText(y + "rd Anniversary");
                } else {
                    viewHolder.eventMessage.setText(y + "th Anniversary");
                }
                viewHolder.eventImage.setImageResource(R.drawable.anniversary);
                viewHolder.eventMessage.setTextColor(context.getResources().getColor(R.color.home_orange_color));
            } else {
                viewHolder.eventImage.setImageResource(R.drawable.birthday);
                viewHolder.eventMessage.setText(Helper.getInstance().capitalize("Birthday"));
                viewHolder.eventMessage.setTextColor(context.getResources().getColor(R.color.home_sky_blue_color));
            }

            RequestOptions options = new RequestOptions().circleCropTransform()
                    .placeholder(ContextCompat.getDrawable(context,R.drawable.avatar_placeholder_light))
                    .error(ContextCompat.getDrawable(context,R.drawable.avatar_placeholder_light))
                    .priority(Priority.HIGH);


            String user_avatar = items.get(i).getUser_avatar();

            if (items.get(i).getUser_id().equals(context.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE).getString(SharePreferenceKeys.USER_ID,""))){
                context.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE).edit().putString(SharePreferenceKeys.USER_AVATAR,user_avatar).apply();
            }


            if (user_avatar != null && !user_avatar.isEmpty()) {
                Glide.with(context).load(context.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE).getString(SharePreferenceKeys.AWS_BASE_URL, "") + user_avatar)
                        .apply(options)
                        .into(viewHolder.profileImage);
            } else {
                Glide.with(context).load(R.drawable.avatar_placeholder_light)
                        .into(viewHolder.profileImage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (items.size() < 10) {
            count = items.size();
        } else {
           count = 10;
            //count = items.size();
        }

        return count;
    }

    public void setList(List<UpcomingEvent> upcomingEvents) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(upcomingEvents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            Date nextDate = c.getTime();
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage, eventImage;
        View line;
        private TextView employeeName, date, eventMessage;

        public ViewHolder(View view) {
            super(view);
            employeeName = view.findViewById(R.id.employeeName);
            date = view.findViewById(R.id.date);
            eventMessage = view.findViewById(R.id.eventMessage);
            eventImage = view.findViewById(R.id.eventImage);
            profileImage = view.findViewById(R.id.profileImage);
            line = view.findViewById(R.id.line);

        }
    }


}

