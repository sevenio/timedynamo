package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 11/8/18.
 */
/*
public class Break {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Break implements Parcelable {

    public final static Parcelable.Creator<Break> CREATOR = new Creator<Break>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Break createFromParcel(Parcel in) {
            return new Break(in);
        }

        public Break[] newArray(int size) {
            return (new Break[size]);
        }

    };
    @SerializedName("break_id")
    @Expose
    private String break_id;
    @SerializedName("break_name")
    @Expose
    private String break_name;
    @SerializedName("start_time")
    @Expose
    private String start_time;
    @SerializedName("end_time")
    @Expose
    private String end_time;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("check_in_break")
    @Expose
    private String check_in_break;
    @SerializedName("check_out_break")
    @Expose
    private String check_out_break;

    protected Break(Parcel in) {
        this.break_id = ((String) in.readValue((String.class.getClassLoader())));
        this.break_name = ((String) in.readValue((String.class.getClassLoader())));
        this.start_time = ((String) in.readValue((String.class.getClassLoader())));
        this.end_time = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.check_in_break = ((String) in.readValue((String.class.getClassLoader())));
        this.check_out_break = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Break() {
    }

    public String getBreak_id() {
        return break_id;
    }

    public void setBreak_id(String break_id) {
        this.break_id = break_id;
    }

    public String getBreak_name() {
        return break_name;
    }

    public void setBreak_name(String break_name) {
        this.break_name = break_name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheck_in_break() {
        return check_in_break;
    }

    public void setCheck_in_break(String check_in_break) {
        this.check_in_break = check_in_break;
    }

    public String getCheck_out_break() {
        return check_out_break;
    }

    public void setCheck_out_break(String check_out_break) {
        this.check_out_break = check_out_break;
    }

    @Override
    public String toString() {
        return "Break{" +
                "break_id='" + break_id + '\'' +
                ", break_name='" + break_name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", type='" + type + '\'' +
                ", check_in_break='" + check_in_break + '\'' +
                ", check_out_break='" + check_out_break + '\'' +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(break_id);
        dest.writeValue(break_name);
        dest.writeValue(start_time);
        dest.writeValue(end_time);
        dest.writeValue(type);
        dest.writeValue(check_in_break);
        dest.writeValue(check_out_break);
    }

    public int describeContents() {
        return 0;
    }

}