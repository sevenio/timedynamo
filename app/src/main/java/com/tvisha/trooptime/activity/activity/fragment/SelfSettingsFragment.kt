package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tvisha.trooptime.databinding.FragmentSelfSettingsBinding

class SelfSettingsFragment : Fragment() {

    private lateinit var binding: FragmentSelfSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


}