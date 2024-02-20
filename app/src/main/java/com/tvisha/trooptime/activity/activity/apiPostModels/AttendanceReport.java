package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceReport {

    @SerializedName("workingDays")
    @Expose
    private int workingDays;
    @SerializedName("permissionCount")
    @Expose
    private int permissionCount;
    @SerializedName("leaveCount")
    @Expose
    private double leaveCount;
    @SerializedName("lopCount")
    @Expose
    private double lopCount;
    @SerializedName("earlyLogoutCount")
    @Expose
    private int earlyLogoutCount;
    @SerializedName("lateLoginCount")
    @Expose
    private int lateLoginCount;
    @SerializedName("lessWorkingDaysCount")
    @Expose
    private int lessWorkingDaysCount;
    @SerializedName("compOffData")
    @Expose
    private List<CompOffDetails> compOffData = null;
    @SerializedName("exception")
    @Expose
    private int exception;
    @SerializedName("autoCheckOut")
    @Expose
    private int autoCheckOut;

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public int getPermissionCount() {
        return permissionCount;
    }

    public void setPermissionCount(int permissionCount) {
        this.permissionCount = permissionCount;
    }

    public double getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public double getLopCount() {
        return lopCount;
    }

    public void setLopCount(int lopCount) {
        this.lopCount = lopCount;
    }

    public int getEarlyLogoutCount() {
        return earlyLogoutCount;
    }

    public void setEarlyLogoutCount(int earlyLogoutCount) {
        this.earlyLogoutCount = earlyLogoutCount;
    }

    public int getLateLoginCount() {
        return lateLoginCount;
    }

    public void setLateLoginCount(int lateLoginCount) {
        this.lateLoginCount = lateLoginCount;
    }

    public int getLessWorkingDaysCount() {
        return lessWorkingDaysCount;
    }

    public void setLessWorkingDaysCount(int lessWorkingDaysCount) {
        this.lessWorkingDaysCount = lessWorkingDaysCount;
    }

    public List<CompOffDetails> getCompOffData() {
        return compOffData;
    }

    public void setCompOffData(List<CompOffDetails> compOffData) {
        this.compOffData = compOffData;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public int getAutoCheckOut() {
        return autoCheckOut;
    }

    public void setAutoCheckOut(int autoCheckOut) {
        this.autoCheckOut = autoCheckOut;
    }

}