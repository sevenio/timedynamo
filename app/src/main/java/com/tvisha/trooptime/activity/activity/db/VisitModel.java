package com.tvisha.trooptime.activity.activity.db;

/**
 * Created by Shiva prasad on 15/11/2016.
 */

public class VisitModel {
    private int ID;
    private String USER_ID;
    private String CheckIn;
    private String CheckOut;
    private String date;
    private String status;
    private String createdAt;
    private int isManual;
    private int sendStatus;
    private int company_id;

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getIsManual() {
        return isManual;
    }

    public void setIsManual(int isManual) {
        this.isManual = isManual;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(String checkIn) {
        CheckIn = checkIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "VisitModel{" +
                "ID=" + ID +
                ", USER_ID='" + USER_ID + '\'' +
                ", CheckIn='" + CheckIn + '\'' +
                ", CheckOut='" + CheckOut + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", isManual=" + isManual +
                ", sendStatus=" + sendStatus +
                '}';
    }

}
