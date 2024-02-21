package com.tvisha.trooptime.activity.activity.model

import java.io.Serializable

/**
 * Created by tvisha on 10/2/17.
 */
class TimeAndAttendenceModel : Serializable {
    private var selfUser_Id: String? = null
    var selfDate: String? = null
    var selfCheck_in_time: String? = null
    var selfCheck_out_time: String? = null
    var selfWorkied_time: String? = null
    private var employeeUser_Id: String? = null
    private var employeeFull_Name: String? = null
    private var employeeEmail: String? = null
    private var reportingBossId: String? = null
    var user_Id: String? = null
    var emp_Id: String? = null
    var check_in_time: String? = null
    var check_out_time: String? = null
    var date: String? = null
    var workedTime: String? = null
    var breakTime: String? = null
    var breakStartTime: String? = null
    var breakEndTime: String? = null
    var breakStartCount: String? = null
    var breakEndCount: String? = null
    var userName: String? = null
    var userAvatar: String? = null
    var leaveApproved: String? = null
    private var leaveStatus: String? = null
    private var leaveType: String? = null
    private var holidayName: String? = null
    var remarks: String? = null
    var empStatus: String? = null
    var empRemarks: String? = null
    fun setSelfUser_Id(selfUser_Id: String?) {
        this.selfUser_Id = selfUser_Id
    }

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

    fun setLeaveStatus(leaveStatus: String?) {
        this.leaveStatus = leaveStatus
    }

    fun setLeaveType(leaveType: String?) {
        this.leaveType = leaveType
    }

    fun setHolidayName(holidayName: String?) {
        this.holidayName = holidayName
    }
}