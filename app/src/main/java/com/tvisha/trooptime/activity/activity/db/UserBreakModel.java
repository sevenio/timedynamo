package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by akhil on 17/5/18.
 */

public class UserBreakModel {

    private int id;
    private int userBreakId;
    private int breakId;
    private int aId;
    private int attendanceId;
    private String startTime;
    private String endTime;
    private int isSync;
    private String referenceId;
    private int type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserBreakId() {
        return userBreakId;
    }

    public void setUserBreakId(int userBreakId) {
        this.userBreakId = userBreakId;
    }

    public int getBreakId() {
        return breakId;
    }

    public void setBreakId(int breakId) {
        this.breakId = breakId;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
