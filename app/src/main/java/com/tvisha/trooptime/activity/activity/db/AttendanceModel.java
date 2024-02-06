package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by akhil on 16/5/18.
 */

public class AttendanceModel {

    private int id;
    private int attendanceId;
    private String userId;
    private String checkIn;
    private String checkOut;
    private int isCheckinManual;
    private int isCheckoutManual;
    private int isSync;
    private String referenceId;
    private int check_in_branch_id;
    private int check_out_branch_id;

    public int getCheck_in_branch_id() {
        return check_in_branch_id;
    }

    public void setCheck_in_branch_id(int check_in_branch_id) {
        this.check_in_branch_id = check_in_branch_id;
    }

    public int getCheck_out_branch_id() {
        return check_out_branch_id;
    }

    public void setCheck_out_branch_id(int check_out_branch_id) {
        this.check_out_branch_id = check_out_branch_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getIsCheckinManual() {
        return isCheckinManual;
    }

    public void setIsCheckinManual(int isCheckinManual) {
        this.isCheckinManual = isCheckinManual;
    }

    public int getIsCheckoutManual() {
        return isCheckoutManual;
    }

    public void setIsCheckoutManual(int isCheckoutManual) {
        this.isCheckoutManual = isCheckoutManual;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
