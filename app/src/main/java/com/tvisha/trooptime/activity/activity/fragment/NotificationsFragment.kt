package com.tvisha.trooptime.activity.activity.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.AttendanceActivity
import com.tvisha.trooptime.activity.activity.Helper.Constants
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.databinding.FragmentNotificationsBinding
import com.tvisha.trooptime.databinding.ItemCalendarDateBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    private val tabNames = arrayOf("Self", "Team")

    private val viewModel: NotificationViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        setupViewPager()
        setupTabs()
        setupDates()
        return binding.root
    }

    private fun setupTabs(){
        for (i in 0..binding.tlSettings.tabCount) {
            val tab: TabLayout.Tab? = binding.tlSettings.getTabAt(i)
            val tabView = tab?.customView
            val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
            tabName?.text = tabNames[i];
            tab?.customView = tabView
        }
        binding.tlSettings.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_color))
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_notification_text))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_color))
            }
        })
        binding.tlSettings.getTabAt(0)?.select()
    }

    private fun setupDates(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)

        binding.tvMonth.text = month
        binding.tvYear.text = "${year}"

        binding.tvMonth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            showCalendarDialog(year,month, dayOfMonth)
        }

        binding.tvYear.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            showCalendarDialog(year,month, dayOfMonth)
        }
        setupDatesRecyclerView()

    }

    private fun showCalendarDialog(year:Int, month: Int, dayOfMonth: Int){
        try {
            val pd = MonthYearDialog()
            pd.setListener { p0, p1, p2, p3 -> }
            pd.show(childFragmentManager, "MonthYearPickerDialog")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupDatesRecyclerView(){
        binding.rvDates.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.rvDates.adapter = DatesRecyclerViewAdapter(getDatesList())
    }

    private fun getDatesList(): List<CalendarDate>{
        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val list =  List(date){it + 1}.map {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, it)
            CalendarDate(calendar = calendar, dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH), dayOfWeek = SimpleDateFormat("EE", Locale.getDefault()).format(calendar.time))

        }
        return list

    }

    @SuppressLint("ValidFragment")
    class MonthYearDialog : androidx.fragment.app.DialogFragment() {
        private var listener: DatePickerDialog.OnDateSetListener? = null
        fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
            this.listener = listener
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val cal = Calendar.getInstance()
            val dialog = inflater.inflate(R.layout.date_picker_dialog, null)
            val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
            val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker
            monthPicker.minValue = 1
            monthPicker.maxValue = 12
            monthPicker.value = cal[Calendar.MONTH] + 1
            val year = 2016
            val current_year = cal[Calendar.YEAR]
            yearPicker.minValue = year
            yearPicker.maxValue = current_year
            yearPicker.value = current_year
            builder.setView(dialog) // Add action buttons
                .setPositiveButton("OK") { dialog, id ->
                    listener!!.onDateSet(null, yearPicker.value, monthPicker.value, 0)
                    AttendanceActivity.dialog_year = yearPicker.value
                    AttendanceActivity.dialog_month = monthPicker.value
                    val calendar = Calendar.getInstance()
                    calendar[AttendanceActivity.dialog_year, AttendanceActivity.dialog_month - 1] =
                        1
                    val days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    if (AttendanceActivity.dialog_month < 10) {
                        val mon = 0.toString() + "" + AttendanceActivity.dialog_month
                        AttendanceActivity.start_date =
                            "" + AttendanceActivity.dialog_year.toString() + "-" + mon + "-" + "01"
                    } else {
                        AttendanceActivity.start_date =
                            "" + AttendanceActivity.dialog_year.toString() + "-" + AttendanceActivity.dialog_month.toString() + "-" + "01"
                    }
                    if (AttendanceActivity.dialog_month < 10) {
                        val mon = 0.toString() + "" + AttendanceActivity.dialog_month
                        AttendanceActivity.end_date =
                            "" + AttendanceActivity.dialog_year.toString() + "-" + mon + "-" + days
                    } else {
                        AttendanceActivity.end_date =
                            "" + AttendanceActivity.dialog_year.toString() + "-" + AttendanceActivity.dialog_month.toString() + "-" + days
                    }
                    if (AttendanceActivity.handler != null) {
                        try {
                            val `object` = JSONObject()
                            `object`.put("month_position", AttendanceActivity.dialog_month - 1)
                            `object`.put("year", AttendanceActivity.dialog_year)
                            `object`.put("month", AttendanceActivity.dialog_month)
                            `object`.put("current_year", current_year)
                            `object`.put("start_date", AttendanceActivity.start_date)
                            `object`.put("end_date", AttendanceActivity.end_date)
                            AttendanceActivity.handler.obtainMessage(
                                Constants.Static.CHECK_IN,
                                `object`
                            ).sendToTarget()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, id -> this@MonthYearDialog.dialog?.cancel() }
            return builder.create()
        }



        companion object {
            private const val MAX_YEAR = 2099
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_notification, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        Log.d("ganga", "settings")
                        val navController = view.let { Navigation.findNavController(it) }
                        navController.navigate(NotificationsFragmentDirections.actionNotificationsFragmentToSettingsFragment())
                        true
                    }
                    R.id.volume -> {
                        Log.d("ganga", "volume")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.notificationUiModel.observe(viewLifecycleOwner){
            Log.d("mod", Gson().toJson(it))
        }
    }

    private fun setupViewPager(){
        val viewPagerAdapter = ViewPagerAdapter(listOf(SelfNotificationFragment(), TeamNotificationFrgment()), childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
    }

}

class ViewPagerAdapter(private val fragments: List<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle ) :
    FragmentStateAdapter( fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

class DatesRecyclerViewAdapter(val date: List<CalendarDate>, var selectedItemIndex: Int = if(date.isEmpty()) 0 else date.lastIndex ): RecyclerView.Adapter<DatesRecyclerViewAdapter.DateViewHolder>(){

    class DateViewHolder(val binding: ItemCalendarDateBinding ): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(ItemCalendarDateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return date.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val item = date[position]
        holder.binding.tvDate.text = item.dateOfMonth.toString()
        holder.binding.tvDay.text = item.dayOfWeek
        holder.binding.redView.isVisible = position == selectedItemIndex
        holder.binding.root.setOnClickListener {
            val oldpositon  = selectedItemIndex
            selectedItemIndex = position
            notifyItemChanged(oldpositon)
            notifyItemChanged(position)
        }
    }

}

data class CalendarDate(val calendar: Calendar, val dateOfMonth: Int, val dayOfWeek : String)