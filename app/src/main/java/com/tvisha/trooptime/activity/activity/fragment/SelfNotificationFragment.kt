package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tvisha.trooptime.activity.activity.Adapter.NotificationsNewAdapter
import com.tvisha.trooptime.activity.activity.Model.Notification
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.databinding.ItemCalendarDateBinding
import com.tvisha.trooptime.databinding.ItemSelfNotificationBinding
import com.tvisha.trooptime.databinding.NotificationListLayoutBinding

class SelfNotificationFragment : Fragment() {
    private lateinit var binding: NotificationListLayoutBinding
    var linearLayoutManager : LinearLayoutManager? = null
    var notificationAdapter: NotificationsNewAdapter?= null
    var isLoading:Boolean = false
    var noMoreData:Boolean = false
    var selfNotificationList:ArrayList<Notification> =  ArrayList()
    val viewModel: NotificationViewmodel by viewModels(ownerProducer ={ requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotificationListLayoutBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notificationCalendars.observe(viewLifecycleOwner){
            Log.d("mod", "SelfNotificationFragment ${Gson().toJson(it)}")
        }
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.rlNotificationList.layoutManager = linearLayoutManager
        binding.rlNotificationList.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount: Int = linearLayoutManager!!.itemCount
                val pastVisiblesItems: Int = linearLayoutManager!!.findLastCompletelyVisibleItemPosition()
                //val scrollPosition: Int = linearLayoutManager!!.findLastVisibleItemPosition()
                if (!noMoreData && !isLoading &&  totalItemCount-1<=pastVisiblesItems && selfNotificationList.size>50) {
//                    fetchList()
                }
            }
        })

//        fetchList()

        /*for (i in 0 until 5){
            var notification = Notification()
            notification.created_at = "2023-10-16 0"+i+":00:00"
            notification.notificationtype = i
            notification.notificationdata = "{\"title\": \"Leave request\",\"description\": \"Need leave on monday due to some work\",\"leave_start_date\": \"2023-10-16\",\"leave_end_date\": \"2023-10-16\",\"no_of_days\": \"1 day\"}"
            NotificationActivityNew.selfNotificationList.add(notification)
        }
        notificationAdapter = NotificationsNewAdapter(requireActivity(),NotificationActivityNew.selfNotificationList,0)
        rlNotificationList.adapter = notificationAdapter*/

    }

//    private fun fetchList() {
//        isLoading = true
//        Thread{
//            var list =  NotificationActivityNew.notificationTable.getNotifications(0,selfNotificationList.size)
//            selfNotificationList.addAll(list)
//            isLoading = false
//            if (selfNotificationList!=null && selfNotificationList.size>0){
//                setAdapter()
//            }else{
//                noMoreData = true
//                requireActivity().runOnUiThread {
//                    binding.noNotifications.visibility = View.VISIBLE
//                }
//            }
//        }.start()
//    }
//    fun setAdapter(){
//        requireActivity().runOnUiThread {
//            binding.noNotifications.visibility = View.GONE
//            if (notificationAdapter==null){
//                notificationAdapter = NotificationsNewAdapter(requireActivity(),selfNotificationList,0)
//                binding.rlNotificationList.adapter = notificationAdapter
//            }else{
//                notificationAdapter!!.notifyDataSetChanged()
//            }
//        }
//    }
}

class SelfNotificationRecyclerViewAdapter(
    val date: List<CalendarDate>,
    private var selectedDate: Int,
    val callback: (CalendarDate) -> Unit
) :
    RecyclerView.Adapter<SelfNotificationRecyclerViewAdapter.SelfNotificationViewHolder>() {

    class SelfNotificationViewHolder(val binding: ItemSelfNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfNotificationViewHolder {
        return SelfNotificationViewHolder(
            ItemSelfNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return date.size
    }

    override fun onBindViewHolder(holder: SelfNotificationViewHolder, position: Int) {
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