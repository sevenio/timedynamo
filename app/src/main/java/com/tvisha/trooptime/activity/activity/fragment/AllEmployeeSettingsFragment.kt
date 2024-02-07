package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tvisha.trooptime.databinding.FragmentAllSettingsBinding

class AllEmployeeSettingsFragment: Fragment() {

    private lateinit var binding: FragmentAllSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}