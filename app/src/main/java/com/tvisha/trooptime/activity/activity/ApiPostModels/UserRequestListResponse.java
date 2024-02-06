package com.tvisha.trooptime.activity.activity.ApiPostModels;

/**
 * Created by tvisha on 9/8/18.
 */

/*
public class UserRequestListResponse {
}
*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRequestListResponse implements Parcelable {

    public final static Parcelable.Creator<UserRequestListResponse> CREATOR = new Creator<UserRequestListResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserRequestListResponse createFromParcel(Parcel in) {
            return new UserRequestListResponse(in);
        }

        public UserRequestListResponse[] newArray(int size) {
            return (new UserRequestListResponse[size]);
        }

    };
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestData")
    @Expose
    private List<SelfRequest> requestData = null;

    protected UserRequestListResponse(Parcel in) {
        this.success = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.requestData, (SelfRequest.class.getClassLoader()));
    }

    public UserRequestListResponse() {
    }

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

    public List<SelfRequest> getRequestData() {
        return requestData;
    }

    public void setRequestData(List<SelfRequest> requestData) {
        this.requestData = requestData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(requestData);
    }

    public int describeContents() {
        return 0;
    }

}


