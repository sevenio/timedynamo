package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.databinding.ItemNotificationFooterBinding
import com.tvisha.trooptime.databinding.ItemTeamNotificationBinding
import com.tvisha.trooptime.databinding.NotificationListLayoutBinding
import java.util.*

class TeamNotificationFrgment : Fragment() {
    private lateinit var binding: NotificationListLayoutBinding
    var linearLayoutManager: LinearLayoutManager? = null
    var notificationAdapter: TeamNotificationRecyclerViewAdapter? = null

    val viewModel: NotificationViewmodel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("sett", "onCreate TeamNotificationFrgment")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NotificationListLayoutBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }


    private fun allNotifications() {
        notificationAdapter?.clearData()
        linearLayoutManager?.scrollToPosition(0)

        viewModel.teamOffset = 0
        viewModel.teamIsLastPage = false
        viewModel.teamIsLoading.postValue(false)
        viewModel.teamIsLoadingBool = false
        viewModel.getTeamNotifications(false)
    }

    fun setup() {
        viewModel.notificationCalendars.observe(viewLifecycleOwner) {
            allNotifications()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notificationCalendars.observe(viewLifecycleOwner){
            Log.d("ganga", "TeamNotificationFrgment observe")

        }
        Log.d("ganga", "TeamNotificationFrgment onViewCreated")

    }
}

class TeamNotificationRecyclerViewAdapter(
    val callback: (String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    var list = ArrayList<CalendarDate>()


    class TeamNotificationViewHolder(val binding: ItemTeamNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setData(data: ArrayList<CalendarDate>) {

        for (post in data) {
            addPost(post)
        }
    }

    fun addFooter() {
        isLoadingAdded = true
        addPost(CalendarDate(Calendar.getInstance(), 1, ""))
    }

    fun getItem(position: Int): CalendarDate {
        return list[position]
    }

    private fun addPost(postData: CalendarDate) {
        if (!list.contains(postData)) {
            list.add(postData)
            notifyItemInserted(list.size - 1)
        }
    }

    fun removeFooter() {
        if (list.size != 0 && isLoadingAdded) {
            isLoadingAdded = false
            val position: Int = list.size - 1
            val result = getItem(position)
            if (result != null) {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
    }


    class MyFooterHolder(val binding: ItemNotificationFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: CalendarDate) {
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            TeamNotificationViewHolder(
                ItemTeamNotificationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else MyFooterHolder(
            ItemNotificationFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.apply {
//            val item = date[bindingAdapterPosition]
//            binding.tvDate.text = item.dateOfMonth.toString()
//            binding.tvDay.text = item.dayOfWeek
//            binding.redView.isVisible = item.dateOfMonth == selectedDate
//            binding.root.setOnClickListener {
//                selectedDate = item.dateOfMonth
//                notifyDataSetChanged()
//                callback(item)
//            }
        }
    }

}