package com.tvisha.trooptime.activity.activity.apiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeePermissionSummaryResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("permissionData")
    @Expose
    private List<PermissionData> permissionData = null;
    @SerializedName("type")
    @Expose
    private int type;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<PermissionData> getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(List<PermissionData> permissionData) {
        this.permissionData = permissionData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}