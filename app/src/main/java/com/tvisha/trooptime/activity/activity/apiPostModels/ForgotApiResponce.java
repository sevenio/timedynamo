package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotApiResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}

