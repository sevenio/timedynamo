package com.tvisha.trooptime.activity.activity.Adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.To_employee;
import com.tvisha.trooptime.activity.activity.LeaveRequestActivity;
import com.tvisha.trooptime.activity.activity.PermissionRequestActivity;
import com.tvisha.trooptime.activity.activity.RequestCompoffActivity;
import com.tvisha.trooptime.activity.activity.SwapActivity;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;


public class ToNamesAdapter extends RecyclerView.Adapter<ToNamesAdapter.ViewHolder> {
    private List<To_employee> items = new ArrayList<>();
    private Context context;

    public ToNamesAdapter(Context context, List<To_employee> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public ToNamesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.to_name_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ToNamesAdapter.ViewHolder viewHolder, final int i) {

        try {


            viewHolder.name.setText(items.get(i).getName());

            viewHolder.cancelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof LeaveRequestActivity) {
                        ((LeaveRequestActivity) context).removeToEmployee(i);
                    }
                    if (context instanceof PermissionRequestActivity) {
                        ((PermissionRequestActivity) context).removeToEmployee(i);
                    }
                    if (context instanceof RequestCompoffActivity) {
                        ((RequestCompoffActivity) context).removeToEmployee(i);
                    }
                    if (context instanceof SwapActivity) {
                        ((SwapActivity) context).removeToEmployee(i);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<To_employee> to_employees) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(to_employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private FrameLayout cancelLayout;


        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            cancelLayout = view.findViewById(R.id.cancelLayout);


        }
    }


}

