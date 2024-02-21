package com.tvisha.trooptime.activity.activity.model;

import java.io.Serializable;

/**
 * Created by tvisha on 10/2/17.
 */
public class TimeAndAttendenceModel implements Serializable {
    private String selfUser_Id;
    private String selfDate;
    private String selfCheck_in_time;
    private String selfCheck_out_time;
    private String selfWorkied_time;
    private String employeeUser_Id;
    private String employeeFull_Name;
    private String employeeEmail;
    private String reportingBossId;
    private String user_Id;
    private String emp_Id;
    private String check_in_time;
    private String check_out_time;
    private String date;
    private String workedTime;
    private String breakTime;
    private String breakStartTime;
    private String breakEndTime;
    private String breakStartCount;
    private String breakEndCount;
    private String userName;
    private String userAvatar;
    private String leaveApproved;
    private String leaveStatus;
    private String leaveType;
    private String holidayName;
    private String remarks;
    private String empStatus;
    private String empRemarks;

    public void setSelfUser_Id(String selfUser_Id) {
        this.selfUser_Id = selfUser_Id;
    }

    public void setEmployeeUser_Id(String employeeUser_Id) {
        this.employeeUser_Id = employeeUser_Id;
    }

    public void setEmployeeFull_Name(String employeeFull_Name) {
        this.employeeFull_Name = employeeFull_Name;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setReportingBossId(String reportingBossId) {
        this.reportingBossId = reportingBossId;
    }

    public String getSelfDate() {
        return selfDate;
    }

    public void setSelfDate(String selfDate) {
        this.selfDate = selfDate;
    }

    public String getSelfCheck_in_time() {
        return selfCheck_in_time;
    }

    public void setSelfCheck_in_time(String selfCheck_in_time) {
        this.selfCheck_in_time = selfCheck_in_time;
    }

    public String getSelfCheck_out_time() {
        return selfCheck_out_time;
    }

    public void setSelfCheck_out_time(String selfCheck_out_time) {
        this.selfCheck_out_time = selfCheck_out_time;
    }

    public String getSelfWorkied_time() {
        return selfWorkied_time;
    }

    public void setSelfWorkied_time(String selfWorkied_time) {
        this.selfWorkied_time = selfWorkied_time;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getEmp_Id() {
        return emp_Id;
    }

    public void setEmp_Id(String emp_Id) {
        this.emp_Id = emp_Id;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public String getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(String workedTime) {
        this.workedTime = workedTime;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(String breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public String getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(String breakEndTime) {
        this.breakEndTime = breakEndTime;
    }

    public String getBreakStartCount() {
        return breakStartCount;
    }

    public void setBreakStartCount(String breakStartCount) {
        this.breakStartCount = breakStartCount;
    }

    public String getBreakEndCount() {
        return breakEndCount;
    }

    public void setBreakEndCount(String breakEndCount) {
        this.breakEndCount = breakEndCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeaveApproved() {
        return leaveApproved;
    }

    public void setLeaveApproved(String leaveApproved) {
        this.leaveApproved = leaveApproved;
    }

    public String getEmpRemarks() {
        return empRemarks;
    }

    public void setEmpRemarks(String empRemarks) {
        this.empRemarks = empRemarks;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
