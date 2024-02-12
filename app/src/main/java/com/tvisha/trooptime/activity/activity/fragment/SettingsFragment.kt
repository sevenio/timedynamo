package com.tvisha.trooptime.activity.activity.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.viewmodels.SettingsViewmodel
import com.tvisha.trooptime.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val tabNames = arrayOf("Self", "Team")
    private val viewModel: SettingsViewmodel by viewModels()
    private lateinit var customProgressBar: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("sett", "onCreate")
        viewModel.getSettings()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("sett", "onCreateView")

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        customProgressBar = CustomProgressBar(requireContext())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabs()
        setupClickListeners()
        setupObservers()
        binding.tlSettings.getTabAt(viewModel.selectedTab)?.select()


    }

    private fun setupObservers() {
        viewModel.showProgress.observe(viewLifecycleOwner) {
            if (it) {
                openProgress()
            } else {
                closeProgress()
            }
        }
    }

    private fun setupClickListeners() {

        binding.ivAllEmployees.setOnClickListener {
            val navController = view?.let { Navigation.findNavController(it) }
            navController?.navigate(SettingsFragmentDirections.actionSettingsFragmentToSelectEmployeeSettingsFragment())
        }
        binding.btnSave.setOnClickListener {
            viewModel.saveSettings()
        }
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

            val tabName: TextView? = tabView?.findViewById(R.id.tv_tab_name)

            tabName?.text = tabNames[i];

            tab?.customView = tabView
//            binding.contacsTabsView.addTab(tab)
        }
        binding.tlSettings.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.selectedTab = tab.position
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
    }


    private fun setupViewPager() {
        val viewPagerAdapter = SettingsViewPagerAdapter(
            listOf(SelfSettingsFragment(), AllEmployeeSettingsFragment()),
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

class SettingsViewPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}