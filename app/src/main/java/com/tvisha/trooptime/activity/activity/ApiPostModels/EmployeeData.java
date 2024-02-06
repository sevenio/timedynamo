package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("check_in")
    @Expose
    private String check_in;
    @SerializedName("check_out")
    @Expose
    private String check_out;
    @SerializedName("working_hours")
    @Expose
    private String working_hours;
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
    @SerializedName("shift_start_time")
    @Expose
    private String shift_start_time;
    @SerializedName("shift_end_time")
    @Expose
    private String shift_end_time;
    @SerializedName("shift_working_hours")
    @Expose
    private String shift_working_hours;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("shift_name")
    @Expose
    private String shift_name;
    @SerializedName("check_in_branch")
    @Expose
    private String check_in_branch;
    @SerializedName("check_out_branch")
    @Expose
    private String check_out_branch;
    @SerializedName("in_branch")
    @Expose
    private String in_branch;
    @SerializedName("out_branch")
    @Expose
    private String out_branch;
    @SerializedName("is_auto_check_out")
    @Expose
    private String is_auto_check_out;
    @SerializedName("is_exception")
    @Expose
    private String is_exception;
    @SerializedName("exception_status")
    @Expose
    private String exception_status;
    @SerializedName("is_auto_check_out_approved")
    @Expose
    private String is_auto_check_out_approved;
    @SerializedName("is_exception_check_in")
    @Expose
    private String is_exception_check_in;
    @SerializedName("is_exception_check_out")
    @Expose
    private String is_exception_check_out;
    @SerializedName("exception_check_in_status")
    @Expose
    private String exception_status_in;
    @SerializedName("exception_check_out_status")
    @Expose
    private String exception_status_out;
    @SerializedName("attendance_id")
    @Expose
    private String attendance_id;

    public String getIs_exception_check_in() {
        return is_exception_check_in;
    }

    public void setIs_exception_check_in(String is_exception_check_in) {
        this.is_exception_check_in = is_exception_check_in;
    }

    public String getIs_exception_check_out() {
        return is_exception_check_out;
    }

    public void setIs_exception_check_out(String is_exception_check_out) {
        this.is_exception_check_out = is_exception_check_out;
    }


    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getCheck_in_branch() {
        return check_in_branch;
    }

    public void setCheck_in_branch(String check_in_branch) {
        this.check_in_branch = check_in_branch;
    }

    public String getCheck_out_branch() {
        return check_out_branch;
    }

    public void setCheck_out_branch(String check_out_branch) {
        this.check_out_branch = check_out_branch;
    }

    public String getIn_branch() {
        return in_branch;
    }

    public void setIn_branch(String in_branch) {
        this.in_branch = in_branch;
    }

    public String getOut_branch() {
        return out_branch;
    }

    public void setOut_branch(String out_branch) {
        this.out_branch = out_branch;
    }

    public String getIs_auto_check_out() {
        return is_auto_check_out;
    }

    public void setIs_auto_check_out(String is_auto_check_out) {
        this.is_auto_check_out = is_auto_check_out;
    }

    public String getIs_exception() {
        return is_exception;
    }

    public void setIs_exception(String is_exception) {
        this.is_exception = is_exception;
    }

    public String getException_status() {
        return exception_status;
    }

    public void setException_status(String exception_status) {
        this.exception_status = exception_status;
    }

    public String getIs_auto_check_out_approved() {
        return is_auto_check_out_approved;
    }

    public void setIs_auto_check_out_approved(String is_auto_check_out_approved) {
        this.is_auto_check_out_approved = is_auto_check_out_approved;
    }

    public String getException_status_in() {
        return exception_status_in;
    }

    public void setException_status_in(String exception_status_in) {
        this.exception_status_in = exception_status_in;
    }

    public String getException_status_out() {
        return exception_status_out;
    }

    public void setException_status_out(String exception_status_out) {
        this.exception_status_out = exception_status_out;
    }
}
