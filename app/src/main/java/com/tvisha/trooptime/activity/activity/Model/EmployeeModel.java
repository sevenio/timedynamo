package com.tvisha.trooptime.activity.activity.Model;

/**
 * Created by tvisha on 20/2/17.
 */
public class EmployeeModel {
    private String employee_Id;
    private String employee_name;
    private boolean checked = false;

    public boolean getChecked() {
        return checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getEmployee_Id() {
        return employee_Id;
    }

    public void setEmployee_Id(String employee_Id) {
        this.employee_Id = employee_Id;
    }

    public String getEmployee_name() {
        return employee_name;

    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
}
