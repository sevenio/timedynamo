package com.tvisha.trooptime.activity.activity.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;

import java.util.List;

/**
 * Created by tvisha on 26/9/18.
 */

public class SelfAttendenceApiResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("attendance")
    @Expose
    private List<Attendance> attendance = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }
}
