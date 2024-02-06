package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeekOffsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("weekOffDays")
    @Expose
    private List<String> weekOffDays = null;

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

    public List<String> getWeekOffDays() {
        return weekOffDays;
    }

    public void setWeekOffDays(List<String> weekOffDays) {
        this.weekOffDays = weekOffDays;
    }

}



/*
public class WeekOffsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("weekOffDays")
    @Expose

    private String weekOffDays;

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

    public String getWeekOffDays() {
        return weekOffDays;
    }

    public void setWeekOffDays(String weekOffDays) {
        this.weekOffDays = weekOffDays;
    }


}*/
