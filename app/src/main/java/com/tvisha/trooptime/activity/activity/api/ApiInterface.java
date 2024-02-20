package com.tvisha.trooptime.activity.activity.api;


import com.tvisha.trooptime.activity.activity.apiPostModels.AllAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.AttendanceReportResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.AutoCheckDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.CcEmpResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ChangePasswordResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.CheckUpdateResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeePermissionSummaryResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeeSummaryResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionStatusResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.FcmUpdateResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.FilterAllAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.FilterAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.ForgotApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.ForgotPasswordResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.GetAwsConfigResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.HomePageResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveReportResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveRequestResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.LogoutApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.PinUnpinResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.RequestCommentsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.SendCommentResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.TeamAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.ToAndCcDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpdateProfileResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.UserRequestListResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.VerifyOtpResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.WeekOffsResponse;
import com.tvisha.trooptime.activity.activity.helper.ServerUrls;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    /*

        @FormUrlEncoded
        @POST("login")
        Call<ApiResponse> getOtp(@Field("username") String username, @Field("token") String token, @Field("device_id") String deviceId);
    */
/*
    @Headers("user-key: 9900a9720d31dfd5fdb4352700c")
    @GET("api/v2.1/search")
    Call<ApiResponse> getRestaurantsBySearch(@Query("entity_id") String entity_id, @Query("entity_type") String entity_type, @Query("q") String query);
*/

    @FormUrlEncoded
    @POST(ServerUrls.Loginurl)
    Call<String> getLoginDetails(
            @Field("username") String username,
            @Field("password") String password,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST(ServerUrls.All_Attendence)
    Call<AllAttendenceApiResponce> getAllAttendence(
            @Field("user_id") String username,
            @Field("token") String token,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST(ServerUrls.All_Attendence)
    Call<FilterAllAttendenceApiResponce> getFilterAllAttendence(
            @Field("user_id") String userId,
            @Field("date") String select_date,
            @Field("token") String token,
            @Field("users") String emp_id,
            @Field("attendance_filter") int attendance_filter,
            @Field("working_hours") int filte_working_time);

    @FormUrlEncoded
    @POST(ServerUrls.Team_Attendence)
    Call<FilterAttendenceApiResponce> getFilterAttendence(
            @Field("user_id") String userId,
            @Field("date") String select_date,
            @Field("token") String token,
            @Field("users") String emp_id,
            @Field("attendance_filter") String attendance_filter,
            @Field("working_hours") String filte_working_time);

    @FormUrlEncoded
    @POST(ServerUrls.Forgeturl)
    Call<ForgotApiResponce> getForgotDetails(
            @Field("emai") String username,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST(ServerUrls.Forgeturl)
    Call<LogoutApiResponce> getLogoutDetails(
            @Field("user_id") String username,
            @Field("token") String token,
            @Field("fcm_token") String fcm_token
    );

    @FormUrlEncoded
    @POST(ServerUrls.Team_Attendence)
    Call<TeamAttendenceApiResponce> getTeamfAttendence(
            @Field("user_id") String username,
            @Field("token") String token,
            @Field("date") String date
    );


    @FormUrlEncoded
    @POST("send-forgot-password")
    Call<ForgotPasswordResponce> getOtp(@Field("mobile") String email);

    @FormUrlEncoded
    @POST("resend-otp")
    Call<ForgotPasswordResponce> resendOtp(@Field("mobile") String email, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("verify-otp")
    Call<VerifyOtpResponse> verifyOtp(@Field("otp") String otp, @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("update-new-password")
    Call<VerifyOtpResponse> resetPassword(@Field("user_id") String userId, @Field("mobile") String email, @Field("new_password") String newPassword, @Field("confirm_password") String confirmPassword);

    @FormUrlEncoded
    @POST("user-request-list")
    Call<UserRequestListResponse> getUserRequestList(@Field("token") String token,
                                                     @Field("user_id") String userId,
                                                     @Field("from_date") String fromDate,
                                                     @Field("to_date") String toDate,
                                                     @Field("request_type") String requestType,
                                                     @Field("request_status") String requestStatus,
                                                     @Field("limit") int limit,
                                                     @Field("offset") int offset);

    @FormUrlEncoded
    @POST("team-request-list")
    Call<UserRequestListResponse> getTeamRequestList(@Field("token") String token,
                                                     @Field("user_id") String userId,
                                                     @Field("from_date") String fromDate,
                                                     @Field("to_date") String toDate,
                                                     @Field("request_type") String requestType,
                                                     @Field("request_status") String requestStatus,
                                                     @Field("limit") int limit,
                                                     @Field("offset") int offset);

    @FormUrlEncoded
    @POST("all-request-list")
    Call<UserRequestListResponse> getAllRequestList(@Field("token") String token,
                                                    @Field("user_id") String userId,
                                                    @Field("from_date") String fromDate,
                                                    @Field("to_date") String toDate,
                                                    @Field("request_type") String requestType,
                                                    @Field("request_status") String requestStatus,
                                                    @Field("limit") int limit,
                                                    @Field("offset") int offset);

    @FormUrlEncoded
    @POST("cc-request-list")
    Call<UserRequestListResponse> getCcRequestList(@Field("token") String token,
                                                   @Field("user_id") String userId,
                                                   @Field("from_date") String fromDate,
                                                   @Field("to_date") String toDate,
                                                   @Field("request_type") String requestType,
                                                   @Field("request_status") String requestStatus,
                                                   @Field("limit") int limit,
                                                   @Field("offset") int offset);

    @FormUrlEncoded
    @POST("get-cc-user-details")
    Call<ToAndCcDetailsResponse> getToAndCcDetails(@Field("token") String token, @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("save-leave-request")
    Call<LeaveRequestResponse> SaveLeaveRequest(@Field("token") String token, @Field("user_id") String userId, @Field("to_employees") String toEmployees,
                                                @Field("cc_employees") String ccEmployees, @Field("type") String type, @Field("reason") String reason, @Field("start_date") String startDate,
                                                @Field("end_date") String endDate, @Field("data") String data, @Field("is_half_day") String isHalfDay);


    @FormUrlEncoded
    @POST("save-swap-request")
    Call<LeaveRequestResponse> SaveSwapRequest(@Field("token") String token,
                                               @Field("user_id") String userId,
                                               @Field("to_employees") String toEmployees,
                                               @Field("cc_employees") String ccEmployees,
                                               @Field("type") String type, @Field("reason") String reason,
                                               @Field("start_date") String startDate,
                                               @Field("end_date") String endDate,
                                               @Field("data") String data,
                                               @Field("request_type") String request_type,
                                               @Field("swap_to") String swap_to);


    @FormUrlEncoded
    @POST("save-permission-request")
    Call<LeaveRequestResponse> SavePermissionRequest(@Field("token") String token, @Field("user_id") String userId, @Field("to_employees") String toEmployees,
                                                     @Field("cc_employees") String ccEmployees, @Field("type") String type, @Field("reason") String reason, @Field("date") String date, @Field("data") String data
    );

    @FormUrlEncoded
    @POST("save-compoff-request")
    Call<LeaveRequestResponse> SaveCompOffRequest(@Field("token") String token, @Field("user_id") String userId, @Field("to_employees") String toEmployees,
                                                  @Field("cc_employees") String ccEmployees, @Field("reason") String reason, @Field("day_worked_date") String dayWorkedDate,
                                                  @Field("compoff_date") String compOffDate);


    @FormUrlEncoded
    @POST(ServerUrls.Self_Attendence)
    Call<SelfAttendenceApiResponce> getSelefAttendence(
            @Field("user_id") String username,
            @Field("token") String token,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("attendance_filter") String attendance_filter,
            @Field("working_hours") String filte_working_time);

    @FormUrlEncoded
    @POST("home-page")
    Call<HomePageResponse> getHomeDetails(@Field("token") String token, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("save-request-comment")
    Call<SendCommentResponse> sendComment(@Field("token") String token, @Field("user_id") String userId, @Field("request_id") String requestId, @Field("comment") String comment, @Field("comment_type") String commentType);

    @FormUrlEncoded
    @POST("save-request-comment")
    Call<SendCommentResponse> sendExceptionAutoCheckoutComment(@Field("token") String token,
                                                               @Field("user_id") String userId,
                                                               @Field("request_id") String requestId,
                                                               @Field("comment") String comment,
                                                               @Field("comment_type") String commentType,
                                                               @Field("exception_type") String exception_type
    );

    @FormUrlEncoded
    @POST("approve-leave-request")
    Call<CommonResponse> approveRequest(@Field("token") String token,
                                        @Field("user_id") String userId,
                                        @Field("request_id") String requestId,
                                        @Field("comment") String comment,
                                        @Field("comment_type") String commentType,
                                        @Field("request_type") String requestType,
                                        @Field("is_reporting_manager") String is_reporting_manager

    );

    @FormUrlEncoded
    @POST("reject-leave-request")
    Call<CommonResponse> rejectRequest(@Field("token") String token,
                                       @Field("user_id") String userId,
                                       @Field("request_id") String requestId,
                                       @Field("comment") String comment,
                                       @Field("comment_type") String commentType,
                                       @Field("request_type") String requestType,
                                       @Field("is_reporting_manager") String is_reporting_manager

    );


    @FormUrlEncoded
    @POST("user-pinned-active")
    Call<PinUnpinResponse> pin(@Field("token") String token, @Field("user_id") String userId, @Field("pin_user_id") String pinUserId, @Field("pin_status") String pinStatus, @Field("pin_type") String pinType);

    @FormUrlEncoded
    @POST("user-pinned-in-active")
    Call<PinUnpinResponse> unpin(@Field("token") String token, @Field("user_id") String userId, @Field("un_pin_user_id") String unPinUserId, @Field("pin_status") String pinStatus, @Field("pin_type") String pinType);

    @FormUrlEncoded
    @POST("save-device-token")
    Call<FcmUpdateResponce> getupdate(
            @Field("user_id") String userid,
            @Field("token") String token,
            @Field("fcm_token") String fcm_token,
            @Field("device_type") String s);

    @FormUrlEncoded
    @POST("get-attendance-report-on-user")
    Call<AttendanceReportResponse> getAttendanceReport(
            @Field("user_id") String userid,
            @Field("token") String token,
            @Field("from_date") String start_date,
            @Field("to_date") String end_date);

    @FormUrlEncoded
    @POST("get-leave-report-on-user")
    Call<LeaveReportResponse> getLeaveReport(
            @Field("user_id") String userid,
            @Field("token") String token,
            @Field("from_date") String start_date,
            @Field("to_date") String end_date);

    @FormUrlEncoded
    @POST("get-employee-summary-card")
    Call<EmployeeSummaryResponse> getEmployeeSummary(
            @Field("user_id") String userid,
            @Field("token") String token,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("get-employee-summary-card-permission")
    Call<EmployeePermissionSummaryResponse> getEmployeePermissionSummary(
            @Field("user_id") String userid,
            @Field("token") String token,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("type") String type);


    @FormUrlEncoded
    @POST("get-image-upload-keys")
    Call<GetAwsConfigResponse> getAwsConfig(
            @Field("token") String token);


    @FormUrlEncoded
    @POST("update-profile-pic")
    Call<UpdateProfileResponse> updateProfile(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("user_avatar") String user_avatar,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("change-password")
    Call<ChangePasswordResponse> changePassword(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("mobile") String mobile,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );

    //get-all-attendance-employee-list

    @FormUrlEncoded
    @POST("get-swap-request-on-user-id")
    Call<WeekOffsResponse> getWeekOffDates(
            @Field("token") String token,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("get-all-attendance-employee-list")
    Call<CcEmpResponse> getAllEmployess(
            @Field("token") String token,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("get-team-attendance-employee-list")
    Call<CcEmpResponse> getTeamEmployess(
            @Field("token") String token,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("get-cc-request-employee-list")
    Call<CcEmpResponse> getCcEmployess(
            @Field("token") String token,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("get-user-request-comments")
    Call<RequestCommentsResponse> getRequestComments(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id

    );

    @FormUrlEncoded
    @POST("get-user-exception-comments")
    Call<RequestCommentsResponse> getExceptionComments(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("exception_id") String request_id

    );

    @FormUrlEncoded
    @POST("save-device-token")
    Call<CommonResponse> saveFcmToken(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("fcm_token") String fcm_token,
            @Field("device_type") String device_type,
            @Field("device_id") String device_id

    );

    @FormUrlEncoded
    @POST("get-auto-check-out-details")
    Call<AutoCheckDetailsResponse> getAutoCheckOutDetails(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("date") String date,
            @Field("attendance_id") String attendance_id

    );

    @FormUrlEncoded
    @POST("get-exception-on-user-id")
    Call<ExceptionDetailsResponse> getExceptionDetails(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("date") String date,
            @Field("attendance_id") String attendance_id,
            @Field("exception_type") String exception_type

    );

    @FormUrlEncoded
    @POST("approve-auto-check-out-exception")
    Call<CommonResponse> approveAutoCheckOutException(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("type") String type,
            @Field("comment") String comment,
            @Field("attendance_id") String attendance_id,
            @Field("check_out") String check_out,
            @Field("emp_id") String emp_id,
            @Field("date") String date,
            @Field("exception_id") String exceptionId

    );

    @FormUrlEncoded
    @POST("update-exception")
    Call<CommonResponse> approveException(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("exception_id") String exception_id,
            @Field("comment") String comment,
            @Field("attendance_id") String attendance_id,
            @Field("check_out") String check_out,
            @Field("emp_id") String emp_id,
            @Field("date") String date,
            @Field("check_in") String check_in,
            @Field("status") String status,
            @Field("exception_type") String exception_type


    );

    @FormUrlEncoded
    @POST("get-exception-on-status")
    Call<ExceptionStatusResponse> getExceptionStatus(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("attendance_id") String attendance_id,
            @Field("exception_type") String exception_type,
            @Field("status") String status,
            @Field("date") String date


    );

    @FormUrlEncoded
    @POST("check-version-update")
    Call<CheckUpdateResponse> checkUpdateAvailable(@Field("token") String token,
                                                   @Field("user_id") String userId,
                                                   @Field("type") String device_type,
                                                   @Field("version") String version
    );

    //todo remove
    @FormUrlEncoded
    @POST("user-notifications-list")
    Call<UserRequestListResponse> getNotifications(@Field("token") String token,
                                                     @Field("user_id") String userId,
                                                     @Field("from_date") String fromDate,
                                                     @Field("to_date") String toDate,
                                                     @Field("request_type") String requestType,
                                                     @Field("request_status") String requestStatus,
                                                     @Field("limit") int limit,
                                                     @Field("offset") int offset);


}
