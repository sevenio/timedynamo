package com.tvisha.trooptime.activity.activity.ApiPostModels;

/**
 * Created by tvisha on 28/7/18.
 */

public class Leave {
    private String date;
    private String apiDate;
    private String key;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApiDate() {
        return apiDate;
    }

    public void setApiDate(String apiDate) {
        this.apiDate = apiDate;
    }
}
