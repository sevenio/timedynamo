package com.tvisha.trooptime.activity.activity.apiPostModels;

/*public class ExceptionDetailsResponse {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExceptionDetailsResponse {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("data")
    @Expose
    private ExceptionDetails data;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ExceptionDetails getData() {
        return data;
    }

    public void setData(ExceptionDetails data) {
        this.data = data;
    }

}