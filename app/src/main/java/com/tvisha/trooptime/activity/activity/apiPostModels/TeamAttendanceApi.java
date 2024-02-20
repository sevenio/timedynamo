package com.tvisha.trooptime.activity.activity.apiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tvisha.trooptime.activity.activity.api_Helper.ApiClient;
import com.tvisha.trooptime.activity.activity.helper.ServerUrls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tvisha on 22/6/18.
 */

public class TeamAttendanceApi extends ApiClient {

    public static TeamAttendanceApi.ApiInterface getApiService() {
        return getClient().create(TeamAttendanceApi.ApiInterface.class);
    }

    public static interface ApiInterface {

        @FormUrlEncoded
        @POST(ServerUrls.Team_Attendence)
        Call<TeamAttendanceApi.TeamAttendenceApiResponce> getTeamfAttendence(
                @Field("user_id") String username,
                @Field("token") String token,
                @Field("date") String date
        );
    }

    public class TeamAttendenceApiResponce {
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
