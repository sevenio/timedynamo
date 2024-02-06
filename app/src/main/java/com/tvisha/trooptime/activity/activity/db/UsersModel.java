package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by akhil on 16/5/18.
 */

public class UsersModel {

    private int userId;
    private String name;
    private String displayName;
    private String role;
    private String email;
    private String mobile;
    private String userAvatar;
    private String fingerPrint;
    private String empId;
    private String shiftId;
    private String status;
    private String reportingBossId;
    private String reportingHrId;
    private String reportingPermissionId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportingBossId() {
        return reportingBossId;
    }

    public void setReportingBossId(String reportingBossId) {
        this.reportingBossId = reportingBossId;
    }

    public String getReportingHrId() {
        return reportingHrId;
    }

    public void setReportingHrId(String reportingHrId) {
        this.reportingHrId = reportingHrId;
    }

    public String getReportingPermissionId() {
        return reportingPermissionId;
    }

    public void setReportingPermissionId(String reportingPermissionId) {
        this.reportingPermissionId = reportingPermissionId;
    }
}
