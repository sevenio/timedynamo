package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.activity.activity.viewmodels.SettingsViewmodel
import com.tvisha.trooptime.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val tabNames = arrayOf("Self", "Team")
    private val viewModel: SettingsViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {

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
                    binding.tvSelfSettings.isVisible = tab.position == 0
                    binding.ivAllEmployees.isVisible = tab.position == 1
                    val tabView = tab.customView
                    val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)
                    tabName?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_color
                        )
                    )
                    binding.viewPager.currentItem = tab.position
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
                    tabName?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_color
                        )
                    )
                }
            })
            binding.tlSettings.getTabAt(0)?.select()
            binding.ivAllEmployees.setOnClickListener {
                val navController = view?.let { Navigation.findNavController(it) }
                navController?.navigate(SettingsFragmentDirections.actionSettingsFragmentToSelectEmployeeSettingsFragment())
            }
            binding.btnSave.setOnClickListener {
                Log.d(
                    "sav",
                    "${viewModel.isMuteSelfNotifications.value}, ${viewModel.isMuteSelfCheckinNotifications.value} , ${viewModel.isMuteSelfCheckoutNotifications}, ${viewModel.isMuteTeamNotifications}, ${viewModel.isMuteTeamCheckoutNotifications}"
                )
            }
        }

        return binding.root
    }



    private fun setupViewPager(){
        val viewPagerAdapter = ViewPagerAdapter(listOf(SelfSettingsFragment(), AllEmployeeSettingsFragment()), childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isSaveEnabled = false
    }

    class ViewPagerAdapter(private val fragments: List<Fragment>, fragmentManager: FragmentManager,lifecycle: Lifecycle ) :
        FragmentStateAdapter( fragmentManager, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }


}