package com.tvisha.trooptime.activity.activity.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.Comment;
import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private ArrayList<Comment> items = new ArrayList<>();
    private Context context;

    public CommentsAdapter(Context context, ArrayList<Comment> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_self_request_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.ViewHolder viewHolder, final int i) {

        try {


            if (i == getItemCount() - 1) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }

            viewHolder.employeeName.setText(Helper.getInstance().capitalize(items.get(i).getUsername()));
            viewHolder.message.setText(Helper.getInstance().replaceSpecialChar(Helper.getInstance().capitalize(items.get(i).getComment())));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Comment> comments) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(comments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView employeeName, message;
        private ImageView statusImage;
        private View line;

        public ViewHolder(View view) {
            super(view);

            employeeName = view.findViewById(R.id.employeeName);
            message = view.findViewById(R.id.message);
            statusImage = view.findViewById(R.id.statusImg);
            line = view.findViewById(R.id.line);

        }
    }


}

