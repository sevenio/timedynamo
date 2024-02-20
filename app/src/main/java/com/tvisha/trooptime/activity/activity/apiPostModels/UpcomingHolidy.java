package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 11/8/18.
 */
/*
public class UpcomingHolidy {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingHolidy implements Parcelable {

    public final static Parcelable.Creator<UpcomingHolidy> CREATOR = new Creator<UpcomingHolidy>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UpcomingHolidy createFromParcel(Parcel in) {
            return new UpcomingHolidy(in);
        }

        public UpcomingHolidy[] newArray(int size) {
            return (new UpcomingHolidy[size]);
        }

    };
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private String type;

    protected UpcomingHolidy(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UpcomingHolidy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(date);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }

}