package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("attendance_team_tab")
    @Expose
    private boolean attendance_team_tab;
    @SerializedName("attendance_all_tab")
    @Expose
    private boolean attendance_all_tab;
    @SerializedName("request_team_tab")
    @Expose
    private boolean request_team_tab;
    @SerializedName("request_all_tab")
    @Expose
    private boolean request_all_tab;

    @Override
    public String toString() {
        return "UserInfo{" +
                "attendance_team_tab=" + attendance_team_tab +
                ", attendance_all_tab='" + attendance_all_tab + '\'' +
                ", request_team_tab='" + request_team_tab + '\'' +
                ", request_all_tab='" + request_all_tab + '\'' +
                '}';
    }

    public boolean isAttendance_all_tab() {
        return attendance_all_tab;
    }

    public void setAttendance_all_tab(boolean attendance_all_tab) {
        this.attendance_all_tab = attendance_all_tab;
    }

    public boolean isRequest_team_tab() {
        return request_team_tab;
    }

    public void setRequest_team_tab(boolean request_team_tab) {
        this.request_team_tab = request_team_tab;
    }

    public boolean isRequest_all_tab() {
        return request_all_tab;
    }

    public void setRequest_all_tab(boolean request_all_tab) {
        this.request_all_tab = request_all_tab;
    }

    public boolean isAttendance_team_tab() {
        return attendance_team_tab;
    }

    public void setAttendance_team_tab(boolean attendance_team_tab) {
        this.attendance_team_tab = attendance_team_tab;
    }





}
