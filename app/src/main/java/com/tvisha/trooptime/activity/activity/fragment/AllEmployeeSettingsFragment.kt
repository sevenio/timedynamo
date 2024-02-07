package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tvisha.trooptime.activity.activity.viewmodels.SettingsViewmodel
import com.tvisha.trooptime.databinding.FragmentAllSettingsBinding

class AllEmployeeSettingsFragment: Fragment() {

    private lateinit var binding: FragmentAllSettingsBinding
    val viewModel: SettingsViewmodel by viewModels(ownerProducer ={ requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }


}