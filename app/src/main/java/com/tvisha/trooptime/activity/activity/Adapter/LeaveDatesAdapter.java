package com.tvisha.trooptime.activity.activity.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tvisha.trooptime.activity.activity.ApiPostModels.Leave;
import com.tvisha.trooptime.activity.activity.LeaveRequestActivity;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;

public class LeaveDatesAdapter extends RecyclerView.Adapter<LeaveDatesAdapter.ViewHolder> {
    String[] mStrings = new String[0];
    private ArrayList<Leave> items = new ArrayList<>();
    private Context context;

    public LeaveDatesAdapter(Context context, ArrayList<Leave> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public LeaveDatesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.halfday_request_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LeaveDatesAdapter.ViewHolder viewHolder, final int i) {
        try {

            Typeface poppins_medium = Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Medium.ttf");
            viewHolder.leave_date.setText(items.get(i).getDate());
            mStrings = new String[3];
            mStrings[0] = "Full Day";
            mStrings[1] = "First Half";
            mStrings[2] = "Second Half";

            viewHolder.reuest_spinner.setItems(mStrings);
            viewHolder.reuest_spinner.setTypeface(poppins_medium);
            viewHolder.reuest_spinner.setHintTextColor(context.getResources().getColor(R.color.req_text_color));
            viewHolder.reuest_spinner.setTextColor(context.getResources().getColor(R.color.req_text_color));
            viewHolder.reuest_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int pos, long id, String item) {

                    String selectedItem = item;
                    int position = pos + 1;
                    items.get(i).setKey(String.valueOf(position));
                    if (context instanceof LeaveRequestActivity) {
                        ((LeaveRequestActivity) context).updateDatesArrayList(i, position);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }

    }

    public void setList(List<Leave> leaveList) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(leaveList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView leave_date;
        MaterialSpinner reuest_spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            leave_date = itemView.findViewById(R.id.leave_date);
            reuest_spinner = itemView.findViewById(R.id.reuest_spinner);
        }
    }

}

