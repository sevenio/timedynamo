package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tvisha.trooptime.activity.activity.Api_Helper.ApiClient;
import com.tvisha.trooptime.activity.activity.Helper.ServerUrls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginApi extends ApiClient {

    public static ApiInterface getApiService() {
        return getClient().create(ApiInterface.class);
    }

    public static interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.Loginurl)
        Call<String> getLoginDetails(
                @Field("username") String username,
                @Field("password") String password,
                @Field("token") String token
        );


    }

    public class LoginResponce {
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("reportingUsers")
        @Expose
        private List<String> reportingUsers = null;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("is_full_access")
        @Expose
        private String is_full_access;


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<String> getReportingUsers() {
            return reportingUsers;
        }

        public void setReportingUsers(List<String> reportingUsers) {
            this.reportingUsers = reportingUsers;
        }

        public String getIs_full_access() {
            return is_full_access;
        }

        public void setIs_full_access(String is_full_access) {
            this.is_full_access = is_full_access;
        }

    }

}
