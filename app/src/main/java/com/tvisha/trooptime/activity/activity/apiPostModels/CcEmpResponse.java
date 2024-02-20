package com.tvisha.trooptime.activity.activity.apiPostModels;

/*public class CcEmpResponse {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CcEmpResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_list")
    @Expose
    private List<EmployeeDetails> employee_list = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EmployeeDetails> getEmployee_list() {
        return employee_list;
    }

    public void setEmployee_list(List<EmployeeDetails> employee_list) {
        this.employee_list = employee_list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}