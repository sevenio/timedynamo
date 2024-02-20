package com.tvisha.trooptime.activity.activity.apiPostModels;

/**
 * Created by tvisha on 24/8/18.
 */

/*public class CommentData {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentData {

    @SerializedName("comment_id")
    @Expose
    private String comment_id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_type")
    @Expose
    private String comment_type;

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

}