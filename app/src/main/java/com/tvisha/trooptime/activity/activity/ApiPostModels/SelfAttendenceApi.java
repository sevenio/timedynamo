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

public class SelfAttendenceApi extends ApiClient {

    public static SelfAttendenceApi.ApiInterface getApiService() {
        return getClient().create(SelfAttendenceApi.ApiInterface.class);
    }

    public interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.Self_Attendence)
        Call<SelfAttendenceApi.SelfAttendenceApiResponce> getSelefAttendence(
                @Field("user_id") String username,
                @Field("token") String token,
                @Field("from_date") String from_date,
                @Field("to_date") String to_date,
                @Field("attendance_filter") String attendance_filter,
                @Field("working_hours") String filte_working_time);
    }

    public class SelfAttendenceApiResponce {
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
