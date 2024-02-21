package com.tvisha.trooptime.activity.activity.model

/**
 * Created by tvisha on 9/2/17.
 */
class TroopTimeModel {
    private var empUserId: String? = null
    var empId: String? = null
    var userAvatar: String? = null
    var userName: String? = null
    var checkIn: String? = null
    private var date: String? = null
    var checkOut: String? = null
    private var startBreakTime: String? = null
    private var endBreakTime: String? = null
    private var breakStartCount: String? = null
    private var breakEndCount: String? = null
    var workTime: String? = null
    private var selfUser_Id: String? = null
    private var selfDate: String? = null
    private var selfCheck_in_time: String? = null
    private var selfCheck_out_time: String? = null
    private var selfWorkied_time: String? = null
    fun setEmpUserId(empUserId: String?) {
        this.empUserId = empUserId
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun setStartBreakTime(startBreakTime: String?) {
        this.startBreakTime = startBreakTime
    }

    fun setEndBreakTime(endBreakTime: String?) {
        this.endBreakTime = endBreakTime
    }

    fun setBreakStartCount(breakStartCount: String?) {
        this.breakStartCount = breakStartCount
    }

    fun setBreakEndCount(breakEndCount: String?) {
        this.breakEndCount = breakEndCount
    }

    fun setSelfUser_Id(selfUser_Id: String?) {
        this.selfUser_Id = selfUser_Id
    }

    fun setSelfDate(selfDate: String?) {
        this.selfDate = selfDate
    }

    fun setSelfCheck_in_time(selfCheck_in_time: String?) {
        this.selfCheck_in_time = selfCheck_in_time
    }

    fun setSelfCheck_out_time(selfCheck_out_time: String?) {
        this.selfCheck_out_time = selfCheck_out_time
    }

    fun setSelfWorkied_time(selfWorkied_time: String?) {
        this.selfWorkied_time = selfWorkied_time
    }
}