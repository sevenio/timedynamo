package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 10/8/18.
 */
/*
public class Data {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };
    @SerializedName("start_time")
    @Expose
    private String start_time;
    @SerializedName("end_time")
    @Expose
    private String end_time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("ot_duration")
    @Expose
    private String ot_duration;

    protected Data(Parcel in) {
        this.start_time = ((String) in.readValue((String.class.getClassLoader())));
        this.end_time = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.ot_duration = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getOt_duration() {
        return ot_duration;
    }

    public void setOt_duration(String ot_duration) {
        this.ot_duration = ot_duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(start_time);
        dest.writeValue(end_time);
        dest.writeValue(date);
        dest.writeValue(ot_duration);

    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Data{" +
                "start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", date='" + date + '\'' +
                ", ot_duration='" + ot_duration + '\'' +
                '}';
    }

}