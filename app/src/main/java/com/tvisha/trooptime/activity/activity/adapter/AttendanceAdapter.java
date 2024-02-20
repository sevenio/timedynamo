package com.tvisha.trooptime.activity.activity.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.AttendanceFragment;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    String AM_PM = "";
    private List<Attendance> items = new ArrayList<>();
    private Context context;
    private AttendanceFragment attendanceFragment;

    public AttendanceAdapter(Context context, List<Attendance> items, AttendanceFragment attendanceFragment) {
        this.items = items;
        this.context = context;
        this.attendanceFragment = attendanceFragment;
    }


    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final AttendanceAdapter.ViewHolder viewHolder, final int position) {

        try {


            String isAutoCheckOut, isAutoCheckOutApproved, isCheckInException, isCheckOutException, isCheckInExceptionApproved, isCheckOutExceptionApproved;


            /*if (position == 0) {
                viewHolder.titleLayout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.titleLayout.setVisibility(View.GONE);
            }*/
            viewHolder.attendanceLayout.setVisibility(View.VISIBLE);
            viewHolder.attendanceStatusLayout.setVisibility(View.GONE);
            viewHolder.checkin_am_pm_tv.setVisibility(View.VISIBLE);
            viewHolder.check_in_time_tv.setVisibility(View.VISIBLE);
            viewHolder.check_out_time_tv.setVisibility(View.VISIBLE);
            viewHolder.checkout_am_pm_tv.setVisibility(View.VISIBLE);
            viewHolder.autoCheckOutLayout.setVisibility(View.GONE);


            final Attendance model = items.get(position);

            isAutoCheckOut = model.getIsAutoCheckOut();
            isAutoCheckOutApproved = model.getIsAutoCheckOutApproved();
            if (isAutoCheckOut != null && !isAutoCheckOut.trim().isEmpty() && (isAutoCheckOut.equals("1") || isAutoCheckOut.equals("2"))) {
                viewHolder.autoCheckOutLayout.setVisibility(View.VISIBLE);
                if (isAutoCheckOutApproved != null && !isAutoCheckOutApproved.trim().isEmpty()) {
                    if (Integer.parseInt(isAutoCheckOutApproved) == Constants.REQUEST_STATUS_APPROVED) {
                        viewHolder.statusImage.setImageResource(R.drawable.accpeted);
                    } else if (Integer.parseInt(isAutoCheckOutApproved) == Constants.REQUEST_STATUS_REJECTED) {
                        viewHolder.statusImage.setImageResource(R.drawable.reject);
                    } else {
                        viewHolder.statusImage.setImageResource(R.drawable.pending);
                    }
                }
            } else {
                viewHolder.autoCheckOutLayout.setVisibility(View.GONE);
            }


            viewHolder.autoCheckOutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getIsAutoCheckOut() != null && !model.getIsAutoCheckOut().trim().isEmpty() &&
                            (model.getIsAutoCheckOut().equals("1") || model.getIsAutoCheckOut().equals("2"))) {

                        String time = viewHolder.check_out_time_tv.getText().toString().trim() + " " + viewHolder.checkout_am_pm_tv.getText().toString().trim();
                        attendanceFragment.getAutoCheckOutDetails(model.getDate(), model.getAttendance_id(), time, model.getIsAutoCheckOutApproved(), model.getCheckOut(), model.getUserId(), model.getIsAutoCheckOut());
                    }

                }
            });

            isCheckInException = model.getIs_exception_check_in();
            isCheckInExceptionApproved = model.getException_status_in();
            if (isCheckInException != null && !isCheckInException.trim().isEmpty() && isCheckInException.equals("1")) {
                viewHolder.exceptionCheckInLayout.setVisibility(View.VISIBLE);
                if (isCheckInExceptionApproved != null && !isCheckInExceptionApproved.trim().isEmpty()) {
                    if (Integer.parseInt(isCheckInExceptionApproved) == Constants.REQUEST_STATUS_APPROVED) {
                        viewHolder.exceptionCheckInStatusImage.setImageResource(R.drawable.accpeted);
                    } else if (Integer.parseInt(isCheckInExceptionApproved) == Constants.REQUEST_STATUS_REJECTED) {
                        viewHolder.exceptionCheckInStatusImage.setImageResource(R.drawable.reject);
                    } else {
                        viewHolder.exceptionCheckInStatusImage.setImageResource(R.drawable.pending);
                    }
                }
            } else {
                viewHolder.exceptionCheckInLayout.setVisibility(View.GONE);
            }


            viewHolder.exceptionCheckInLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getIs_exception_check_in() != null && !model.getIs_exception_check_in().trim().isEmpty() &&
                            (model.getIs_exception_check_in().equals("1") && model.getException_status_in() != null && model.getException_status_in().equals("0"))) {

                        String time = viewHolder.check_in_time_tv.getText().toString().trim() + " " + viewHolder.checkin_am_pm_tv.getText().toString().trim();
                        attendanceFragment.getExceptionDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_in(), model.getCheckOut(), model.getUserId(), model.getIsAutoCheckOut(), "1", model.getCheckIn());
                    }
                    if (model.getIs_exception_check_in() != null && !model.getIs_exception_check_in().trim().isEmpty() &&
                            (model.getIs_exception_check_in().equals("1") && model.getException_status_in() != null && !model.getException_status_in().equals("0"))) {

                        String time = viewHolder.check_in_time_tv.getText().toString().trim() + " " + viewHolder.checkin_am_pm_tv.getText().toString().trim();
                        attendanceFragment.getExceptionStatusDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_in(), model.getCheckOut(), model.getUserId(), model.getIsAutoCheckOut(), "1", model.getCheckIn());
                    }


                }
            });


            isCheckOutException = model.getIs_exception_check_out();
            isCheckOutExceptionApproved = model.getException_status_out();
            if (isCheckOutException != null && !isCheckOutException.trim().isEmpty() && isCheckOutException.equals("2")) {
                viewHolder.exceptionCheckOutLayout.setVisibility(View.VISIBLE);
                if (isCheckOutExceptionApproved != null && !isCheckOutExceptionApproved.trim().isEmpty()) {
                    if (Integer.parseInt(isCheckOutExceptionApproved) == Constants.REQUEST_STATUS_APPROVED) {
                        viewHolder.exceptionCheckOutStatusImage.setImageResource(R.drawable.accpeted);
                    } else if (Integer.parseInt(isCheckOutExceptionApproved) == Constants.REQUEST_STATUS_REJECTED) {
                        viewHolder.exceptionCheckOutStatusImage.setImageResource(R.drawable.reject);
                    } else {
                        viewHolder.exceptionCheckOutStatusImage.setImageResource(R.drawable.pending);
                    }
                }
            } else {
                viewHolder.exceptionCheckOutLayout.setVisibility(View.GONE);
            }


            viewHolder.exceptionCheckOutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getIs_exception_check_out() != null && !model.getIs_exception_check_out().trim().isEmpty() &&
                            (model.getIs_exception_check_out().equals("2") && model.getException_status_out() != null && model.getException_status_out().equals("0"))) {

                        String time = viewHolder.check_out_time_tv.getText().toString().trim() + " " + viewHolder.checkout_am_pm_tv.getText().toString().trim();
                        attendanceFragment.getExceptionDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_out(), model.getCheckOut(), model.getUserId(), model.getIsAutoCheckOut(), "2", model.getCheckIn());
                    } else if (model.getIs_exception_check_out() != null && !model.getIs_exception_check_out().trim().isEmpty() &&
                            (model.getIs_exception_check_out().equals("2") && model.getException_status_out() != null && !model.getException_status_out().equals("0"))) {

                        String time = viewHolder.check_out_time_tv.getText().toString().trim() + " " + viewHolder.checkout_am_pm_tv.getText().toString().trim();
                        attendanceFragment.getExceptionStatusDetails(model.getDate(), model.getAttendance_id(), time, model.getException_status_out(), model.getCheckOut(), model.getUserId(), model.getIsAutoCheckOut(), "2", model.getCheckIn());
                    }


                }
            });

            String checkin = getAttendenceTime(model.getCheckIn());
            if (!checkin.trim().equals("N/A")) {
                viewHolder.check_in_time_tv.setVisibility(View.VISIBLE);
                viewHolder.checkin_am_pm_tv.setVisibility(View.VISIBLE);
                viewHolder.check_in_time_tv.setText(checkin.trim());
                viewHolder.checkin_am_pm_tv.setText(" " + AM_PM);
            } else {
                viewHolder.check_in_time_tv.setVisibility(View.VISIBLE);
                viewHolder.checkin_am_pm_tv.setVisibility(View.INVISIBLE);
                viewHolder.check_in_time_tv.setText("N/A");

            }
            String checkout = getAttendenceTime(model.getCheckOut());
            if (!checkout.trim().equals("N/A")) {
                viewHolder.check_out_time_tv.setVisibility(View.VISIBLE);
                viewHolder.checkout_am_pm_tv.setVisibility(View.VISIBLE);
                viewHolder.check_out_time_tv.setText(checkout.trim());
                viewHolder.checkout_am_pm_tv.setText(" " + AM_PM);
            } else {
                viewHolder.check_out_time_tv.setVisibility(View.VISIBLE);
                viewHolder.checkout_am_pm_tv.setVisibility(View.INVISIBLE);
                viewHolder.check_out_time_tv.setText("N/A");

            }


            if (Utilities.timeComparision(model.getShiftStartTime(), model.getCheckIn(), Constants.Static.CHECK_IN) && !checkin.trim().equals("N/A")) {
                viewHolder.check_in_line.setVisibility(View.VISIBLE);
            } else {
                viewHolder.check_in_line.setVisibility(View.INVISIBLE);
            }

            boolean is_true = Utilities.workhoursComparision(model.getShiftWorkingHours(), model.getWorkingHours());

            if (Utilities.timeComparision(model.getShiftEndTime(), model.getCheckOut(), Constants.Static.CHECK_OUT) && !checkout.trim().equals("N/A")) {
                if (is_true) {
                    viewHolder.check_out_line.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.check_out_line.setVisibility(View.INVISIBLE);
                }

            } else {

                viewHolder.check_out_line.setVisibility(View.INVISIBLE);
          /*  if (is_true && !checkout.trim().equals("N/A")) {
                viewHolder.check_out_line.setVisibility(View.VISIBLE);
            } else {
                viewHolder.check_out_line.setVisibility(View.INVISIBLE);
            }*/
            }


      /*  if (Utilities.timeComparision(model.getShift_end_time(), model.getCheck_out(), Constants.Static.CHECK_OUT) && !checkout.trim().equals("N/A") && is_true) {
            viewHolder.check_out_line.setVisibility(View.VISIBLE);
        } else {
            if (is_true && !checkout.trim().equals("N/A")) {
                viewHolder.check_out_line.setVisibility(View.VISIBLE);
            } else {
                viewHolder.check_out_line.setVisibility(View.INVISIBLE);
            }
        }*/


            if (!model.getCheckIn().contains("0000-00-00") && !model.getCheckOut().contains("0000-00-00")) {

                if (!model.getWorkingHours().contains("00:00:00")) {
                    String[] split = model.getWorkingHours().split(":");
                    String hours = split[0];
                    String minutes = split[1];

                    viewHolder.hours_tv.setVisibility(View.VISIBLE);
                    viewHolder.hours_title_tv.setVisibility(View.VISIBLE);
                    viewHolder.mins_title_tv.setVisibility(View.VISIBLE);
                    viewHolder.mins_tv.setVisibility(View.VISIBLE);

                    viewHolder.hours_tv.setText(hours);
                    viewHolder.mins_tv.setText(minutes);
                } else {
                    viewHolder.hours_tv.setVisibility(View.GONE);
                    viewHolder.hours_title_tv.setVisibility(View.GONE);
                    viewHolder.mins_title_tv.setVisibility(View.GONE);
                    viewHolder.mins_tv.setVisibility(View.GONE);

                }
            } else {
                viewHolder.hours_tv.setVisibility(View.VISIBLE);
                viewHolder.hours_tv.setText("N/A");
                viewHolder.hours_title_tv.setVisibility(View.GONE);
                viewHolder.mins_title_tv.setVisibility(View.GONE);
                viewHolder.mins_tv.setVisibility(View.GONE);
            }


            if (model.getCheckIn() != null && !model.getCheckIn().equals("0000-00-00 00:00:00")) {
                if (model.getCheckIn().length() >= 10) {
                    String check_in_date = model.getCheckIn().substring(0, 10);
                    try {
                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(check_in_date);
                        String that_day = new SimpleDateFormat("dd MMM yyyy").format(d);
                        String[] strings = that_day.split(" ");
                        String day = strings[0];
                        String mon = strings[1];
                        String yea = strings[2];
                        viewHolder.date_tv.setText(day);
                        viewHolder.month_tv.setText(mon);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (model.getDate() != null && !model.getDate().equals("0000-00-00 00:00:00")) {
                    if (model.getDate().length() >= 10) {
                        String check_in_date = model.getDate().substring(0, 10);
                        try {
                            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(check_in_date);
                            String that_day = new SimpleDateFormat("dd MMM yyyy").format(d);
                            String[] strings = that_day.split(" ");
                            String day = strings[0];
                            String mon = strings[1];
                            String yea = strings[2];
                            viewHolder.date_tv.setText(day);
                            viewHolder.month_tv.setText(mon);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            switch (model.getIsHoliday()) {

                case Constants.HOLIDAY_TYPE_NONE:
                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("ABSENT/NCNS");
                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }


                    break;
                case Constants.HOLIDAY_TYPE_WEEKOFF:
                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("WEEK OFF");

                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }

                    break;
                case Constants.HOLIDAY_TYPE_CONFIRM:
                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("CONFIRM HOLIDAY");
                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }


                    break;
                case Constants.HOLIDAY_TYPE_OPTIONAL:
                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("OPTIONAL HOLIDAY");

                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }


                    break;

                case Constants.HOLIDAY_TYPE_LEAVE:

                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("LEAVE");
                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }

                    break;
                case Constants.HOLIDAY_TYPE_COMPOFF:

                    if (model.getCheckIn().equals("0000-00-00 00:00:00") && model.getWorkingHours().equals("00:00:00")) {
                        viewHolder.attendanceLayout.setVisibility(View.GONE);
                        viewHolder.attendanceStatusLayout.setVisibility(View.VISIBLE);
                        viewHolder.checkin_status_tv.setText("COMP OFF");
                    } else {
                        viewHolder.checkin_status_tv.setText("");
                    }

                    break;

                default:
                    viewHolder.attendanceLayout.setVisibility(View.VISIBLE);
                    viewHolder.attendanceStatusLayout.setVisibility(View.GONE);
            }


            try {
                if (model.getCheckIn() != null && model.getCheckOut() != null && !model.getCheckIn().equals("0000-00-00 00:00:00") && !model.getCheckOut().equals("0000-00-00 00:00:00")) {


                    String check_in_date = model.getCheckIn().substring(0, 10);
                    String check_out_date = model.getCheckOut().substring(0, 10);


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
                            viewHolder.checkOutDate1.setVisibility(View.VISIBLE);

                            viewHolder.checkOutDate.setText("(" + complete_day + ")");
                            viewHolder.checkOutDate1.setText("");
                            complete_day = "";


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        viewHolder.checkOutDate.setVisibility(View.GONE);
                        viewHolder.checkOutDate1.setVisibility(View.GONE);
                    }


                } else {
                    viewHolder.checkOutDate.setVisibility(View.GONE);
                    viewHolder.checkOutDate1.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Attendance> attendances) {
        try {
            List<Attendance> temp = new ArrayList<>();
            temp.addAll(attendances);
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAttendenceTime(String timestamp) {

        String time = "N/A";
        try {
            if (!timestamp.contains("0000-00-00")) {
                Date d1 = null;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = null;
                if (timestamp != null && !timestamp.equals("null") && !timestamp.trim().isEmpty() && timestamp.contains(".")) {
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

                String hour = "", mins = "";

                if (postDate.get(Calendar.HOUR) == 0) {
                    hour = "12";
                } else if (postDate.get(Calendar.HOUR) < 10) {
                    hour = "0" + postDate.get(Calendar.HOUR);
                } else {
                    hour = postDate.get(Calendar.HOUR) + "";
                }

                if (postDate.get(Calendar.MINUTE) < 10) {
                    mins = "0" + postDate.get(Calendar.MINUTE);
                } else {
                    mins = postDate.get(Calendar.MINUTE) + "";
                }


                AM_PM = ((postDate.get(Calendar.AM_PM)) == 0 ? "am" : "pm");


                time = hour + ":" + mins;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView check_in_time_tv, checkin_am_pm_tv, check_out_time_tv, checkout_am_pm_tv,
                hours_tv, mins_tv, date_tv, month_tv, mins_title_tv, hours_title_tv, checkin_status_tv, checkOutDate, checkOutDate1;
        View check_out_line, check_in_line;
        LinearLayout attendanceLayout, attendanceStatusLayout, titleLayout, autoCheckOutLayout, workHoursLayout, exceptionCheckInLayout, exceptionCheckOutLayout;
        ImageView statusImage, exceptionCheckOutStatusImage, exceptionCheckInStatusImage;


        public ViewHolder(View view) {
            super(view);

            check_in_time_tv = view.findViewById(R.id.check_in_time_tv);
            checkin_am_pm_tv = view.findViewById(R.id.checkin_am_pm_tv);
            check_out_time_tv = view.findViewById(R.id.check_out_time_tv);
            checkout_am_pm_tv = view.findViewById(R.id.checkout_am_pm_tv);
            checkOutDate = (TextView) itemView.findViewById(R.id.checkOutDate);
            checkOutDate1 = (TextView) itemView.findViewById(R.id.checkOutDate1);
            hours_tv = view.findViewById(R.id.hours_tv);
            mins_tv = view.findViewById(R.id.mins_tv);
            date_tv = view.findViewById(R.id.date_tv);
            month_tv = view.findViewById(R.id.month_tv);
            check_out_line = view.findViewById(R.id.check_out_line);
            check_in_line = view.findViewById(R.id.check_in_line);
            hours_title_tv = view.findViewById(R.id.hours_title_tv);
            mins_title_tv = view.findViewById(R.id.mins_title_tv);
            attendanceLayout = view.findViewById(R.id.attendanceLayout);
            attendanceStatusLayout = view.findViewById(R.id.attendanceStatusLayout);
            checkin_status_tv = view.findViewById(R.id.checkin_status_tv);
            //titleLayout = view.findViewById(R.id.titleLayout);
            workHoursLayout = view.findViewById(R.id.workHoursLayout);

            exceptionCheckInLayout = view.findViewById(R.id.exceptionCheckInLayout);
            exceptionCheckOutLayout = view.findViewById(R.id.exceptionCheckOutLayout);
            statusImage = view.findViewById(R.id.statusImage);
            exceptionCheckInStatusImage = view.findViewById(R.id.exceptionCheckInStatusImage);
            autoCheckOutLayout = view.findViewById(R.id.autoCheckOutLayout);
            exceptionCheckOutStatusImage = view.findViewById(R.id.exceptionCheckOutStatusImage);


        }
    }


}

