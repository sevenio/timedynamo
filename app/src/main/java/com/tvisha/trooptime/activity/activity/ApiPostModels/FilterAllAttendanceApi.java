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
 * Created by tvisha on 23/6/18.
 */

public class FilterAllAttendanceApi extends ApiClient {

    public static FilterAllAttendanceApi.ApiInterface getApiService() {
        return getClient().create(FilterAllAttendanceApi.ApiInterface.class);
    }

    public static interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.All_Attendence)
        Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> getFilterAllAttendence(
                @Field("user_id") String userId,
                @Field("date") String select_date,
                @Field("token") String token,
                @Field("users") String emp_id,
                @Field("attendance_filter") int attendance_filter,
                @Field("working_hours") int filte_working_time);
    }

    public class FilterAllAttendenceApiResponce {
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
