package com.tvisha.trooptime.activity.activity.Adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.Cc_employess;
import com.tvisha.trooptime.activity.activity.LeaveRequestActivity;
import com.tvisha.trooptime.activity.activity.PermissionRequestActivity;
import com.tvisha.trooptime.activity.activity.RequestCompoffActivity;
import com.tvisha.trooptime.activity.activity.SwapActivity;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;


public class CcNamesAdapter extends RecyclerView.Adapter<CcNamesAdapter.ViewHolder> {
    String[] mStrings = new String[0];
    private List<Cc_employess> items = new ArrayList<>();
    private Context context;

    public CcNamesAdapter(Context context, List<Cc_employess> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public CcNamesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.name_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CcNamesAdapter.ViewHolder viewHolder, final int i) {

        try {


            viewHolder.name.setText(items.get(i).getName());

            viewHolder.cancelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof LeaveRequestActivity) {
                        ((LeaveRequestActivity) context).removeCcEmployee(i);
                    }
                    if (context instanceof PermissionRequestActivity) {
                        ((PermissionRequestActivity) context).removeCcEmployee(i);
                    }
                    if (context instanceof RequestCompoffActivity) {
                        ((RequestCompoffActivity) context).removeCcEmployee(i);
                    }
                    if (context instanceof SwapActivity) {
                        ((SwapActivity) context).removeCcEmployee(i);
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

    public void setList(List<Cc_employess> cc_employesses) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(cc_employesses);
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

