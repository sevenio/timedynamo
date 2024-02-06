package com.tvisha.trooptime.activity.activity.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.EmployeeData;
import com.tvisha.trooptime.activity.activity.AttendanceFragment;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LopsAdapter extends RecyclerView.Adapter<LopsAdapter.ViewHolder> {
    private static int TYPE = -1;
    String dateFormat = "dd MMMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    AttendanceFragment attendanceFragment;
    String hour = "", mins = "", AM_PM = "";
    private List<EmployeeData> items = new ArrayList<>();
    private Context context;

    public LopsAdapter(Context context, List<EmployeeData> items, AttendanceFragment attendanceFragment) {
        this.items = items;
        this.context = context;
        this.attendanceFragment = attendanceFragment;

    }

    @Override
    public LopsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lop_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LopsAdapter.ViewHolder viewHolder, final int position) {


        try {

            String date = "";
            date = items.get(position).getDate();
            date = getDate(date);
           // viewHolder.date_tv.setText(date.substring(0, 6) + "' " + date.substring(7));
            viewHolder.date_tv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {

            if (items != null && items.size() > 0 && position < items.size()) {


                final EmployeeData model = items.get(position);

                if (model != null) {
                    String checkin = getAttendenceTime(model.getCheck_in());
                    if (!checkin.trim().equals("N/A")) {

                        viewHolder.check_in_hours_tv.setText(hour);
                        viewHolder.check_in_mins_tv.setText(":" + mins);
                        viewHolder.check_in_am_pm_tv.setText(" " + AM_PM);
                    } else {
                        viewHolder.check_in_hours_tv.setText("");
                        viewHolder.check_in_mins_tv.setText("");
                        viewHolder.check_in_am_pm_tv.setText("");
                    }

                    String checkout = getAttendenceTime(model.getCheck_out());
                    if (!checkout.trim().equals("N/A")) {

                        viewHolder.check_out_hours_tv.setText(hour);
                        viewHolder.check_out_mins_tv.setText(":" + mins);
                        viewHolder.check_out_am_pm_tv.setText(" " + AM_PM);
                    } else {
                        viewHolder.check_out_hours_tv.setText("");
                        viewHolder.check_out_mins_tv.setText("");
                        viewHolder.check_out_am_pm_tv.setText("");
                    }

                    if (Utilities.timeComparision(model.getShift_start_time(), model.getCheck_in(), Constants.Static.CHECK_IN) && !checkin.trim().equals("N/A")) {
                        viewHolder.check_in_line.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.check_in_line.setVisibility(View.INVISIBLE);
                    }


                    boolean is_true = Utilities.workhoursComparision(model.getShift_working_hours(), model.getWorking_hours());

                    if (Utilities.timeComparision(model.getShift_end_time(), model.getCheck_out(), Constants.Static.CHECK_OUT) && !checkout.trim().equals("N/A") && is_true) {
                        viewHolder.check_out_line.setVisibility(View.VISIBLE);
                    } else {
                        if (is_true && !checkout.trim().equals("N/A")) {
                            viewHolder.check_out_line.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.check_out_line.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (TYPE == Constants.EARLY_LOGOUT_REPORT || TYPE == Constants.LATE_LOGIN_REPORT || TYPE == Constants.LESS_WORK_HRS_REPORT
                            ) {
                        viewHolder.statusImage.setVisibility(View.GONE);
                        viewHolder.exceptionCheckOutImage.setVisibility(View.GONE);
                        viewHolder.autoCheckOutLayout.setVisibility(View.GONE);
                        viewHolder.exceptionCheckInLayout.setVisibility(View.GONE);
                        viewHolder.exceptionCheckOutLayout.setVisibility(View.GONE);
                    } else if (TYPE == Constants.AUTOCHECK_OUT_REPORT || TYPE == Constants.EXCEPTION_REPORT) {


                        String approvedStatus = "";


                        if (model.getIs_auto_check_out() != null && !model.getIs_auto_check_out().trim().isEmpty() && (model.getIs_auto_check_out().trim().equals("1") || model.getIs_auto_check_out().trim().equals("2"))) {
                            viewHolder.autoCheckOutLayout.setVisibility(View.VISIBLE);
                            if (model.getIs_auto_check_out_approved() != null && !model.getIs_auto_check_out_approved().trim().isEmpty()) {
                                approvedStatus = model.getIs_auto_check_out_approved();
                                if (approvedStatus != null && !approvedStatus.equals("")) {
                                    if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_APPROVED) {
                                        viewHolder.autoCheckoutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_PENDING) {
                                        viewHolder.autoCheckoutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_REJECTED) {
                                        viewHolder.autoCheckoutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.reject));
                                    }

                                }
                            }

                        } else {
                            viewHolder.autoCheckOutLayout.setVisibility(View.GONE);
                        }
                        if (model.getIs_exception_check_in() != null && !model.getIs_exception_check_in().trim().isEmpty() && model.getIs_exception_check_in().trim().equals("1")) {
                            viewHolder.exceptionCheckInLayout.setVisibility(View.VISIBLE);
                            if (model.getException_status_in() != null && !model.getException_status_in().trim().isEmpty()) {
                                approvedStatus = model.getException_status_in();
                                if (approvedStatus != null && !approvedStatus.trim().equals("")) {
                                    if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_APPROVED) {
                                        viewHolder.exceptionCheckInStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_PENDING) {
                                        viewHolder.exceptionCheckInStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_REJECTED) {
                                        viewHolder.exceptionCheckInStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.reject));
                                    }

                                }
                            }

                        } else {
                            viewHolder.exceptionCheckInLayout.setVisibility(View.GONE);
                        }
                        if (model.getIs_exception_check_out() != null && !model.getIs_exception_check_out().trim().isEmpty() && model.getIs_exception_check_out().trim().equals("2")) {
                            viewHolder.exceptionCheckOutLayout.setVisibility(View.VISIBLE);
                            if (model.getException_status_out() != null && !model.getException_status_out().trim().isEmpty()) {
                                approvedStatus = model.getException_status_out();
                                if (approvedStatus != null && !approvedStatus.trim().equals("")) {
                                    if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_APPROVED) {
                                        viewHolder.exceptionCheckOutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_PENDING) {
                                        viewHolder.exceptionCheckOutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
                                    } else if (Integer.parseInt(approvedStatus) == Constants.EXCEPTION_AUTOCHECKOUT_STATUS_REJECTED) {
                                        viewHolder.exceptionCheckOutStatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.reject));
                                    }

                                }
                            }
                        } else {
                            viewHolder.exceptionCheckOutLayout.setVisibility(View.GONE);
                        }


                    }

                    if (!model.getCheck_in().contains("0000-00-00") && !model.getCheck_out().contains("0000-00-00")) {

                        if (!model.getWorking_hours().contains("00:00:00")) {
                            String[] split = model.getWorking_hours().split(":");
                            String hours = split[0];
                            String minutes = split[1];
                            viewHolder.hours_tv.setText(hours);
                            viewHolder.mins_tv.setText(minutes);

                            viewHolder.hours_tv.setVisibility(View.VISIBLE);
                            viewHolder.mins_tv.setVisibility(View.VISIBLE);
                            viewHolder.hours_title_tv.setVisibility(View.VISIBLE);
                            viewHolder.mins_title_tv.setVisibility(View.VISIBLE);
                            viewHolder.no_work_hours_tv.setVisibility(View.GONE);
                        }

                    } else {
                        viewHolder.hours_tv.setVisibility(View.GONE);
                        viewHolder.mins_tv.setVisibility(View.GONE);
                        viewHolder.hours_title_tv.setVisibility(View.GONE);
                        viewHolder.mins_title_tv.setVisibility(View.GONE);
                        viewHolder.no_work_hours_tv.setVisibility(View.GONE);
                    }
                    //attendanceId

                    viewHolder.autoCheckOutLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (TYPE != Constants.EARLY_LOGOUT_REPORT && TYPE != Constants.LATE_LOGIN_REPORT && TYPE != Constants.LESS_WORK_HRS_REPORT) {
                                if (model != null) {
                                    if (model.getIs_auto_check_out() != null && !model.getIs_auto_check_out().trim().isEmpty() &&
                                            (model.getIs_auto_check_out().trim().equals("1") || model.getIs_auto_check_out().trim().equals("2"))) {

                                        String time = viewHolder.check_out_hours_tv.getText().toString() + viewHolder.check_out_mins_tv.getText().toString() + viewHolder.check_out_am_pm_tv.getText().toString();
                                        attendanceFragment.getAutoCheckOutDetails(model.getDate(), model.getAttendance_id(), time, model.getIs_auto_check_out_approved(), model.getCheck_out(), model.getUser_id(), model.getIs_auto_check_out());
                                    }
                                }
                            }

                        }
                    });

                    viewHolder.exceptionCheckInLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TYPE != Constants.EARLY_LOGOUT_REPORT && TYPE != Constants.LATE_LOGIN_REPORT && TYPE != Constants.LESS_WORK_HRS_REPORT) {
                                if (model != null) {
                                    if (model.getIs_exception_check_in() != null && !model.getIs_exception_check_in().trim().isEmpty() &&
                                            (model.getIs_exception_check_in().equals("1") && model.getException_status_in() != null && model.getException_status_in().equals("0"))) {

                                        String time = viewHolder.check_in_hours_tv.getText().toString() + viewHolder.check_in_mins_tv.getText().toString() + viewHolder.check_in_am_pm_tv.getText().toString();
                                        attendanceFragment.getExceptionDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_in(), model.getCheck_out(), model.getUser_id(), model.getIs_auto_check_out(), "1", model.getCheck_in());
                                    } else if (model.getIs_exception_check_in() != null && !model.getIs_exception_check_in().trim().isEmpty() &&
                                            (model.getIs_exception_check_in().equals("1") && model.getException_status_in() != null && !model.getException_status_in().equals("0"))) {

                                        String time = viewHolder.check_out_hours_tv.getText().toString() + viewHolder.check_out_mins_tv.getText().toString() + viewHolder.check_out_am_pm_tv.getText().toString();
                                        attendanceFragment.getExceptionStatusDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_in(), model.getCheck_out(), model.getUser_id(), model.getIs_auto_check_out(), "1", model.getCheck_in());
                                    }
                                }
                            }

                        }
                    });
                    viewHolder.exceptionCheckOutLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TYPE != Constants.EARLY_LOGOUT_REPORT && TYPE != Constants.LATE_LOGIN_REPORT && TYPE != Constants.LESS_WORK_HRS_REPORT) {
                                if (model != null) {

                                    if (model.getIs_exception_check_out() != null && !model.getIs_exception_check_out().trim().isEmpty() &&
                                            (model.getIs_exception_check_out().equals("2") && model.getException_status_out() != null && model.getException_status_out().equals("0"))) {

                                        String time = viewHolder.check_in_hours_tv.getText().toString() + viewHolder.check_in_mins_tv.getText().toString() + viewHolder.check_in_am_pm_tv.getText().toString();
                                        attendanceFragment.getExceptionDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_out(), model.getCheck_out(), model.getUser_id(), model.getIs_auto_check_out(), "2", model.getCheck_in());
                                    } else if (model.getIs_exception_check_out() != null && !model.getIs_exception_check_out().trim().isEmpty() &&
                                            (model.getIs_exception_check_out().equals("2") && model.getException_status_out() != null && !model.getException_status_out().equals("0"))) {

                                        String time = viewHolder.check_out_hours_tv.getText().toString() + viewHolder.check_out_mins_tv.getText().toString() + viewHolder.check_out_am_pm_tv.getText().toString();
                                        attendanceFragment.getExceptionStatusDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_out(), model.getCheck_out(), model.getUser_id(), model.getIs_auto_check_out(), "2", model.getCheck_in());
                                    }

                                }
                            }

                        }
                    });

                    try {
                        if (model.getCheck_in() != null && model.getCheck_out() != null && !model.getCheck_in().equals("0000-00-00 00:00:00") && !model.getCheck_out().equals("0000-00-00 00:00:00")) {


                            String check_in_date = model.getCheck_in().substring(0, 10);
                            String check_out_date = model.getCheck_out().substring(0, 10);


                            if (!check_in_date.equals(check_out_date)) {
                                try {
                                    Date d = new SimpleDateFormat("yyyy-MM-dd").parse(check_out_date);
                                    String that_day = new SimpleDateFormat("dd MMM yyyy").format(d);
                                    String[] strings = that_day.split(" ");
                                    String day = strings[0];
                                    String mon = strings[1];
                                    String yea = strings[2];
                                    String complete_day = day + " " + mon + "' " + yea;
                                    viewHolder.checkOutDate.setVisibility(View.VISIBLE);
                                    viewHolder.checkOutDate.setText(" (" + complete_day + ")");
                                    complete_day = "";


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                viewHolder.checkOutDate.setVisibility(View.GONE);

                            }


                        } else {
                            viewHolder.checkOutDate.setVisibility(View.GONE);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setList(List<EmployeeData> employeeDataList) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(employeeDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setType(int type) {
        TYPE = type;
    }

    private String getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAttendenceTime(String timestamp) {
        String time = "N/A";
        try {
            if (!timestamp.contains("0000-00-00")) {
                Date d1 = null;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = null;
                if (timestamp != null && !timestamp.equals("null") && !timestamp.isEmpty() && timestamp.contains(".")) {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (timestamp != null) {
                    d1 = form.parse(timestamp);
                } else {
                    String date = form.format(calendar.getTime());
                    d1 = form.parse(date);
                }
                Calendar postDate = Calendar.getInstance();
                postDate.setTime(d1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String date = dateFormat.format(d1);


                if (postDate.get(Calendar.HOUR) < 10) {
                    hour = "0" + postDate.get(Calendar.HOUR);
                } else {
                    hour = postDate.get(Calendar.HOUR) + "";
                }

                if (postDate.get(Calendar.MINUTE) < 10) {
                    mins = "0" + postDate.get(Calendar.MINUTE);
                } else {
                    mins = postDate.get(Calendar.MINUTE) + "";
                }

                time = hour + ":" + mins;


                AM_PM = ((postDate.get(Calendar.AM_PM)) == 0 ? "am" : "pm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date_tv, check_in_hours_tv, check_in_mins_tv, check_in_am_pm_tv, check_out_hours_tv,
                check_out_mins_tv, check_out_am_pm_tv, hours_tv, mins_tv, checkOutDate, hours_title_tv, mins_title_tv, no_work_hours_tv;
        private ImageView statusImage, exceptionCheckOutImage, exceptionCheckOutStatusImage, exceptionCheckInStatusImage, autoCheckoutStatusImage;
        private LinearLayout autoCheckOutLayout, workHoursLayout, exceptionCheckInLayout, exceptionCheckOutLayout;
        private View check_out_line, check_in_line;


        public ViewHolder(View view) {
            super(view);

            date_tv = view.findViewById(R.id.date_tv);
            check_in_hours_tv = view.findViewById(R.id.check_in_hours_tv);
            check_in_mins_tv = view.findViewById(R.id.check_in_mins_tv);
            check_in_am_pm_tv = view.findViewById(R.id.check_in_am_pm_tv);
            check_out_hours_tv = view.findViewById(R.id.check_out_hours_tv);
            check_out_am_pm_tv = view.findViewById(R.id.check_out_am_pm_tv);
            check_out_mins_tv = view.findViewById(R.id.check_out_mins_tv);
            statusImage = view.findViewById(R.id.statusImage);
            checkOutDate = view.findViewById(R.id.checkOutDate);
            check_out_line = view.findViewById(R.id.check_out_line);
            check_in_line = view.findViewById(R.id.check_in_line);
            hours_tv = view.findViewById(R.id.hours_tv);
            mins_tv = view.findViewById(R.id.mins_tv);
            hours_title_tv = view.findViewById(R.id.hours_title_tv);
            mins_title_tv = view.findViewById(R.id.mins_title_tv);
            no_work_hours_tv = view.findViewById(R.id.no_work_hours_tv);
            exceptionCheckOutImage = view.findViewById(R.id.exceptionCheckOutImage);
            exceptionCheckInLayout = view.findViewById(R.id.exceptionCheckInLayout);
            exceptionCheckOutLayout = view.findViewById(R.id.exceptionCheckOutLayout);
            statusImage = view.findViewById(R.id.statusImage);
            autoCheckoutStatusImage = view.findViewById(R.id.autoCheckoutStatusImage);
            exceptionCheckInStatusImage = view.findViewById(R.id.exceptionCheckInStatusImage);
            autoCheckOutLayout = view.findViewById(R.id.autoCheckOutLayout);
            exceptionCheckOutStatusImage = view.findViewById(R.id.exceptionCheckOutStatusImage);


        }
    }
}
