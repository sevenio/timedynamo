package com.tvisha.trooptime.activity.activity.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationFragmentType
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.databinding.FragmentNotificationsBinding
import com.tvisha.trooptime.databinding.ItemCalendarDateBinding
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    private val tabNames = arrayOf("Self", "Team")

    private val viewModel: NotificationViewmodel by viewModels()
    private lateinit var customProgressBar: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("sett", "onCreate")

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("sett", "onCreateView")

        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        customProgressBar = CustomProgressBar(requireContext())
        setupViewPager()
        setupTabs()
        setupDates(Calendar.getInstance())
        viewModel.fragmentType.observe(viewLifecycleOwner) { notificationFragmentType ->
            when (notificationFragmentType) {
                NotificationFragmentType.SELF -> viewModel.notificationCalendars.value?.selfCalendar?.let {
                    setupDates(
                        it
                    )
                }
                NotificationFragmentType.TEAM -> viewModel.notificationCalendars.value?.teamCalendar?.let {
                    setupDates(
                        it
                    )
                }
                else -> {}
            }
        }
        viewModel.notificationCalendars.observe(viewLifecycleOwner) {
            when (viewModel.fragmentType.value) {
                NotificationFragmentType.SELF -> setupDates(it.selfCalendar)
                NotificationFragmentType.TEAM -> setupDates(it.teamCalendar)
                null -> {}
            }
        }
        binding.tlSettings.getTabAt(viewModel.selectedTab)?.select()



        return binding.root
    }

    private fun setupTabs() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SharePreferenceKeys.SP_NAME,
                Context.MODE_PRIVATE
            )
        val teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false)
        if (!teamLead) {
            binding.tlSettings.removeTabAt(1)
        }
        for (i in 0..binding.tlSettings.tabCount) {
            val tab: TabLayout.Tab? = binding.tlSettings.getTabAt(i)
            val tabView = tab?.customView
            val rootView: LinearLayout? = tabView?.findViewById(R.id.ll_root)
            val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
            when (i) {
                0 -> {
                    if (!teamLead) {
                        rootView?.setBackgroundResource(R.drawable.bg_tab_main)
                    } else {
                        rootView?.setBackgroundResource(R.drawable.bg_tab_item_left)
                    }
                }
                else -> rootView?.setBackgroundResource(R.drawable.bg_tab_item_right)
            }

            tabName?.text = tabNames[i];
            tab?.customView = tabView
        }
        binding.tlSettings.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.selectedTab = tab.position
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_color))
                binding.viewPager.currentItem = tab.position
                when (tab.position) {
                    0 -> viewModel.updateType(NotificationFragmentType.SELF)
                    1 -> viewModel.updateType(NotificationFragmentType.TEAM)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_notification_text
                    )
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                tabName?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_color))
            }
        })
    }

    private fun setupDates(calendar: Calendar) {
        lifecycleScope.launch {
            val year = calendar.get(Calendar.YEAR)

            val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)

            binding.tvMonth.text = month
            binding.tvYear.text = "$year"

            binding.tvMonth.setOnClickListener {
                selectMonth(calendar)
            }

            binding.tvYear.setOnClickListener {
                selectYear(calendar)
            }
            setupDatesRecyclerView(getDatesList(calendar), calendar)
            binding.rvDates.scrollToPosition(calendar.get(Calendar.DAY_OF_MONTH) - 1)
        }
    }


    private fun selectMonth(calendar: Calendar) {

        val builder = MonthPickerDialog.Builder(
            requireContext(),
            { selectedMonth, selectedYear ->
                calendar.set(Calendar.MONTH, selectedMonth)

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val calendarYear = calendar.get(Calendar.YEAR)
                val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
                val calendarMonth = calendar.get(Calendar.MONTH)
                if (currentYear == calendarYear && currentMonth == calendarMonth) {
                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                }
                viewModel.updateCalendar(calendar)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH)
        )

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val selectedYear = calendar.get(Calendar.YEAR)
        if (selectedYear == currentYear) {
            builder.setMonthRange(Calendar.JANUARY, Calendar.getInstance().get(Calendar.MONTH))
        }
        builder.setActivatedMonth(calendar.get(Calendar.MONTH))
            .showMonthOnly()
            .setOnMonthChangedListener {

            }
            .build().show()
    }

    private fun selectYear(calendar: Calendar) {

        val builder = MonthPickerDialog.Builder(
            requireContext(),
            { selectedMonth, selectedYear ->
                Log.d("cal", "month $selectedMonth year $selectedYear")
                calendar.set(Calendar.YEAR, selectedYear)
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val calendarYear = calendar.get(Calendar.YEAR)
                val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
                val calendarMonth = calendar.get(Calendar.MONTH)
                if (currentYear == calendarYear && currentMonth == calendarMonth) {
                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                } else if (currentYear == calendarYear) {
                    calendar.set(
                        Calendar.MONTH,
                        Calendar.getInstance().get(Calendar.MONTH)
                    )
                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                }
                viewModel.updateCalendar(calendar)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH)
        )

        builder.setActivatedYear(calendar.get(Calendar.YEAR))
            .showYearOnly()
            .setYearRange(2016, Calendar.getInstance().get(Calendar.YEAR))
            .setOnYearChangedListener {
                Log.d("cal", "year $it")
//                calendar.set(Calendar.MONTH, it)
            }
            .build().show()


    }

    private fun setupDatesRecyclerView(list: List<CalendarDate>, currentCalendar: Calendar) {
        binding.rvDates.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvDates.adapter = DatesRecyclerViewAdapter(
            list,
            currentCalendar.get(Calendar.DAY_OF_MONTH)
        ) { calendarDate ->

            val calendar = when (viewModel.fragmentType.value) {
                NotificationFragmentType.SELF -> viewModel.notificationCalendars.value?.selfCalendar
                NotificationFragmentType.TEAM -> viewModel.notificationCalendars.value?.teamCalendar
                null -> null
            }

            calendar?.let {
                val updatedCalendar = Calendar.getInstance()
                updatedCalendar.time = it.time
                updatedCalendar.set(Calendar.DAY_OF_MONTH, calendarDate.dateOfMonth)
                viewModel.updateCalendar(updatedCalendar)
            }
        }
    }

    private fun getDatesList(calendar: Calendar): List<CalendarDate> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val selectedYear = calendar.get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val selectedMonth = calendar.get(Calendar.MONTH)
        val date = if (currentYear == selectedYear && selectedMonth == currentMonth) {
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        } else {
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        val list = List(date) { (it + 1) }.map {
            val newCalendar = Calendar.getInstance()
            newCalendar.time = calendar.time
            newCalendar.set(Calendar.DAY_OF_MONTH, it)
            CalendarDate(
                calendar = newCalendar,
                dateOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH),
                dayOfWeek = SimpleDateFormat("EE", Locale.getDefault()).format(newCalendar.time)
            )
        }
        return list

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


    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(
            listOf(SelfNotificationFragment(), TeamNotificationFrgment()),
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )
        binding.viewPager.adapter = viewPagerAdapter

    }

    fun openProgress() {
        lifecycleScope.launch {
            if (!customProgressBar.isShowing) {
                customProgressBar.show()
            }
        }
    }

    fun closeProgress() {
        lifecycleScope.launch {
            if (customProgressBar.isShowing) {
                customProgressBar.dismiss()
            }
        }
    }


}

class ViewPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

class DatesRecyclerViewAdapter(
    val date: List<CalendarDate>,
    private var selectedDate: Int,
    val callback: (CalendarDate) -> Unit
) :
    RecyclerView.Adapter<DatesRecyclerViewAdapter.DateViewHolder>() {

    class DateViewHolder(val binding: ItemCalendarDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            ItemCalendarDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return date.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.apply {
            val item = date[bindingAdapterPosition]
            binding.tvDate.text = item.dateOfMonth.toString()
            binding.tvDay.text = item.dayOfWeek
            binding.redView.isVisible = item.dateOfMonth == selectedDate
            binding.root.setOnClickListener {
                selectedDate = item.dateOfMonth
                notifyDataSetChanged()
                callback(item)
            }
        }
    }

}

data class CalendarDate(val calendar: Calendar, val dateOfMonth: Int, val dayOfWeek: String)