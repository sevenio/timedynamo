package com.tvisha.trooptime.activity.activity.viewHolders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tvisha.trooptime.activity.activity.helper.Helper
import com.tvisha.trooptime.activity.activity.model.Notification
import com.tvisha.trooptime.databinding.NotificationItemBinding


class NotificationViewHolder(var binding:NotificationItemBinding,var context: Context) :RecyclerView.ViewHolder(binding.root) {
    fun bindData(notification: Notification?,type:Int) {
        var notificationdata = Helper.stringToJsonObject(notification!!.notificationdata)
        binding.tvNotificationTime.text = notification.created_at
        binding.tvNotificationType.text  = Helper.notificationType(notification.notificationtype)
        binding.tvNotificationData.text = ""
        binding.tvTeamMemberName.visibility = if (type==0) View.GONE else View.VISIBLE
        if (notificationdata!=null) {
            if (notificationdata.has("member_name")) {
                binding.tvTeamMemberName.text = notificationdata.optString("member_name")
            }
            var message = notificationdata.optString("description")+"\n"+notificationdata.optString("no_of_days")+" ("+notificationdata.optString("leave_start_date")+" to "+notificationdata.optString("leave_end_date")+" )"
            binding.tvNotificationData.text = message
        }
    }
}