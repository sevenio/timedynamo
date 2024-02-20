package com.tvisha.trooptime.activity.activity.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.apiPostModels.Break;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BreaksAdapter extends RecyclerView.Adapter<BreaksAdapter.ViewHolder> {

    private List<Break> items = new ArrayList<>();
    private Context context;

    public BreaksAdapter(Context context, List<Break> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public BreaksAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.breaks_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BreaksAdapter.ViewHolder viewHolder, final int i) {

        try {


            if (i == getItemCount() - 1) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }

            try {
                if (viewHolder.title != null) {
                    String breakName = items.get(i).getBreak_name();
                    breakName = breakName.toLowerCase().replace("break", "");
                    viewHolder.title.setText(Helper.getInstance().capitalize(breakName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            String from = items.get(i).getStart_time().substring(11, 16);
            String to = items.get(i).getEnd_time().substring(11, 16);
            int from_hours = Integer.parseInt(from.substring(0, 2));
            int to_hours = Integer.parseInt(to.substring(0, 2));
            if (from_hours <= 12) {

                if (from_hours == 12) {
                    viewHolder.fromTimeAmPm.setText(" pm - ");
                } else {
                    viewHolder.fromTimeAmPm.setText(" am - ");
                }

                if (from_hours < 10) {

                    viewHolder.fromTime.setText(items.get(i).getStart_time().substring(11, 16));

                } else {
                    viewHolder.fromTime.setText(items.get(i).getStart_time().substring(11, 16));
                }

            } else {
                int h = from_hours - 12;
                viewHolder.fromTimeAmPm.setText(" pm - ");
                if (h < 10) {

                    viewHolder.fromTime.setText(h + items.get(i).getStart_time().substring(13, 16));
                } else {
                    viewHolder.fromTime.setText(h + items.get(i).getStart_time().substring(13, 16));
                }

            }

            if (to_hours <= 12) {
                if (to_hours == 12) {
                    viewHolder.toTimeAmPm.setText(" pm");
                } else {
                    viewHolder.toTimeAmPm.setText(" am");
                }

                if (to_hours < 10) {

                    viewHolder.toTime.setText(items.get(i).getEnd_time().substring(11, 16));
                } else {
                    viewHolder.toTime.setText(items.get(i).getEnd_time().substring(11, 16));
                }

            } else {
                int h = to_hours - 12;
                viewHolder.toTimeAmPm.setText(" pm");
                if (h < 10) {
                    viewHolder.toTime.setText(h + items.get(i).getEnd_time().substring(13, 16));

                } else {
                    viewHolder.toTime.setText(h + items.get(i).getEnd_time().substring(13, 16));
                }

            }

            // viewHolder.toTime.setText(capitalize(items.get(i).getEnd_time().substring(11,16)));


            long time = timeDifference(items.get(i).getStart_time(), items.get(i).getEnd_time());
            viewHolder.breakMins.setText(String.valueOf((int) time));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Break> breaks_lists) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(breaks_lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long timeDifference(String startTime, String endTime) {


        Date startDate, endDate;

        long mins = 0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            startDate = simpleDateFormat.parse(startTime);
            endDate = simpleDateFormat.parse(endTime);
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();


            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            mins = different / minutesInMilli;


            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;




            if (elapsedSeconds > 30) {
                mins = mins + 1;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mins;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View line;
        private TextView title, fromTime, toTime, breakHours, fromTimeAmPm, toTimeAmPm, breakHoursTitle, breakMins, breakMinsTitle;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            fromTime = view.findViewById(R.id.fromTime);
            toTime = view.findViewById(R.id.toTime);
            breakHours = view.findViewById(R.id.breakHours);
            line = view.findViewById(R.id.line);
            fromTimeAmPm = view.findViewById(R.id.fromTimeAmPm);
            toTimeAmPm = view.findViewById(R.id.toTimeAmPm);
            breakHoursTitle = view.findViewById(R.id.breakHoursTitle);
            breakMins = view.findViewById(R.id.breakMins);
            breakMinsTitle = view.findViewById(R.id.breakMinsTitle);


        }
    }

}

