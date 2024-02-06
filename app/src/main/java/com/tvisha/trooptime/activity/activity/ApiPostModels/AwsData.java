package com.tvisha.trooptime.activity.activity.ApiPostModels;

/*public class AwsData {
}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AwsData {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("bucket")
    @Expose
    private String bucket;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}