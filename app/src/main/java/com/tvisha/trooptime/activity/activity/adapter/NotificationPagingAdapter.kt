package com.tvisha.trooptime.activity.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tvisha.trooptime.activity.activity.model.Notification
import com.tvisha.trooptime.activity.activity.viewHolders.NotificationViewHolder
import com.tvisha.trooptime.R

class NotificationPagingAdapter(var context:Context,var type:Int):PagingDataAdapter<Notification,RecyclerView.ViewHolder>(diffCallback){
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return if (oldItem is Notification && newItem is Notification) {
                    oldItem.iD == newItem.iD
                } else {
                    oldItem == newItem
                }
            }

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.equals(newItem)

            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position)!=null){
            holder as NotificationViewHolder
            holder.bindData(getItem(position),type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.notification_item,parent,false),context)
    }
}