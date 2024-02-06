package com.tvisha.trooptime.activity.activity.ApiPostModels;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Breaks_list implements Parcelable {

    public final static Parcelable.Creator<Breaks_list> CREATOR = new Creator<Breaks_list>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Breaks_list createFromParcel(Parcel in) {
            return new Breaks_list(in);
        }

        public Breaks_list[] newArray(int size) {
            return (new Breaks_list[size]);
        }

    };
    @SerializedName("breaks")
    @Expose
    private List<Break> breaks = null;
    @SerializedName("transit")
    @Expose
    private List<Break> transit = null;
    @SerializedName("others")
    @Expose
    private List<Break> others = null;
    @SerializedName("sales")
    @Expose
    private List<Break> sales = null;

    protected Breaks_list(Parcel in) {
        in.readList(this.breaks, (Break.class.getClassLoader()));
        in.readList(this.transit, (Break.class.getClassLoader()));
        in.readList(this.others, (Break.class.getClassLoader()));
        in.readList(this.sales, (Break.class.getClassLoader()));
    }

    public Breaks_list() {
    }

    @Override
    public String toString() {
        return "Breaks_list{" +
                "breaks=" + breaks +
                ", transit=" + transit +
                ", others=" + others +
                ", sales=" + sales +
                '}';
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks;
    }

    public List<Break> getTransit() {
        return transit;
    }

    public void setTransit(List<Break> transit) {
        this.transit = transit;
    }

    public List<Break> getOthers() {
        return others;
    }

    public void setOthers(List<Break> others) {
        this.others = others;
    }

    public List<Break> getSales() {
        return sales;
    }

    public void setSales(List<Break> sales) {
        this.sales = sales;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(breaks);
        dest.writeList(transit);
        dest.writeList(others);
        dest.writeList(sales);
    }

    public int describeContents() {
        return 0;
    }

}