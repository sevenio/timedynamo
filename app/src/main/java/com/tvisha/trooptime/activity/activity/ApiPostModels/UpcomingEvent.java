package com.tvisha.trooptime.activity.activity.ApiPostModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tvisha on 11/8/18.
 */
/*

public class UpcomingEvent {
}
*/


public class UpcomingEvent implements Parcelable {

    public final static Parcelable.Creator<UpcomingEvent> CREATOR = new Creator<UpcomingEvent>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UpcomingEvent createFromParcel(Parcel in) {
            return new UpcomingEvent(in);
        }

        public UpcomingEvent[] newArray(int size) {
            return (new UpcomingEvent[size]);
        }

    };
    @SerializedName("tmp_date")
    @Expose
    private String tmp_date;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String display_name;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_avatar")
    @Expose
    private String user_avatar;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("event_type")
    @Expose
    private String event_type;
    @SerializedName("years")
    @Expose
    private int years;

    protected UpcomingEvent(Parcel in) {
        this.tmp_date = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.display_name = ((String) in.readValue((String.class.getClassLoader())));
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.user_avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.event_type = ((String) in.readValue((String.class.getClassLoader())));
        this.years = ((int) in.readValue((int.class.getClassLoader())));
    }

    public UpcomingEvent() {
    }

    public String getTmp_date() {
        return tmp_date;
    }

    public void setTmp_date(String tmp_date) {
        this.tmp_date = tmp_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tmp_date);
        dest.writeValue(name);
        dest.writeValue(display_name);
        dest.writeValue(user_id);
        dest.writeValue(user_avatar);
        dest.writeValue(date);
        dest.writeValue(event_type);
        dest.writeValue(years);
    }

    public int describeContents() {
        return 0;
    }

}