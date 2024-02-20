package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 11/8/18.
 */
/*
public class Attendance_list {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Attendance_list implements Parcelable {

    public final static Parcelable.Creator<Attendance_list> CREATOR = new Creator<Attendance_list>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Attendance_list createFromParcel(Parcel in) {
            return new Attendance_list(in);
        }

        public Attendance_list[] newArray(int size) {
            return (new Attendance_list[size]);
        }

    };
    @SerializedName("check_in")
    @Expose
    private String check_in;
    @SerializedName("check_out")
    @Expose
    private String check_out;
    @SerializedName("is_checkin_manual")
    @Expose
    private String is_checkin_manual;
    @SerializedName("is_checkout_manual")
    @Expose
    private String is_checkout_manual;
    @SerializedName("breaks")
    @Expose
    private List<Break> breaks = new ArrayList<>();

    protected Attendance_list(Parcel in) {
        this.check_in = ((String) in.readValue((String.class.getClassLoader())));
        this.check_out = ((String) in.readValue((String.class.getClassLoader())));
        this.is_checkin_manual = ((String) in.readValue((String.class.getClassLoader())));
        this.is_checkout_manual = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.breaks, (Break.class.getClassLoader()));
    }

    public Attendance_list() {
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getIs_checkin_manual() {
        return is_checkin_manual;
    }

    public void setIs_checkin_manual(String is_checkin_manual) {
        this.is_checkin_manual = is_checkin_manual;
    }

    public String getIs_checkout_manual() {
        return is_checkout_manual;
    }

    public void setIs_checkout_manual(String is_checkout_manual) {
        this.is_checkout_manual = is_checkout_manual;
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(check_in);
        dest.writeValue(check_out);
        dest.writeValue(is_checkin_manual);
        dest.writeValue(is_checkout_manual);
        dest.writeList(breaks);
    }

    public int describeContents() {
        return 0;
    }

}