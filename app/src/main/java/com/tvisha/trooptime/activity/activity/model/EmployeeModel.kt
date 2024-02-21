package com.tvisha.trooptime.activity.activity.model

/**
 * Created by tvisha on 20/2/17.
 */
class EmployeeModel {
    var employee_Id: String? = null
    var employee_name: String? = null
    var checked = false
    fun isChecked(): Boolean {
        return checked
    }
}