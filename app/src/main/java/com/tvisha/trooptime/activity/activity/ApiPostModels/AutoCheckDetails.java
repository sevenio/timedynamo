package com.tvisha.trooptime.activity.activity.ApiPostModels;

/*public class AutoCheckDetails {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/* "data": {
         "exception_id": "146",
         "exception_type": "3",
         "updated_check_in": "0000-00-00 00:00:00",
         "updated_check_out": "2019-05-15 23:30:00",
         "check_in": "2019-05-15 09:31:02",
         "check_out": "2019-05-15 23:30:00",
         "date": "2019-05-15",
         "type": "0",
         "status": "1",
         "request_to_users": "Admin , Suman Jampala , Pavan kumar",
         "request_cc_users": "Mounika",
         "reporting_boss_id": ",1,15,16,",
         "user_id": "14",
         "name": "Ram Krishna Devansha",
         "reporting_hr_id": ",7,",
         "reporting_permission_id": ",24,",
         "accepted_by": "Admin",
         "comments": []
         }*/

public class AutoCheckDetails {

    @SerializedName("exception_id")
    @Expose
    private String exception_id;
    @SerializedName("exception_type")
    @Expose
    private String exception_type;
    @SerializedName("updated_check_in")
    @Expose
    private String updated_check_in;
    @SerializedName("updated_check_out")
    @Expose
    private String updated_check_out;
    @SerializedName("check_in")
    @Expose
    private String check_in;
    @SerializedName("check_out")
    @Expose
    private String check_out;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reporting_permission_id")
    @Expose
    private String reporting_permission_id;
    @SerializedName("accepted_by")
    @Expose
    private String accepted_by;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("request_to_users")
    @Expose
    private String requestToUsers;
    @SerializedName("request_cc_users")
    @Expose
    private String requestCcUsers;
    @SerializedName("request_id")
    @Expose
    private String request_id;
    @SerializedName("reporting_boss_id")
    @Expose
    private String reportingBossId;
    @SerializedName("reporting_hr_id")
    @Expose
    private String reportingHrId;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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


    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getException_id() {
        return exception_id;
    }

    public void setException_id(String exception_id) {
        this.exception_id = exception_id;
    }

    public String getException_type() {
        return exception_type;
    }

    public void setException_type(String exception_type) {
        this.exception_type = exception_type;
    }

    public String getUpdated_check_in() {
        return updated_check_in;
    }

    public void setUpdated_check_in(String updated_check_in) {
        this.updated_check_in = updated_check_in;
    }

    public String getUpdated_check_out() {
        return updated_check_out;
    }

    public void setUpdated_check_out(String updated_check_out) {
        this.updated_check_out = updated_check_out;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReporting_permission_id() {
        return reporting_permission_id;
    }

    public void setReporting_permission_id(String reporting_permission_id) {
        this.reporting_permission_id = reporting_permission_id;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestToUsers() {
        return requestToUsers;
    }

    public void setRequestToUsers(String requestToUsers) {
        this.requestToUsers = requestToUsers;
    }

    public String getRequestCcUsers() {
        return requestCcUsers;
    }

    public void setRequestCcUsers(String requestCcUsers) {
        this.requestCcUsers = requestCcUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}