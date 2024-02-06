package com.tvisha.trooptime.activity.activity.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.listner.TimeChangeListner;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by koti on 10/2/17.
 */

public class TimePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final int TYPE_DATE = 1;
    public static final int TYPE_TIME = 2;
    public static final int TYPE_DATE_TIME = 0;
    public static TimePicker timePicker = new TimePicker();
    public static int CALENDAR_TYPE = -1;
    public static int CALENDAR_TYPE_PAST = 0;
    public static int CALENDAR_TYPE_FEATURE = 1;
    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    TextView timeHolder;
    String slelectedDate;
    int TYPE;  //0=> for time and date 1=>for date only

    public static TimePicker getInstance(int REQ_TYPE, TextView view) {
        //  if (timePicker == null) {
        timePicker = new TimePicker();
        //  }
        timePicker.TYPE = REQ_TYPE;
        timePicker.timeHolder = view;
        timePicker.CALENDAR_TYPE = -1;
        timePicker.slelectedDate = null;
        return timePicker;
    }

    public static TimePicker getInstance(int REQ_TYPE, int CAL_TYPE, TextView view) {
        //if (timePicker == null) {
        timePicker = new TimePicker();
        //}
        timePicker.TYPE = REQ_TYPE;
        timePicker.CALENDAR_TYPE = CAL_TYPE;
        timePicker.timeHolder = view;
        timePicker.slelectedDate = null;
        return timePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // Use the current date as the default date in the picker
        if (TYPE == TYPE_TIME) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if (TYPE == TYPE_TIME) {
                        timeHolder.setText(selectedHour + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute));
                    } else {
                        timeHolder.setText(slelectedDate + " " + selectedHour + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute));
                    }

                    if ((getContext() instanceof TimeChangeListner)) {
                        ((TimeChangeListner) getContext()).onTimeChanged();
                    }
                }
            }, hour, minute, true);
            return timePickerDialog;
        } else {
            //if u set min and max then it show remain days only
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, startYear, startMonth, startDay);
            if (CALENDAR_TYPE == CALENDAR_TYPE_PAST) {
                Date cDate = c.getTime();
                dialog.getDatePicker().setMaxDate(cDate.getTime());
            } else {
                c.add(Calendar.DATE, +1);
                Date fDate = c.getTime();
                dialog.getDatePicker().setMinDate(fDate.getTime());
            }
            return dialog;
        }
        //from current date
        //dialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        //  String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;  //for display
        //slelectedDate = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;  //for format
        slelectedDate = year + "-" + ((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1)) + "-" + ((dayOfMonth) < 10 ? ("0" + dayOfMonth) : dayOfMonth);  //for format
        timeHolder.setTag(slelectedDate);
        timeHolder.setText(slelectedDate);
        if (TYPE == TYPE_DATE && (getContext() instanceof TimeChangeListner)) {
            ((TimeChangeListner) getContext()).onTimeChanged();
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (TYPE == TYPE_DATE_TIME) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if (TYPE == TYPE_TIME) {
                        timeHolder.setText(selectedHour + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute));
                    } else {
                        timeHolder.setText(slelectedDate + " " + selectedHour + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute));
                    }
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Event Time");
            mTimePicker.show();
        } else if (TYPE != TYPE_TIME) {
            if (slelectedDate != null) {
                timeHolder.setText(slelectedDate);
            }
        }
        super.onDismiss(dialog);
    }
}
