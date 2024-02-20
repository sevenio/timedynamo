package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 9/8/18.
 */
/*
public class SelfRequest {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelfRequest implements Parcelable {

    public final static Parcelable.Creator<SelfRequest> CREATOR = new Creator<SelfRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SelfRequest createFromParcel(Parcel in) {
            return new SelfRequest(in);
        }

        public SelfRequest[] newArray(int size) {
            return (new SelfRequest[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("swap_to")
    @Expose
    private String swap_to;
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
    @SerializedName("request_by")
    @Expose
    private String request_by;
    @SerializedName("request_to")
    @Expose
    private String request_to;
    @SerializedName("request_cc")
    @Expose
    private String request_cc;
    @SerializedName("action_by")
    @Expose
    private String action_by;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("request_by_name")
    @Expose
    private String request_by_name;
    @SerializedName("swap_to_name")
    @Expose
    private String swap_to_name;
    @SerializedName("swap_status")
    @Expose
    private String swap_status;
    @SerializedName("days_count")
    @Expose
    private String days_count;
    @SerializedName("accepted_by")
    @Expose
    private String accepted_by;
    @SerializedName("request_by_user_avatar")
    @Expose
    private String request_by_user_avatar;
    @SerializedName("request_to_users")
    @Expose
    private String request_to_users;
    @SerializedName("request_cc_users")
    @Expose
    private String request_cc_users;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("data")
    @Expose
    private Data data;

    protected SelfRequest(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.request_type = ((String) in.readValue((String.class.getClassLoader())));
        this.reason = ((String) in.readValue((String.class.getClassLoader())));
        this.start_date = ((String) in.readValue((String.class.getClassLoader())));
        this.end_date = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.is_half_day = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.request_by = ((String) in.readValue((String.class.getClassLoader())));
        this.request_to = ((String) in.readValue((String.class.getClassLoader())));
        this.request_cc = ((String) in.readValue((String.class.getClassLoader())));
        this.action_by = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.request_by_name = ((String) in.readValue((String.class.getClassLoader())));
        this.swap_to_name = ((String) in.readValue((String.class.getClassLoader())));
        this.swap_status = ((String) in.readValue((String.class.getClassLoader())));
        this.accepted_by = ((String) in.readValue((String.class.getClassLoader())));
        this.request_by_user_avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.request_to_users = ((String) in.readValue((String.class.getClassLoader())));
        this.request_cc_users = ((String) in.readValue((String.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.comments, (Comment.class.getClassLoader()));
    }

    public SelfRequest() {
    }

    public String getSwap_to() {
        return swap_to;
    }

    public void setSwap_to(String swap_to) {
        this.swap_to = swap_to;
    }

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

    public String getRequest_to() {
        return request_to;
    }

    public void setRequest_to(String request_to) {
        this.request_to = request_to;
    }

    public String getRequest_cc() {
        return request_cc;
    }

    public void setRequest_cc(String request_cc) {
        this.request_cc = request_cc;
    }

    public String getAction_by() {
        return action_by;
    }

    public void setAction_by(String action_by) {
        this.action_by = action_by;
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

    public String getRequest_by_user_avatar() {
        return request_by_user_avatar;
    }

    public void setRequest_by_user_avatar(String request_by_user_avatar) {
        this.request_by_user_avatar = request_by_user_avatar;
    }

    public String getRequest_to_users() {
        return request_to_users;
    }

    public void setRequest_to_users(String request_to_users) {
        this.request_to_users = request_to_users;
    }

    public String getRequest_cc_users() {
        return request_cc_users;
    }

    public void setRequest_cc_users(String request_cc_users) {
        this.request_cc_users = request_cc_users;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSwap_to_name() {
        return swap_to_name;
    }

    public void setSwap_to_name(String swap_to_name) {
        this.swap_to_name = swap_to_name;
    }

    public String getSwap_status() {
        return swap_status;
    }

    public void setSwap_status(String swap_status) {
        this.swap_status = swap_status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }

    public String getDays_count() {
        return days_count;
    }

    public void setDays_count(String days_count) {
        this.days_count = days_count;
    }


    @Override
    public String toString() {
        return "SelfRequest{" +
                "id='" + id + '\'' +
                ", request_type='" + request_type + '\'' +
                ", reason='" + reason + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", type='" + type + '\'' +
                ", is_half_day='" + is_half_day + '\'' +
                ", request_by='" + request_by + '\'' +
                ", request_to='" + request_to + '\'' +
                ", request_cc='" + request_cc + '\'' +
                ", action_by='" + action_by + '\'' +
                ", status='" + status + '\'' +
                ", request_by_name='" + request_by_name + '\'' +
                ", swap_to_name='" + swap_to_name + '\'' +
                ", swap_status='" + swap_status + '\'' +
                ", days_count='" + days_count + '\'' +
                ", accepted_by='" + accepted_by + '\'' +
                ", request_by_user_avatar='" + request_by_user_avatar + '\'' +
                ", request_to_users='" + request_to_users + '\'' +
                ", request_cc_users='" + request_cc_users + '\'' +
                ", created_at='" + created_at + '\'' +
                ", comments=" + comments +
                ", data=" + data +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);

        dest.writeValue(request_type);
        dest.writeValue(reason);
        dest.writeValue(start_date);
        dest.writeValue(end_date);
        dest.writeValue(type);
        dest.writeValue(is_half_day);
        dest.writeValue(data);
        dest.writeValue(request_by);
        dest.writeValue(request_to);
        dest.writeValue(request_cc);
        dest.writeValue(action_by);
        dest.writeValue(status);
        dest.writeValue(request_by_name);
        dest.writeValue(swap_to_name);
        dest.writeValue(swap_status);
        dest.writeValue(days_count);
        dest.writeValue(accepted_by);
        dest.writeValue(request_by_user_avatar);
        dest.writeValue(request_to_users);
        dest.writeValue(request_cc_users);
        dest.writeValue(created_at);
        dest.writeList(comments);
    }

    public int describeContents() {
        return 0;
    }

}