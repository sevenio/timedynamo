package com.tvisha.trooptime.activity.activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.UpcomingHolidy;
import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.activity.activity.HomeActivity;
import com.tvisha.trooptime.activity.activity.LeaveRequestActivity;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.ViewHolder> {
    String dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    String dateFormat1 = "MM";
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    String dateFormat2 = "yy";
    String dateFormat3 = "dd";
    SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2, Locale.US);
    SimpleDateFormat sdf3 = new SimpleDateFormat(dateFormat3, Locale.US);
    String dayOfWeek = "";
    String month = "", dt = "";
    String cyear = "";
    private List<UpcomingHolidy> items = new ArrayList<>();
    private Context context;

    public HolidaysAdapter(Context context, List<UpcomingHolidy> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public HolidaysAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holidays_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final HolidaysAdapter.ViewHolder viewHolder, final int i) {

        try {

            if(i==0 && HomeActivity.holdaysItemSize==0){
                viewHolder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // Ensure you call it only once :
                        viewHolder.itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        if(context instanceof HomeActivity){
                            ((HomeActivity) context).setHolidaysItemSize(viewHolder.itemView.getHeight());
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

            viewHolder.holidayName.setText(Helper.getInstance().capitalize(items.get(i).getName()));
            String date = items.get(i).getDate();
            Calendar calendar = Calendar.getInstance();
            String currentMonth = sdf1.format(calendar.getTime());
            String currentYear = sdf2.format(calendar.getTime());
            String currentDate = sdf3.format(calendar.getTime());
            Date d = null;
            try {
                d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                month = sdf1.format(c.getTime());
                cyear = sdf2.format(c.getTime());
                dt = sdf3.format(c.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int year = calendar.get(Calendar.YEAR);
            try {
                String newDate;
                if (Integer.parseInt(month) >= Integer.parseInt(currentMonth) && Integer.parseInt(currentMonth) <= 12 &&
                        Integer.parseInt(cyear) == Integer.parseInt(currentYear)) {
                    newDate = getDate(String.valueOf(year) + date.substring(4));
                } else {
                    newDate = getDate(String.valueOf(year + 1) + date.substring(4));
                }

                if (Integer.parseInt(month) == Integer.parseInt(currentMonth) && Integer.parseInt(currentDate) == Integer.parseInt(dt)) {
                    viewHolder.date.setText("Today");
                } else {
                    viewHolder.date.setText(dayOfWeek + " " + newDate.substring(0, 6) + ", " + newDate.substring(7) + " ");
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (items.get(i).getType().equals("1")) {
                viewHolder.holidayType.setText(Helper.getInstance().capitalize("confirm"));
                viewHolder.holidayType.setTextColor(context.getResources().getColor(R.color.home_green_color));
            } else {
                viewHolder.holidayType.setText(Helper.getInstance().capitalize("Optional"));
                viewHolder.holidayType.setTextColor(context.getResources().getColor(R.color.home_sky_blue_color));
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!items.get(i).getType().equals("1")) {
                        Intent intent = new Intent(context, LeaveRequestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("date", items.get(i).getDate());
                        context.startActivity(intent);


                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (items.size() < 3) {
            count = items.size();
        } else {
            count = 3;
            //count=items.size();
        }

        return count;
    }

    public void setList(List<UpcomingHolidy> upcomingHolidies) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(upcomingHolidies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView holidayName, date, holidayType;
        private View line;

        public ViewHolder(View view) {
            super(view);

            holidayName = view.findViewById(R.id.holidayName);
            date = view.findViewById(R.id.date);
            holidayType = view.findViewById(R.id.holidayType);
            line = view.findViewById(R.id.line);


        }
    }


}

