package com.tvisha.trooptime.activity.activity.ApiPostModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_type")
    @Expose
    private String request_type;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("is_half_day")
    @Expose
    private String is_half_day;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("request_by")
    @Expose
    private String request_by;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("request_by_name")
    @Expose
    private String request_by_name;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("days_count")
    @Expose
    private String days_count;
    @SerializedName("request_to")
    @Expose
    private String request_to;
    @SerializedName("request_cc")
    @Expose
    private String request_cc;
    @SerializedName("accepted_by")
    @Expose
    private String accepted_by;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRequest_cc() {
        return request_cc;
    }

    public void setRequest_cc(String request_cc) {
        this.request_cc = request_cc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_half_day() {
        return is_half_day;
    }

    public void setIs_half_day(String is_half_day) {
        this.is_half_day = is_half_day;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getRequest_by() {
        return request_by;
    }

    public void setRequest_by(String request_by) {
        this.request_by = request_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_by_name() {
        return request_by_name;
    }

    public void setRequest_by_name(String request_by_name) {
        this.request_by_name = request_by_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDays_count() {
        return days_count;
    }

    public void setDays_count(String days_count) {
        this.days_count = days_count;
    }

    public String getRequest_to() {
        return request_to;
    }

    public void setRequest_to(String request_to) {
        this.request_to = request_to;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }

}