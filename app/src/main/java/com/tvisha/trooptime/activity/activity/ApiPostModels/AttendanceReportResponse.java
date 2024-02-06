package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceReportResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("attendanceReport")
    @Expose
    private AttendanceReport attendanceReport;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AttendanceReport getAttendanceReport() {
        return attendanceReport;
    }

    public void setAttendanceReport(AttendanceReport attendanceReport) {
        this.attendanceReport = attendanceReport;
    }

}