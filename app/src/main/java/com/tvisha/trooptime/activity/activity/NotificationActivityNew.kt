package com.tvisha.trooptime.activity.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tvisha.trooptime.activity.activity.Fragment.SelfNotificationFragment
import com.tvisha.trooptime.activity.activity.Fragment.TeamNotificationFrgment
import com.tvisha.trooptime.activity.activity.db.NotificationTables
import com.tvisha.trooptime.R
import com.tvisha.trooptime.databinding.NotificationLayoutBinding


class NotificationActivityNew : AppCompatActivity(), ViewPager.OnPageChangeListener {
    private lateinit var binding: NotificationLayoutBinding
    var fragment : Fragment? = null
    var fragmentCount = 2
    var notificationPagerAdapter : NotificationPagerAdapter? = null
    var selfTab : ConstraintLayout? = null
    var teamTab : ConstraintLayout? = null
    companion object {
        lateinit var notificationTable : NotificationTables
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_layout)
        initview()
    }
    fun initview(){
        binding.actionBar.actionButton1.visibility = View.GONE
        binding.actionBar.actionLable.text = getString(R.string.Notification)
        binding.actionBar.actionBack.setOnClickListener {
            onBackPressed()
        }
        notificationTable = NotificationTables(this)
        notificationPagerAdapter = NotificationPagerAdapter(supportFragmentManager)
        binding.notificationViewPager.adapter = notificationPagerAdapter
        binding.tlTabView.setupWithViewPager(binding.notificationViewPager)
        binding.tlTabView.isSmoothScrollingEnabled = true
        createTabs()
        binding.notificationViewPager.swipeLocked = true
        binding.tlTabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })
        binding.notificationViewPager.addOnPageChangeListener(this)
    }
    private fun createTabs(){
        selfTab = LayoutInflater.from(this).inflate(R.layout.custom_tab,null) as ConstraintLayout
        val textView : TextView = selfTab!!.findViewById(R.id.tvTabName)
        textView.text = getString(R.string.Self)
        val layoutParams: ViewGroup.LayoutParams = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        selfTab!!.background = ContextCompat.getDrawable(this,R.drawable.tab_select_background)
        selfTab!!.layoutParams = layoutParams
        binding.tlTabView.getTabAt(0)!!.customView = selfTab
        teamTab = LayoutInflater.from(this).inflate(R.layout.custom_tab,null) as ConstraintLayout
        val textView1 : TextView = teamTab!!.findViewById(R.id.tvTabName)
        textView1.text = getString(R.string.Team)
        teamTab!!.layoutParams = layoutParams
        binding.tlTabView.getTabAt(1)!!.customView = teamTab
    }
    inner class NotificationPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        override fun getItem(position: Int): Fragment {
            when(position){
                0->{
                    fragment = SelfNotificationFragment()
                }
                1->{
                    fragment = TeamNotificationFrgment()
                }
            }
            return fragment!!
        }

        override fun getCount(): Int {
            return fragmentCount
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        when(position){
            0->{
                selfTab!!.background = ContextCompat.getDrawable(this,R.drawable.tab_select_background)
                teamTab!!.background = null
            }
            1->{
                teamTab!!.background = ContextCompat.getDrawable(this,R.drawable.tab_select_background)
                selfTab!!.background = null
            }
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}