package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterAllAttendenceApiResponce {
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
