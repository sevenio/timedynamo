package com.tvisha.trooptime.activity.activity.Model;

public class Notification {
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long ID;
    public String notificationdata;
    public int notificationtype;

    public String getnotificationdata() {
        return notificationdata;
    }

    public void setnotificationdata(String notificationdata) {
        this.notificationdata = notificationdata;
    }

    public int getnotificationtype() {
        return notificationtype;
    }

    public void setnotificationtype(int notificationtype) {
        this.notificationtype = notificationtype;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String created_at;
    public String updated_at;
}
