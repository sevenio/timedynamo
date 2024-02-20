package com.tvisha.trooptime.activity.activity.apiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompOffData {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_full_day")
    @Expose
    private int is_full_day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIs_full_day() {
        return is_full_day;
    }

    public void setIs_full_day(int is_full_day) {
        this.is_full_day = is_full_day;
    }

}