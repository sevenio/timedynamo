package com.tvisha.trooptime.activity.activity.ApiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUpdateResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("isUpdate")
    @Expose
    private boolean isUpdate;
    @SerializedName("message")
    @Expose

    private String message;

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

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

}