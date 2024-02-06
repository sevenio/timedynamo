package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendanceList implements Serializable {

    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("is_checkin_manual")
    @Expose
    private String isCheckinManual;
    @SerializedName("is_checkout_manual")
    @Expose
    private String isCheckoutManual;
/*    @SerializedName("breaks")
    @Expose
    private List<Object> breaks = null;*/

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getIsCheckinManual() {
        return isCheckinManual;
    }

    public void setIsCheckinManual(String isCheckinManual) {
        this.isCheckinManual = isCheckinManual;
    }

    public String getIsCheckoutManual() {
        return isCheckoutManual;
    }

    public void setIsCheckoutManual(String isCheckoutManual) {
        this.isCheckoutManual = isCheckoutManual;
    }
/*
    public List<Object> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Object> breaks) {
        this.breaks = breaks;
    }*/

}
