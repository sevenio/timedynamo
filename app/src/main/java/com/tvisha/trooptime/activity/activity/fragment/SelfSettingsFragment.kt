package com.tvisha.trooptime.activity.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tvisha.trooptime.activity.activity.viewmodels.NotificationViewmodel
import com.tvisha.trooptime.activity.activity.viewmodels.SettingsViewmodel
import com.tvisha.trooptime.databinding.FragmentSelfSettingsBinding

class SelfSettingsFragment : Fragment() {

    private lateinit var binding: FragmentSelfSettingsBinding
    val viewModel: SettingsViewmodel by viewModels(ownerProducer ={ requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }


}