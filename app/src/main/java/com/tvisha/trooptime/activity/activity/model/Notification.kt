package com.tvisha.trooptime.activity.activity.model

class Notification {
    var iD: Long = 0
    var notificationdata: String? = null
    var notificationtype = 0
    fun getnotificationdata(): String? {
        return notificationdata
    }

    fun setnotificationdata(notificationdata: String?) {
        this.notificationdata = notificationdata
    }

    fun getnotificationtype(): Int {
        return notificationtype
    }

    fun setnotificationtype(notificationtype: Int) {
        this.notificationtype = notificationtype
    }

    var created_at: String? = null
    var updated_at: String? = null
}