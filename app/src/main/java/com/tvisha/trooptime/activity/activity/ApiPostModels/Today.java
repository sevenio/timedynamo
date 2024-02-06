package com.tvisha.trooptime.activity.activity.ApiPostModels;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Today implements Parcelable {

    public final static Parcelable.Creator<Today> CREATOR = new Creator<Today>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Today createFromParcel(Parcel in) {
            return new Today(in);
        }

        public Today[] newArray(int size) {
            return (new Today[size]);
        }

    };
    @SerializedName("check_in")
    @Expose
    private String check_in;
    @SerializedName("check_out")
    @Expose
    private String check_out;
    @SerializedName("working_hours")
    @Expose
    private String working_hours;
    @SerializedName("break_time")
    @Expose
    private int break_time;
    @SerializedName("attendance_list")
    @Expose
    private List<Attendance_list> attendance_list = null;
    @SerializedName("ot")
    @Expose
    private String ot;
    @SerializedName("is_holiday")
    @Expose
    private String is_holiday;
    @SerializedName("is_checkin_manual")
    @Expose
    private String is_checkin_manual;
    @SerializedName("is_checkout_manual")
    @Expose
    private String is_checkout_manual;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("number_of_breaks")
    @Expose
    private int number_of_breaks;
    @SerializedName("shift_start_time")
    @Expose
    private String shift_start_time;
    @SerializedName("shift_end_time")
    @Expose
    private String shift_end_time;
    @SerializedName("shift_working_hours")
    @Expose
    private String shift_working_hours;
    @SerializedName("breaks_list")
    @Expose
    private Breaks_list breaks_list;
    @SerializedName("day")
    @Expose
    private String day;

    protected Today(Parcel in) {
        this.check_in = ((String) in.readValue((String.class.getClassLoader())));
        this.check_out = ((String) in.readValue((String.class.getClassLoader())));
        this.working_hours = ((String) in.readValue((String.class.getClassLoader())));
        this.break_time = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.attendance_list, (Attendance_list.class.getClassLoader()));
        this.ot = ((String) in.readValue((String.class.getClassLoader())));
        this.is_holiday = ((String) in.readValue((String.class.getClassLoader())));
        this.is_checkin_manual = ((String) in.readValue((String.class.getClassLoader())));
        this.is_checkout_manual = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.number_of_breaks = ((int) in.readValue((int.class.getClassLoader())));
        this.shift_start_time = ((String) in.readValue((String.class.getClassLoader())));
        this.shift_end_time = ((String) in.readValue((String.class.getClassLoader())));
        this.shift_working_hours = ((String) in.readValue((String.class.getClassLoader())));
        this.breaks_list = ((Breaks_list) in.readValue((Breaks_list.class.getClassLoader())));
        this.day = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Today() {
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public int getBreak_time() {
        return break_time;
    }

    public void setBreak_time(int break_time) {
        this.break_time = break_time;
    }

    public List<Attendance_list> getAttendance_list() {
        return attendance_list;
    }

    public void setAttendance_list(List<Attendance_list> attendance_list) {
        this.attendance_list = attendance_list;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public String getIs_holiday() {
        return is_holiday;
    }

    public void setIs_holiday(String is_holiday) {
        this.is_holiday = is_holiday;
    }

    public String getIs_checkin_manual() {
        return is_checkin_manual;
    }

    public void setIs_checkin_manual(String is_checkin_manual) {
        this.is_checkin_manual = is_checkin_manual;
    }

    public String getIs_checkout_manual() {
        return is_checkout_manual;
    }

    public void setIs_checkout_manual(String is_checkout_manual) {
        this.is_checkout_manual = is_checkout_manual;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber_of_breaks() {
        return number_of_breaks;
    }

    public void setNumber_of_breaks(int number_of_breaks) {
        this.number_of_breaks = number_of_breaks;
    }

    public String getShift_start_time() {
        return shift_start_time;
    }

    public void setShift_start_time(String shift_start_time) {
        this.shift_start_time = shift_start_time;
    }

    public String getShift_end_time() {
        return shift_end_time;
    }

    public void setShift_end_time(String shift_end_time) {
        this.shift_end_time = shift_end_time;
    }

    public String getShift_working_hours() {
        return shift_working_hours;
    }

    public void setShift_working_hours(String shift_working_hours) {
        this.shift_working_hours = shift_working_hours;
    }

    public Breaks_list getBreaks_list() {
        return breaks_list;
    }

    public void setBreaks_list(Breaks_list breaks_list) {
        this.breaks_list = breaks_list;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(check_in);
        dest.writeValue(check_out);
        dest.writeValue(working_hours);
        dest.writeValue(break_time);
        dest.writeList(attendance_list);
        dest.writeValue(ot);
        dest.writeValue(is_holiday);
        dest.writeValue(is_checkin_manual);
        dest.writeValue(is_checkout_manual);
        dest.writeValue(date);
        dest.writeValue(number_of_breaks);
        dest.writeValue(shift_start_time);
        dest.writeValue(shift_end_time);
        dest.writeValue(shift_working_hours);
        dest.writeValue(breaks_list);
        dest.writeValue(day);
    }

    public int describeContents() {
        return 0;
    }

}