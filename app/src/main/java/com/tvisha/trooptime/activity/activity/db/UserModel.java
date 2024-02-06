package com.tvisha.trooptime.activity.activity.db;

import java.io.Serializable;

/**
 * Created by Shiva prasad on 27/10/2016.
 */
public class UserModel implements Serializable {
    int id;
    String user_id;
    String name;
    String email;
    String mobile;
    String userImage;
    String thumbImage;

    String emp_id;
    String department;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userImage='" + userImage + '\'' +
                ", thumbImage='" + thumbImage + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
