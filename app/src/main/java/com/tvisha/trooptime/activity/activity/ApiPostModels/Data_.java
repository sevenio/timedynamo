package com.tvisha.trooptime.activity.activity.ApiPostModels;

/**
 * Created by tvisha on 22/8/18.
 */

/*
public class Data_ {
}
*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data_ implements Parcelable {

    public final static Parcelable.Creator<Data_> CREATOR = new Creator<Data_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data_ createFromParcel(Parcel in) {
            return new Data_(in);
        }

        public Data_[] newArray(int size) {
            return (new Data_[size]);
        }

    };
    @SerializedName("2018-08-22")
    @Expose
    private String _2018_08_22;
    @SerializedName("2018-08-23")
    @Expose
    private String _2018_08_23;
    @SerializedName("2018-08-24")
    @Expose
    private String _2018_08_24;

    protected Data_(Parcel in) {
        this._2018_08_22 = ((String) in.readValue((String.class.getClassLoader())));
        this._2018_08_23 = ((String) in.readValue((String.class.getClassLoader())));
        this._2018_08_24 = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data_() {
    }

    public String get2018_08_22() {
        return _2018_08_22;
    }

    public void set2018_08_22(String _2018_08_22) {
        this._2018_08_22 = _2018_08_22;
    }

    public String get2018_08_23() {
        return _2018_08_23;
    }

    public void set2018_08_23(String _2018_08_23) {
        this._2018_08_23 = _2018_08_23;
    }

    public String get2018_08_24() {
        return _2018_08_24;
    }

    public void set2018_08_24(String _2018_08_24) {
        this._2018_08_24 = _2018_08_24;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_2018_08_22);
        dest.writeValue(_2018_08_23);
        dest.writeValue(_2018_08_24);
    }

    public int describeContents() {
        return 0;
    }

}