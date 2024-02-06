package com.tvisha.trooptime.activity.activity.ApiPostModels;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomePageResponse implements Parcelable {

    public final static Parcelable.Creator<HomePageResponse> CREATOR = new Creator<HomePageResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HomePageResponse createFromParcel(Parcel in) {
            return new HomePageResponse(in);
        }

        public HomePageResponse[] newArray(int size) {
            return (new HomePageResponse[size]);
        }

    };
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
    private List<UpcomingEvent> upcomingEvents = new ArrayList<>();
    @SerializedName("upcomingHolidys")
    @Expose
    private List<UpcomingHolidy> upcomingHolidys = new ArrayList<>();
    @SerializedName("request_self")
    @Expose
    private List<SelfRequest> request_self = new ArrayList<>();
    @SerializedName("request_team")
    @Expose
    private List<Request_team> request_team = new ArrayList<>();
    @SerializedName("todayEvents")
    @Expose
    private List<UpcomingEvent> todayEvents = new ArrayList<>();
    @SerializedName("todayHolidays")
    @Expose
    private List<UpcomingHolidy> todayHolidays = new ArrayList<>();

    protected HomePageResponse(Parcel in) {
        this.success = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.today = ((Today) in.readValue((Today.class.getClassLoader())));
        in.readList(this.upcomingEvents, (UpcomingEvent.class.getClassLoader()));
        in.readList(this.upcomingHolidys, (UpcomingHolidy.class.getClassLoader()));
        in.readList(this.request_self, (Request_self.class.getClassLoader()));
        in.readList(this.request_team, (Request_team.class.getClassLoader()));
        in.readList(this.todayEvents, (UpcomingEvent.class.getClassLoader()));
        in.readList(this.todayHolidays, (UpcomingHolidy.class.getClassLoader()));
    }

    public HomePageResponse() {
    }

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

    public List<SelfRequest> getRequest_self() {
        return request_self;
    }

    public void setRequest_self(List<SelfRequest> request_self) {
        this.request_self = request_self;
    }

    public List<Request_team> getRequest_team() {
        return request_team;
    }

    public void setRequest_team(List<Request_team> request_team) {
        this.request_team = request_team;
    }

    public List<UpcomingEvent> getTodayEvents() {
        return todayEvents;
    }

    public void setTodayEvents(List<UpcomingEvent> todayEvents) {
        this.todayEvents = todayEvents;
    }

    public List<UpcomingHolidy> getTodayHolidays() {
        return todayHolidays;
    }

    public void setTodayHolidays(List<UpcomingHolidy> todayHolidays) {
        this.todayHolidays = todayHolidays;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(today);
        dest.writeList(upcomingEvents);
        dest.writeList(upcomingHolidys);
        dest.writeList(request_self);
        dest.writeList(request_team);
        dest.writeList(todayEvents);
        dest.writeList(todayHolidays);
    }

    public int describeContents() {
        return 0;
    }

}