package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 24/8/18.
 */

/*
public class SendCommentResponse {
}
*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendCommentResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CommentData data;

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

    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

}