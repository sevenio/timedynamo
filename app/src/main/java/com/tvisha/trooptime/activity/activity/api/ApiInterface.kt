package com.tvisha.trooptime.activity.activity.api

import com.tvisha.trooptime.activity.activity.apiPostModels.*
import com.tvisha.trooptime.activity.activity.helper.ServerUrls
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST(ServerUrls.Loginurl)
    suspend fun getLoginDetails(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("token") token: String?
    ): Response<String?>

    @FormUrlEncoded
    @POST(ServerUrls.All_Attendence)
    fun getAllAttendence(
        @Field("user_id") username: String?,
        @Field("token") token: String?,
        @Field("date") date: String?
    ): Call<AllAttendenceApiResponce?>?

    @FormUrlEncoded
    @POST(ServerUrls.All_Attendence)
    fun getFilterAllAttendence(
        @Field("user_id") userId: String?,
        @Field("date") select_date: String?,
        @Field("token") token: String?,
        @Field("users") emp_id: String?,
        @Field("attendance_filter") attendance_filter: Int,
        @Field("working_hours") filte_working_time: Int
    ): Call<FilterAllAttendenceApiResponce?>?

    @FormUrlEncoded
    @POST(ServerUrls.Team_Attendence)
    fun getFilterAttendence(
        @Field("user_id") userId: String?,
        @Field("date") select_date: String?,
        @Field("token") token: String?,
        @Field("users") emp_id: String?,
        @Field("attendance_filter") attendance_filter: String?,
        @Field("working_hours") filte_working_time: String?
    ): Call<FilterAttendenceApiResponce?>?

    @FormUrlEncoded
    @POST(ServerUrls.Forgeturl)
    fun getForgotDetails(
        @Field("emai") username: String?,
        @Field("token") token: String?
    ): Call<ForgotApiResponce?>?

    @FormUrlEncoded
    @POST(ServerUrls.Forgeturl)
    fun getLogoutDetails(
        @Field("user_id") username: String?,
        @Field("token") token: String?,
        @Field("fcm_token") fcm_token: String?
    ): Call<LogoutApiResponce?>?

    @FormUrlEncoded
    @POST(ServerUrls.Team_Attendence)
    fun getTeamfAttendence(
        @Field("user_id") username: String?,
        @Field("token") token: String?,
        @Field("date") date: String?
    ): Call<TeamAttendenceApiResponce?>?

    @FormUrlEncoded
    @POST("send-forgot-password")
    fun getOtp(@Field("mobile") email: String?): Call<ForgotPasswordResponce?>?

    @FormUrlEncoded
    @POST("resend-otp")
    fun resendOtp(
        @Field("mobile") email: String?,
        @Field("user_id") userId: String?
    ): Call<ForgotPasswordResponce?>?

    @FormUrlEncoded
    @POST("verify-otp")
    fun verifyOtp(
        @Field("otp") otp: String?,
        @Field("mobile") mobile: String?
    ): Call<VerifyOtpResponse?>?

    @FormUrlEncoded
    @POST("update-new-password")
    fun resetPassword(
        @Field("user_id") userId: String?,
        @Field("mobile") email: String?,
        @Field("new_password") newPassword: String?,
        @Field("confirm_password") confirmPassword: String?
    ): Call<VerifyOtpResponse?>?

    @FormUrlEncoded
    @POST("user-request-list")
    fun getUserRequestList(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("from_date") fromDate: String?,
        @Field("to_date") toDate: String?,
        @Field("request_type") requestType: String?,
        @Field("request_status") requestStatus: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): Call<UserRequestListResponse?>?

    @FormUrlEncoded
    @POST("team-request-list")
    fun getTeamRequestList(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("from_date") fromDate: String?,
        @Field("to_date") toDate: String?,
        @Field("request_type") requestType: String?,
        @Field("request_status") requestStatus: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): Call<UserRequestListResponse?>?

    @FormUrlEncoded
    @POST("all-request-list")
    fun getAllRequestList(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("from_date") fromDate: String?,
        @Field("to_date") toDate: String?,
        @Field("request_type") requestType: String?,
        @Field("request_status") requestStatus: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): Call<UserRequestListResponse?>?

    @FormUrlEncoded
    @POST("cc-request-list")
    fun getCcRequestList(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("from_date") fromDate: String?,
        @Field("to_date") toDate: String?,
        @Field("request_type") requestType: String?,
        @Field("request_status") requestStatus: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): Call<UserRequestListResponse?>?

    @FormUrlEncoded
    @POST("get-cc-user-details")
    fun getToAndCcDetails(
        @Field("token") token: String?,
        @Field("user_id") userId: String?
    ): Call<ToAndCcDetailsResponse?>?

    @FormUrlEncoded
    @POST("save-leave-request")
    fun SaveLeaveRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("to_employees") toEmployees: String?,
        @Field("cc_employees") ccEmployees: String?,
        @Field("type") type: String?,
        @Field("reason") reason: String?,
        @Field("start_date") startDate: String?,
        @Field("end_date") endDate: String?,
        @Field("data") data: String?,
        @Field("is_half_day") isHalfDay: String?
    ): Call<LeaveRequestResponse?>?

    @FormUrlEncoded
    @POST("save-swap-request")
    fun SaveSwapRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("to_employees") toEmployees: String?,
        @Field("cc_employees") ccEmployees: String?,
        @Field("type") type: String?, @Field("reason") reason: String?,
        @Field("start_date") startDate: String?,
        @Field("end_date") endDate: String?,
        @Field("data") data: String?,
        @Field("request_type") request_type: String?,
        @Field("swap_to") swap_to: String?
    ): Call<LeaveRequestResponse?>?

    @FormUrlEncoded
    @POST("save-permission-request")
    fun SavePermissionRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("to_employees") toEmployees: String?,
        @Field("cc_employees") ccEmployees: String?,
        @Field("type") type: String?,
        @Field("reason") reason: String?,
        @Field("date") date: String?,
        @Field("data") data: String?
    ): Call<LeaveRequestResponse?>?

    @FormUrlEncoded
    @POST("save-compoff-request")
    fun SaveCompOffRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("to_employees") toEmployees: String?,
        @Field("cc_employees") ccEmployees: String?,
        @Field("reason") reason: String?,
        @Field("day_worked_date") dayWorkedDate: String?,
        @Field("compoff_date") compOffDate: String?
    ): Call<LeaveRequestResponse?>?

    @FormUrlEncoded
    @POST(ServerUrls.Self_Attendence)
    fun getSelefAttendence(
        @Field("user_id") username: String?,
        @Field("token") token: String?,
        @Field("from_date") from_date: String?,
        @Field("to_date") to_date: String?,
        @Field("attendance_filter") attendance_filter: String?,
        @Field("working_hours") filte_working_time: String?
    ): Call<SelfAttendenceApiResponce?>?

    @FormUrlEncoded
    @POST("home-page")
    fun getHomeDetails(
        @Field("token") token: String?,
        @Field("user_id") userId: String?
    ): Call<HomePageResponse?>?

    @FormUrlEncoded
    @POST("save-request-comment")
    fun sendComment(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("request_id") requestId: String?,
        @Field("comment") comment: String?,
        @Field("comment_type") commentType: String?
    ): Call<SendCommentResponse?>?

    @FormUrlEncoded
    @POST("save-request-comment")
    fun sendExceptionAutoCheckoutComment(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("request_id") requestId: String?,
        @Field("comment") comment: String?,
        @Field("comment_type") commentType: String?,
        @Field("exception_type") exception_type: String?
    ): Call<SendCommentResponse?>?

    @FormUrlEncoded
    @POST("approve-leave-request")
    fun approveRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("request_id") requestId: String?,
        @Field("comment") comment: String?,
        @Field("comment_type") commentType: String?,
        @Field("request_type") requestType: String?,
        @Field("is_reporting_manager") is_reporting_manager: String?
    ): Call<CommonResponse?>?

    @FormUrlEncoded
    @POST("reject-leave-request")
    fun rejectRequest(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("request_id") requestId: String?,
        @Field("comment") comment: String?,
        @Field("comment_type") commentType: String?,
        @Field("request_type") requestType: String?,
        @Field("is_reporting_manager") is_reporting_manager: String?
    ): Call<CommonResponse?>?

    @FormUrlEncoded
    @POST("user-pinned-active")
    fun pin(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("pin_user_id") pinUserId: String?,
        @Field("pin_status") pinStatus: String?,
        @Field("pin_type") pinType: String?
    ): Call<PinUnpinResponse?>?

    @FormUrlEncoded
    @POST("user-pinned-in-active")
    fun unpin(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("un_pin_user_id") unPinUserId: String?,
        @Field("pin_status") pinStatus: String?,
        @Field("pin_type") pinType: String?
    ): Call<PinUnpinResponse?>?

    @FormUrlEncoded
    @POST("save-device-token")
    fun getupdate(
        @Field("user_id") userid: String?,
        @Field("token") token: String?,
        @Field("fcm_token") fcm_token: String?,
        @Field("device_type") s: String?
    ): Call<FcmUpdateResponce?>?

    @FormUrlEncoded
    @POST("get-attendance-report-on-user")
    fun getAttendanceReport(
        @Field("user_id") userid: String?,
        @Field("token") token: String?,
        @Field("from_date") start_date: String?,
        @Field("to_date") end_date: String?
    ): Call<AttendanceReportResponse?>?

    @FormUrlEncoded
    @POST("get-leave-report-on-user")
    fun getLeaveReport(
        @Field("user_id") userid: String?,
        @Field("token") token: String?,
        @Field("from_date") start_date: String?,
        @Field("to_date") end_date: String?
    ): Call<LeaveReportResponse?>?

    @FormUrlEncoded
    @POST("get-employee-summary-card")
    fun getEmployeeSummary(
        @Field("user_id") userid: String?,
        @Field("token") token: String?,
        @Field("from_date") from_date: String?,
        @Field("to_date") to_date: String?,
        @Field("type") type: String?
    ): Call<EmployeeSummaryResponse?>?

    @FormUrlEncoded
    @POST("get-employee-summary-card-permission")
    fun getEmployeePermissionSummary(
        @Field("user_id") userid: String?,
        @Field("token") token: String?,
        @Field("from_date") from_date: String?,
        @Field("to_date") to_date: String?,
        @Field("type") type: String?
    ): Call<EmployeePermissionSummaryResponse?>?

    @FormUrlEncoded
    @POST("get-image-upload-keys")
    fun getAwsConfig(
        @Field("token") token: String?
    ): Call<GetAwsConfigResponse?>?

    @FormUrlEncoded
    @POST("update-profile-pic")
    fun updateProfile(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("user_avatar") user_avatar: String?,
        @Field("mobile") mobile: String?
    ): Call<UpdateProfileResponse?>?

    @FormUrlEncoded
    @POST("change-password")
    fun changePassword(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("mobile") mobile: String?,
        @Field("old_password") old_password: String?,
        @Field("new_password") new_password: String?
    ): Call<ChangePasswordResponse?>?

    //get-all-attendance-employee-list
    @FormUrlEncoded
    @POST("get-swap-request-on-user-id")
    fun getWeekOffDates(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?
    ): Call<WeekOffsResponse?>?

    @FormUrlEncoded
    @POST("get-all-attendance-employee-list")
    fun getAllEmployess(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?
    ): Call<CcEmpResponse?>?

    @FormUrlEncoded
    @POST("get-team-attendance-employee-list")
    fun getTeamEmployess(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?
    ): Call<CcEmpResponse?>?

    @FormUrlEncoded
    @POST("get-cc-request-employee-list")
    fun getCcEmployess(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?
    ): Call<CcEmpResponse?>?

    @FormUrlEncoded
    @POST("get-user-request-comments")
    fun getRequestComments(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("request_id") request_id: String?
    ): Call<RequestCommentsResponse?>?

    @FormUrlEncoded
    @POST("get-user-exception-comments")
    fun getExceptionComments(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("exception_id") request_id: String?
    ): Call<RequestCommentsResponse?>?

    @FormUrlEncoded
    @POST("save-device-token")
    fun saveFcmToken(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("fcm_token") fcm_token: String?,
        @Field("device_type") device_type: String?,
        @Field("device_id") device_id: String?
    ): Call<CommonResponse?>?

    @FormUrlEncoded
    @POST("get-auto-check-out-details")
    fun getAutoCheckOutDetails(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("date") date: String?,
        @Field("attendance_id") attendance_id: String?
    ): Call<AutoCheckDetailsResponse?>?

    @FormUrlEncoded
    @POST("get-exception-on-user-id")
    fun getExceptionDetails(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("date") date: String?,
        @Field("attendance_id") attendance_id: String?,
        @Field("exception_type") exception_type: String?
    ): Call<ExceptionDetailsResponse?>?

    @FormUrlEncoded
    @POST("approve-auto-check-out-exception")
    fun approveAutoCheckOutException(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("type") type: String?,
        @Field("comment") comment: String?,
        @Field("attendance_id") attendance_id: String?,
        @Field("check_out") check_out: String?,
        @Field("emp_id") emp_id: String?,
        @Field("date") date: String?,
        @Field("exception_id") exceptionId: String?
    ): Call<CommonResponse?>?

    @FormUrlEncoded
    @POST("update-exception")
    fun approveException(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("exception_id") exception_id: String?,
        @Field("comment") comment: String?,
        @Field("attendance_id") attendance_id: String?,
        @Field("check_out") check_out: String?,
        @Field("emp_id") emp_id: String?,
        @Field("date") date: String?,
        @Field("check_in") check_in: String?,
        @Field("status") status: String?,
        @Field("exception_type") exception_type: String?
    ): Call<CommonResponse?>?

    @FormUrlEncoded
    @POST("get-exception-on-status")
    fun getExceptionStatus(
        @Field("token") token: String?,
        @Field("user_id") user_id: String?,
        @Field("attendance_id") attendance_id: String?,
        @Field("exception_type") exception_type: String?,
        @Field("status") status: String?,
        @Field("date") date: String?
    ): Call<ExceptionStatusResponse?>?

    @FormUrlEncoded
    @POST("check-version-update")
    fun checkUpdateAvailable(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("type") device_type: String?,
        @Field("version") version: String?
    ): Call<CheckUpdateResponse?>?

    //todo remove
    @FormUrlEncoded
    @POST("user-notifications-list")
    fun getNotifications(
        @Field("token") token: String?,
        @Field("user_id") userId: String?,
        @Field("from_date") fromDate: String?,
        @Field("to_date") toDate: String?,
        @Field("request_type") requestType: String?,
        @Field("request_status") requestStatus: String?,
        @Field("limit") limit: Int,
        @Field("offset") offset: Int
    ): Call<UserRequestListResponse?>?
}