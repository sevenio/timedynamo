package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tvisha.trooptime.R
import com.tvisha.trooptime.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private val tabNames = arrayOf("Self", "Team")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        for (i in 0..binding.tlSettings.tabCount) {
            val tab: TabLayout.Tab? = binding.tlSettings.getTabAt(i)
            val tabView = tab?.customView

            val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)

            tabName?.text = tabNames[i];

            tab?.customView = tabView
//            binding.contacsTabsView.addTab(tab)
        }
        setupViewPager()

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

        return binding.root
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

    private fun setupViewPager(){
        val viewPagerAdapter = ViewPagerAdapter(listOf(SelfSettingsFragment(), AllEmployeeSettingsFragment()), childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(private val fragments: List<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle ) :
        FragmentStateAdapter( fragmentManager, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }




}