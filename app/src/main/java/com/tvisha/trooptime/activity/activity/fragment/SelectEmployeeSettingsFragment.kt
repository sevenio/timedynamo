package com.tvisha.trooptime.activity.activity.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.app.MyApplication
import com.tvisha.trooptime.activity.activity.db.UsersModel
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.viewmodels.SelectEmployeesViewmodel
import com.tvisha.trooptime.databinding.FragmentSelectEmployeeSettingsBinding
import com.tvisha.trooptime.databinding.ItemSelectEmployeeBinding
import java.util.*

class SelectEmployeeSettingsFragment: Fragment() {
    private lateinit var binding: FragmentSelectEmployeeSettingsBinding
    private val viewModel: SelectEmployeesViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectEmployeeSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUsers()
        viewModel.userList.observe(viewLifecycleOwner){
            setupRecyclerView(it)
        }
    }


    private fun setupRecyclerView(list: List<UsersModel>){
        binding.rvEmployee.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmployee.adapter = EmployeesRecyclerViewAdapter(list, mutableSetOf() )


    }






}

class EmployeesRecyclerViewAdapter(val date: List<UsersModel>, var selectedItemSet: MutableSet<Int>): RecyclerView.Adapter<EmployeesRecyclerViewAdapter.EmployeeViewHolder>(){

    class EmployeeViewHolder(val binding: ItemSelectEmployeeBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(ItemSelectEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return date.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = date[position]
        val context = holder.itemView.context
        val options: RequestOptions = RequestOptions().circleCrop()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.avatar_placeholder_light))
            .error(ContextCompat.getDrawable(context, R.drawable.avatar_placeholder_light))
            .priority(Priority.HIGH)
        Glide.with(holder.itemView.context).load(context.getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE)?.getString(SharePreferenceKeys.AWS_BASE_URL, "") + item.userAvatar)
            .apply(options)
            .into(holder.binding.ivProfile)

    }

}

