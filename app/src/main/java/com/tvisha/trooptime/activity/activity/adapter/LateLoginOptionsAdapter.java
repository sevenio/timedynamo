package com.tvisha.trooptime.activity.activity.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.ToastUtil;
import com.tvisha.trooptime.activity.activity.model.LateLoginOptions;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;


public class LateLoginOptionsAdapter extends RecyclerView.Adapter<LateLoginOptionsAdapter.ViewHolder> {
    Context context;
    List<LateLoginOptions> lateLoginOptionses;
    String optionLables[] = {"I'm on my way", "Permission", "Leave", "Call Manager"};
    String notificationId;

    public LateLoginOptionsAdapter(String notificationId) {
        this.notificationId = notificationId;
        setOptins();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_late_login_option, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LateLoginOptions model = getItem(position);
        holder.option.setText(model.getOption());

    }

    public LateLoginOptions getItem(int position) {
        return lateLoginOptionses.get(position);
    }

    @Override
    public int getItemCount() {
        return lateLoginOptionses.size();
    }

    public void setOptins() {
        try {
            if (lateLoginOptionses == null) {
                lateLoginOptionses = new ArrayList<LateLoginOptions>();
                for (int i = 0; i < optionLables.length; i++) {
                    LateLoginOptions model = new LateLoginOptions();
                    model.setOption(optionLables[i]);
                    model.setId(String.valueOf(i));

                    lateLoginOptionses.add(model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView option;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
            option = (TextView) itemView.findViewById(R.id.option);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item:
                    if (getAdapterPosition() == 0) {
                        ToastUtil.showToast(context, "Comming soon...!");
                    } else if (getAdapterPosition() == 1) {
                        Navigation.getInstance().permissionRequest(context);

                    } else if (getAdapterPosition() == 2) {
                        Navigation.getInstance().leaveRequest(context);
                    } else {
                        ToastUtil.showToast(context, "Comming soon...!");
                    }
                    break;
            }
        }
    }
}
