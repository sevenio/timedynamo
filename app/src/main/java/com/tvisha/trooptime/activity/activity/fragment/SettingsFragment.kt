package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tvisha.trooptime.R
import com.tvisha.trooptime.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val tabNames = arrayOf("Self", "Team")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        for (i in 0..binding.tlSettings.tabCount) {
            val tab: TabLayout.Tab? = binding.tlSettings.getTabAt(i)
            val tabView = tab?.customView

            val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)

            tabName?.text = tabNames[i];

            tab?.customView = tabView
//            binding.contacsTabsView.addTab(tab)
        }
        setupViewPager()

        binding.tlSettings.addOnTabSelectedListener(object : OnTabSelectedListener {
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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViewPager(){
        val viewPagerAdapter = ViewPagerAdapter(listOf(SelfSettingsFragment(), AllEmployeeSettingsFragment()), childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(private val fragments: List<Fragment>, fragmentManager: FragmentManager,lifecycle: Lifecycle ) :
        FragmentStateAdapter( fragmentManager, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }


}