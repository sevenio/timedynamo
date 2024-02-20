package com.tvisha.trooptime.activity.activity.apiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeSummaryResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("employeeData")
    @Expose
    private List<EmployeeData> employeeData = null;
    @SerializedName("type")
    @Expose
    private int type;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EmployeeData> getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(List<EmployeeData> employeeData) {
        this.employeeData = employeeData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}