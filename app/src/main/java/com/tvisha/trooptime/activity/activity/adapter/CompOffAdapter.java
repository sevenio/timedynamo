package com.tvisha.trooptime.activity.activity.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.apiPostModels.CompOffDetails;
import com.tvisha.trooptime.activity.activity.AttendanceFragment;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CompOffAdapter extends RecyclerView.Adapter<CompOffAdapter.ViewHolder> {
    String dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    AttendanceFragment attendanceFragment;
    String dayName = "";
    private List<CompOffDetails> items = new ArrayList<>();
    private Context context;

    public CompOffAdapter(Context context, List<CompOffDetails> items, AttendanceFragment attendanceFragment) {
        this.items = items;
        this.context = context;
        this.attendanceFragment = attendanceFragment;

    }

    @Override
    public CompOffAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comp_off_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompOffAdapter.ViewHolder viewHolder, final int i) {

        try {

            String startDate = "", endDate = "";
            startDate = items.get(i).getStart_date();
            endDate = items.get(i).getData().getDate();

            startDate = getDate(startDate);
            viewHolder.worked_date_tv.setText(startDate.substring(0, 6) + "' " + startDate.substring(7) + " ");
            viewHolder.worked_dayname_tv.setText("(" + dayName + ")");

            endDate = getDate(endDate);
            viewHolder.compoff_date_tv.setText(endDate.substring(0, 6) + "' " + endDate.substring(7) + " ");
            viewHolder.compoff_dayname_tv.setText("(" + dayName + ")");


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setList(List<CompOffDetails> compOffDetailsList) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(compOffDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            dayName = days[c.get(Calendar.DAY_OF_WEEK) - 1];
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView compoff_date_tv, compoff_dayname_tv, worked_date_tv, worked_dayname_tv;


        public ViewHolder(View view) {
            super(view);

            compoff_date_tv = view.findViewById(R.id.compoff_date_tv);
            compoff_dayname_tv = view.findViewById(R.id.compoff_dayname_tv);
            worked_date_tv = view.findViewById(R.id.worked_date_tv);
            worked_dayname_tv = view.findViewById(R.id.worked_dayname_tv);


        }
    }


}