package com.tvisha.trooptime.activity.activity.model

/**
 * Created by tvisha on 10/2/17.
 */
class SelfAttenedModel {
    private var selfUser_Id: String? = null
    private var selfDate: String? = null
    private var selfWorkied_time: String? = null
    private var selfCheck_out_time: String? = null
    private var selfCheck_in_time: String? = null
    fun setSelfUser_Id(selfUser_Id: String?) {
        this.selfUser_Id = selfUser_Id
    }

    fun setSelfDate(selfDate: String?) {
        this.selfDate = selfDate
    }

    fun setSelfWorkied_time(selfWorkied_time: String?) {
        this.selfWorkied_time = selfWorkied_time
    }

    fun setSelfCheck_out_time(selfCheck_out_time: String?) {
        this.selfCheck_out_time = selfCheck_out_time
    }

    fun setSelfCheck_in_time(selfCheck_in_time: String?) {
        this.selfCheck_in_time = selfCheck_in_time
    }
}