package com.tvisha.trooptime.activity.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tvisha.trooptime.activity.activity.helper.Helper
import com.tvisha.trooptime.activity.activity.model.Notification
import com.tvisha.trooptime.R

class NotificationsNewAdapter(var context: Context,var notificationList:ArrayList<Notification>,var type : Int): RecyclerView.Adapter<NotificationsNewAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var tvTeamMemberName: TextView = view.findViewById(R.id.tvTeamMemberName)
        var tvNotificationType: TextView = view.findViewById(R.id.tvNotificationType)
        var tvNotificationData: TextView = view.findViewById(R.id.tvNotificationData)
        var tvNotificationTime: TextView = view.findViewById(R.id.tvNotificationTime)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var notification = notificationList[position]
        var notificationdata = Helper.stringToJsonObject(notification.notificationdata)
        holder.tvNotificationTime.text = notification.created_at
        holder.tvNotificationType.text  = Helper.notificationType(notification.notificationtype)
        holder.tvNotificationData.text = ""
        holder.tvTeamMemberName.visibility = if (type==0) View.GONE else View.VISIBLE
        if (notificationdata!=null) {
            if (notificationdata.has("member_name")) {
                holder.tvTeamMemberName.text = notificationdata.optString("member_name")
            }
            var message = notificationdata.optString("description")+"\n"+notificationdata.optString("no_of_days")+" ("+notificationdata.optString("leave_start_date")+" to "+notificationdata.optString("leave_end_date")+" )"
            holder.tvNotificationData.text = message
        }
    }


    override fun getItemCount(): Int {
        return notificationList.size
    }
}