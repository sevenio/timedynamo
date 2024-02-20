package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveReport {

    @SerializedName("sickCount")
    @Expose
    private String sickCount;
    @SerializedName("sickPendingCount")
    @Expose
    private String sickPendingCount;
    @SerializedName("casualCount")
    @Expose
    private String casualCount;
    @SerializedName("optionalCount")
    @Expose
    private String optionalCount;
    @SerializedName("lopCount")
    @Expose
    private String lopCount;
    @SerializedName("earlyLogoutCount")
    @Expose
    private int earlyLogoutCount;
    @SerializedName("lateLoginCount")
    @Expose
    private int lateLoginCount;
    @SerializedName("permissionCount")
    @Expose
    private int permissionCount;
    @SerializedName("lessWorkingDaysCount")
    @Expose
    private int lessWorkingDaysCount;
    @SerializedName("compOffData")
    @Expose
    private List<CompOffDetails> compoffData = null;
    @SerializedName("exception")
    @Expose
    private int exception;
    @SerializedName("autoCheckOut")
    @Expose
    private int autoCheckOut;
    @SerializedName("totalLeaveCount")
    @Expose
    private String totalLeaveCount;

    public String getSickCount() {
        return sickCount;
    }

    public void setSickCount(String sickCount) {
        this.sickCount = sickCount;
    }

    public String getSickPendingCount() {
        return sickPendingCount;
    }

    public void setSickPendingCount(String sickPendingCount) {
        this.sickPendingCount = sickPendingCount;
    }

    public String getCasualCount() {
        return casualCount;
    }

    public void setCasualCount(String casualCount) {
        this.casualCount = casualCount;
    }

    public String getOptionalCount() {
        return optionalCount;
    }

    public void setOptionalCount(String optionalCount) {
        this.optionalCount = optionalCount;
    }

    public String getLopCount() {
        return lopCount;
    }

    public void setLopCount(String lopCount) {
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

    public int getPermissionCount() {
        return permissionCount;
    }

    public void setPermissionCount(int permissionCount) {
        this.permissionCount = permissionCount;
    }

    public int getLessWorkingDaysCount() {
        return lessWorkingDaysCount;
    }

    public void setLessWorkingDaysCount(int lessWorkingDaysCount) {
        this.lessWorkingDaysCount = lessWorkingDaysCount;
    }

    public List<CompOffDetails> getCompoffData() {
        return compoffData;
    }

    public void setCompoffData(List<CompOffDetails> compoffData) {
        this.compoffData = compoffData;
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

    public String getTotalLeaveCount() {
        return totalLeaveCount;
    }

    public void setTotalLeaveCount(String totalLeaveCount) {
        this.totalLeaveCount = totalLeaveCount;
    }
}