package com.tvisha.trooptime.activity.activity.model;

/**
 * Created by tvisha on 9/2/17.
 */
public class TroopTimeModel {
    private String empUserId;
    private String empId;
    private String userAvatar;
    private String userName;
    private String checkIn;
    private String date;
    private String checkOut;
    private String startBreakTime;
    private String endBreakTime;
    private String breakStartCount;
    private String breakEndCount;
    private String workTime;
    private String selfUser_Id;
    private String selfDate;
    private String selfCheck_in_time;
    private String selfCheck_out_time;
    private String selfWorkied_time;

    public void setEmpUserId(String empUserId) {
        this.empUserId = empUserId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartBreakTime(String startBreakTime) {
        this.startBreakTime = startBreakTime;
    }

    public void setEndBreakTime(String endBreakTime) {
        this.endBreakTime = endBreakTime;
    }

    public void setBreakStartCount(String breakStartCount) {
        this.breakStartCount = breakStartCount;
    }

    public void setBreakEndCount(String breakEndCount) {
        this.breakEndCount = breakEndCount;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setSelfUser_Id(String selfUser_Id) {
        this.selfUser_Id = selfUser_Id;
    }

    public void setSelfDate(String selfDate) {
        this.selfDate = selfDate;
    }

    public void setSelfCheck_in_time(String selfCheck_in_time) {

        this.selfCheck_in_time = selfCheck_in_time;
    }

    public void setSelfCheck_out_time(String selfCheck_out_time) {
        this.selfCheck_out_time = selfCheck_out_time;
    }

    public void setSelfWorkied_time(String selfWorkied_time) {
        this.selfWorkied_time = selfWorkied_time;
    }
}
