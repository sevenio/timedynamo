package com.tvisha.trooptime.activity.activity.ApiPostModels;

/*public class ExceptionStatusResponse {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExceptionStatusResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ExceptionStatusDetails data;

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

    public ExceptionStatusDetails getData() {
        return data;
    }

    public void setData(ExceptionStatusDetails data) {
        this.data = data;
    }

}