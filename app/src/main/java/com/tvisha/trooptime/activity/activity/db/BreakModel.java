package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by akhil on 16/5/18.
 */

public class BreakModel {

    private int breakId;
    private int shiftId;
    private String breakName;
    private String breakTime;

    public int getBreakId() {
        return breakId;
    }

    public void setBreakId(int breakId) {
        this.breakId = breakId;
    }

    public String getBreakName() {
        return breakName;
    }

    public void setBreakName(String breakName) {
        this.breakName = breakName;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }
}
