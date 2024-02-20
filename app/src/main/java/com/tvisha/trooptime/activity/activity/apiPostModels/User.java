package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("user_avatar")
    @Expose
    private String userAvatar;
    @SerializedName("company_id")
    @Expose
    private String companyId;

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public List<String> getReporting_cc() {
        return reporting_cc;
    }

    public void setReporting_cc(List<String> reporting_cc) {
        this.reporting_cc = reporting_cc;
    }

    @SerializedName("api_key")
    @Expose
    private String apiKey;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("reporting_boss_id")
    @Expose
    private String reportingBossId;
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("is_verified")
    @Expose
    private String is_verified;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("is_cc_exist")
    @Expose
    private boolean is_cc_exist;
    @SerializedName("is_to_exists")
    @Expose
    private boolean is_to_exists;
    @SerializedName("company_branch")
    @Expose
    private String company_branch;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("reporting_cc")
    @Expose
    private List<String> reporting_cc;



    public String getCompany_branch() {
        return company_branch;
    }

    public void setCompany_branch(String company_branch) {
        this.company_branch = company_branch;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getEmp_id() {
        return empId;
    }

    public void setEmp_id(String emp_id) {
        this.empId = emp_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReporting_boss_id() {
        return reportingBossId;
    }

    public void setReporting_boss_id(String reporting_boss_id) {
        this.reportingBossId = reporting_boss_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReportingBossId() {
        return reportingBossId;
    }

    public void setReportingBossId(String reportingBossId) {
        this.reportingBossId = reportingBossId;
    }

    public boolean isIs_to_exists() {
        return is_to_exists;
    }

    public void setIs_to_exists(boolean is_to_exists) {
        this.is_to_exists = is_to_exists;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public boolean isIs_cc_exist() {
        return is_cc_exist;
    }

    public void setIs_cc_exist(boolean is_cc_exist) {
        this.is_cc_exist = is_cc_exist;
    }

    public String getComapanyBranch() {
        return company_branch;
    }

    public void setCompanyBranch(String company_branch) {
        this.company_branch = company_branch;
    }
}
