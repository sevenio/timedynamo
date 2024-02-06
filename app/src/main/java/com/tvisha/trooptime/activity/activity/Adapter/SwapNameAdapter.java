package com.tvisha.trooptime.activity.activity.Adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.Users;
import com.tvisha.trooptime.activity.activity.SwapActivity;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;


public class SwapNameAdapter extends RecyclerView.Adapter<SwapNameAdapter.ViewHolder> {
    private List<Users> items = new ArrayList<>();
    private Context context;

    public SwapNameAdapter(Context context, List<Users> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public SwapNameAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.name_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        try {

            viewHolder.name.setText(items.get(i).getName());

            viewHolder.cancelLayout.setVisibility(View.VISIBLE);

            viewHolder.cancelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context instanceof SwapActivity) {
                        ((SwapActivity) context).removeSwapEmployee(i);
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

    public void setList(List<Users> users) {

        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(users);
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

