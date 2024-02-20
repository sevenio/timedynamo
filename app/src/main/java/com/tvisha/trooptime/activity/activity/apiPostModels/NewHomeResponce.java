package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tvisha on 7/9/18.
 */

public class NewHomeResponce {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("today")
    @Expose
    private Today today;
    @SerializedName("upcomingEvents")
    @Expose
    private List<UpcomingEvent> upcomingEvents = null;
    @SerializedName("upcomingHolidys")
    @Expose
    private List<UpcomingHolidy> upcomingHolidys = null;
    @SerializedName("request_self")
    @Expose
    private List<Request_self> request_self = null;
    @SerializedName("request_team")
    @Expose
    private List<Request_team> request_team = null;

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

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }

    public List<UpcomingEvent> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(List<UpcomingEvent> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public List<UpcomingHolidy> getUpcomingHolidys() {
        return upcomingHolidys;
    }

    public void setUpcomingHolidys(List<UpcomingHolidy> upcomingHolidys) {
        this.upcomingHolidys = upcomingHolidys;
    }

    public List<Request_self> getRequest_self() {
        return request_self;
    }

    public void setRequest_self(List<Request_self> request_self) {
        this.request_self = request_self;
    }

    public List<Request_team> getRequest_team() {
        return request_team;
    }

    public void setRequest_team(List<Request_team> request_team) {
        this.request_team = request_team;
    }
}
