package com.tvisha.trooptime.activity.activity.ApiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAwsConfigResponse {


    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("base_url")
    @Expose
    private String base_url;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}