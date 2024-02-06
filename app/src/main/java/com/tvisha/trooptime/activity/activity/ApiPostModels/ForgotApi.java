package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tvisha.trooptime.activity.activity.Api_Helper.ApiClient;
import com.tvisha.trooptime.activity.activity.Helper.ServerUrls;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tvisha on 21/6/18.
 */

public class ForgotApi extends ApiClient {
    public static ForgotApi.ApiInterface getApiService() {
        return getClient().create(ForgotApi.ApiInterface.class);
    }

    public static interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.Forgeturl)
        Call<ForgotApi.ForgotApiResponce> getForgotDetails(
                @Field("emai") String username,
                @Field("token") String token
        );
    }

    public class ForgotApiResponce {
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("message")
        @Expose
        private String message;

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

    }
}
