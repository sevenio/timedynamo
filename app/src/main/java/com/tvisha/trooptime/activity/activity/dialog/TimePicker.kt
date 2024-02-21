package com.tvisha.trooptime.activity.activity.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tvisha.trooptime.activity.activity.listner.TimeChangeListner
import java.util.*

/**
 * Created by koti on 10/2/17.
 */
class TimePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var c = Calendar.getInstance()
    var startYear = c[Calendar.YEAR]
    var startMonth = c[Calendar.MONTH]
    var startDay = c[Calendar.DAY_OF_MONTH]
    var timeHolder: TextView? = null
    var slelectedDate: String? = null
    var TYPE //0=> for time and date 1=>for date only
            = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        return if (TYPE == TYPE_TIME) {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            TimePickerDialog(context,
                { timePicker, selectedHour, selectedMinute ->
                    if (TYPE == TYPE_TIME) {
                        timeHolder?.setText(selectedHour.toString() + ":" + if (selectedMinute < 10) "0$selectedMinute" else selectedMinute)
                    } else {
                        timeHolder?.setText(slelectedDate + " " + selectedHour + ":" + if (selectedMinute < 10) "0$selectedMinute" else selectedMinute)
                    }
                    if (context is TimeChangeListner) {
                        (context as TimeChangeListner?)?.onTimeChanged()
                    }
                }, hour, minute, true)
        } else {
            //if u set min and max then it show remain days only
            val dialog = DatePickerDialog(context!!, this, startYear, startMonth, startDay)
            if (CALENDAR_TYPE == CALENDAR_TYPE_PAST) {
                val cDate = c.time
                dialog.datePicker.maxDate = cDate.time
            } else {
                c.add(Calendar.DATE, +1)
                val fDate = c.time
                dialog.datePicker.minDate = fDate.time
            }
            dialog
        }
        //from current date
        //dialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        startYear = year
        startMonth = monthOfYear
        startDay = dayOfMonth
        //  String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;  //for display
        //slelectedDate = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;  //for format
        slelectedDate =
            year.toString() + "-" + (if (monthOfYear + 1 < 10) "0" + (monthOfYear + 1) else monthOfYear + 1) + "-" + if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth //for format
        timeHolder?.setTag(slelectedDate)
        timeHolder?.setText(slelectedDate)
        if (TYPE == TYPE_DATE && context is TimeChangeListner) {
            (context as TimeChangeListner?)?.onTimeChanged()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (TYPE == TYPE_DATE_TIME) {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context,
                { timePicker, selectedHour, selectedMinute ->
                    if (TYPE == TYPE_TIME) {
                        timeHolder?.setText(selectedHour.toString() + ":" + if (selectedMinute < 10) "0$selectedMinute" else selectedMinute)
                    } else {
                        timeHolder?.setText(slelectedDate + " " + selectedHour + ":" + if (selectedMinute < 10) "0$selectedMinute" else selectedMinute)
                    }
                }, hour, minute, true) //Yes 24 hour time
            mTimePicker.setTitle("Event Time")
            mTimePicker.show()
        } else if (TYPE != TYPE_TIME) {
            if (slelectedDate != null) {
                timeHolder?.setText(slelectedDate)
            }
        }
        super.onDismiss(dialog)
    }

    companion object {
        const val TYPE_DATE = 1
        const val TYPE_TIME = 2
        const val TYPE_DATE_TIME = 0
        var timePicker = TimePicker()
        var CALENDAR_TYPE = -1
        var CALENDAR_TYPE_PAST = 0
        @JvmField
        var CALENDAR_TYPE_FEATURE = 1
        fun getInstance(REQ_TYPE: Int, view: TextView?): TimePicker {
            //  if (timePicker == null) {
            timePicker = TimePicker()
            //  }
            timePicker.TYPE = REQ_TYPE
            timePicker.timeHolder = view
            CALENDAR_TYPE = -1
            timePicker.slelectedDate = null
            return timePicker
        }

        fun getInstance(REQ_TYPE: Int, CAL_TYPE: Int, view: TextView?): TimePicker {
            //if (timePicker == null) {
            timePicker = TimePicker()
            //}
            timePicker.TYPE = REQ_TYPE
            CALENDAR_TYPE = CAL_TYPE
            timePicker.timeHolder = view
            timePicker.slelectedDate = null
            return timePicker
        }
    }
}