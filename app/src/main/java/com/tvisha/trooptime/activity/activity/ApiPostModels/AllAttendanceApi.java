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

/**
 * Created by tvisha on 22/6/18.
 */

public class AllAttendanceApi extends ApiClient {

    public static AllAttendanceApi.ApiInterface getApiService() {
        return getClient().create(AllAttendanceApi.ApiInterface.class);
    }

    public static interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.All_Attendence)
        Call<AllAttendanceApi.AllAttendenceApiResponce> getAllAttendence(
                @Field("user_id") String username,
                @Field("token") String token,
                @Field("date") String date
        );
    }

    public class AllAttendenceApiResponce {
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("attendance")
        @Expose
        private List<Attendance> attendance = null;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public List<Attendance> getAttendance() {
            return attendance;
        }

        public void setAttendance(List<Attendance> attendance) {
            this.attendance = attendance;
        }
    }
}
