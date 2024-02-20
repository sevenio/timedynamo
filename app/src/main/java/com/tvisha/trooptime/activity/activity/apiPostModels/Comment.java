package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 10/8/18.
 */

/*public class Comment {
}*/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

    public final static Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return (new Comment[size]);
        }

    };
    @SerializedName("comment_id")
    @Expose
    private String comment_id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_type")
    @Expose
    private String comment_type;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("username")
    @Expose
    private String username;

    protected Comment(Parcel in) {
        this.comment_id = ((String) in.readValue((String.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
        this.comment_type = ((String) in.readValue((String.class.getClassLoader())));
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Comment() {
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_type() {
        return comment_type;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(comment_id);
        dest.writeValue(comment);
        dest.writeValue(comment_type);
        dest.writeValue(user_id);
        dest.writeValue(username);
    }

    public int describeContents() {
        return 0;
    }

}