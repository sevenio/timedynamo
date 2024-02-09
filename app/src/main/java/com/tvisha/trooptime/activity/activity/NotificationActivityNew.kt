package com.tvisha.trooptime.activity.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
//import androidx.navigation.NavController
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.NavigationUI
import com.tvisha.trooptime.R
import com.tvisha.trooptime.databinding.NotificationLayoutBinding


class NotificationActivityNew : AppCompatActivity() {

    private lateinit var binding: NotificationLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupToolbar()
    }

    private fun setupToolbar() {
        val navController: NavController = findNavController(R.id.main_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
            Log.d("ganga", "fallback")
            finish()
            true
        }.build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }



    override fun onSupportNavigateUp(): Boolean {

        if (!(findNavController(R.id.main_nav_host_fragment).navigateUp() || super.onSupportNavigateUp())) {
            onBackPressedDispatcher.onBackPressed()
        }
        return true
    }

}