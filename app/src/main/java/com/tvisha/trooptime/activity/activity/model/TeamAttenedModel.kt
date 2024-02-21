package com.tvisha.trooptime.activity.activity.model

/**
 * Created by tvisha on 10/2/17.
 */
class TeamAttenedModel {
    private var employeeUser_Id: String? = null
    private var employeeFull_Name: String? = null
    private var employeeEmail: String? = null
    private var reportingBossId: String? = null
    private var user_Id: String? = null
    private var emp_Id: String? = null
    private var check_in_time: String? = null
    private var check_out_time: String? = null
    private var date: String? = null
    private var workedTime: String? = null
    private var breakTime: String? = null
    private var breakStartTime: String? = null
    private var breakEndTime: String? = null
    private var breakStartCount: String? = null
    private var breakEndCount: String? = null
    private var userName: String? = null
    private var userAvatar: String? = null
    fun setEmployeeUser_Id(employeeUser_Id: String?) {
        this.employeeUser_Id = employeeUser_Id
    }

    fun setEmployeeFull_Name(employeeFull_Name: String?) {
        this.employeeFull_Name = employeeFull_Name
    }

    fun setEmployeeEmail(employeeEmail: String?) {
        this.employeeEmail = employeeEmail
    }

    fun setReportingBossId(reportingBossId: String?) {
        this.reportingBossId = reportingBossId
    }

    fun setUser_Id(user_Id: String?) {
        this.user_Id = user_Id
    }

    fun setEmp_Id(emp_Id: String?) {
        this.emp_Id = emp_Id
    }

    fun setCheck_in_time(check_in_time: String?) {
        this.check_in_time = check_in_time
    }

    fun setCheck_out_time(check_out_time: String?) {
        this.check_out_time = check_out_time
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun setWorkedTime(workedTime: String?) {
        this.workedTime = workedTime
    }

    fun setBreakTime(breakTime: String?) {
        this.breakTime = breakTime
    }

    fun setBreakStartTime(breakStartTime: String?) {
        this.breakStartTime = breakStartTime
    }

    fun setBreakEndTime(breakEndTime: String?) {
        this.breakEndTime = breakEndTime
    }

    fun setBreakStartCount(breakStartCount: String?) {
        this.breakStartCount = breakStartCount
    }

    fun setBreakEndCount(breakEndCount: String?) {
        this.breakEndCount = breakEndCount
    }

    fun setUserName(userName: String?) {
        this.userName = userName
    }

    fun setUserAvatar(userAvatar: String?) {
        this.userAvatar = userAvatar
    }
}