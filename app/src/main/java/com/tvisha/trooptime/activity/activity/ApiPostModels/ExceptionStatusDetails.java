package com.tvisha.trooptime.activity.activity.ApiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExceptionStatusDetails {

    @SerializedName("user_comment")
    @Expose
    private String userComment;
    @SerializedName("updated_check_in")
    @Expose
    private String updatedCheckIn;
    @SerializedName("updated_check_out")
    @Expose
    private String updatedCheckOut;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("request_to_users")
    @Expose
    private String requestToUsers;
    @SerializedName("request_cc_users")
    @Expose
    private String requestCcUsers;
    @SerializedName("type_value")
    @Expose
    private String typeValue;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("reporting_boss_id")
    @Expose
    private String reportingBossId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reporting_hr_id")
    @Expose
    private String reportingHrId;
    @SerializedName("accepted_by")
    @Expose
    private String acceptedBy;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    @Override
    public String toString() {
        return "ExceptionStatusDetails{" +
                "userComment='" + userComment + '\'' +
                ", updatedCheckIn='" + updatedCheckIn + '\'' +
                ", updatedCheckOut='" + updatedCheckOut + '\'' +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", requestToUsers='" + requestToUsers + '\'' +
                ", requestCcUsers='" + requestCcUsers + '\'' +
                ", typeValue='" + typeValue + '\'' +
                ", reason='" + reason + '\'' +
                ", label='" + label + '\'' +
                ", reportingBossId='" + reportingBossId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", reportingHrId='" + reportingHrId + '\'' +
                ", acceptedBy='" + acceptedBy + '\'' +
                ", comments=" + comments +
                '}';
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUpdatedCheckIn() {
        return updatedCheckIn;
    }

    public void setUpdatedCheckIn(String updatedCheckIn) {
        this.updatedCheckIn = updatedCheckIn;
    }

    public String getUpdatedCheckOut() {
        return updatedCheckOut;
    }

    public void setUpdatedCheckOut(String updatedCheckOut) {
        this.updatedCheckOut = updatedCheckOut;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReportingBossId() {
        return reportingBossId;
    }

    public void setReportingBossId(String reportingBossId) {
        this.reportingBossId = reportingBossId;
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

    public String getReportingHrId() {
        return reportingHrId;
    }

    public void setReportingHrId(String reportingHrId) {
        this.reportingHrId = reportingHrId;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}