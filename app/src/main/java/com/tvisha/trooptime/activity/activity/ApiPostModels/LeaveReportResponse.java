package com.tvisha.trooptime.activity.activity.ApiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveReportResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("leaveReport")
    @Expose
    private LeaveReport leaveReport;

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

    public LeaveReport getLeaveReport() {
        return leaveReport;
    }

    public void setLeaveReport(LeaveReport leaveReport) {
        this.leaveReport = leaveReport;
    }

}