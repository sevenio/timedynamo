package com.tvisha.trooptime.activity.activity.Adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tvisha.trooptime.activity.activity.Helper.CircleTransform;
import com.tvisha.trooptime.R;

public class NotificationCommentAdapter extends RecyclerView.Adapter<NotificationCommentAdapter.ViewHolder> {

    Context context;
    String msgs[] = {"i don`t know", "when will u come..!", "can you able to come meet me", "where are you..?", "i don`t know", "when will u come..!", "can you able to come meet me", "where are you..?"};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_sub_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.comment.setText(msgs[position]);
            Picasso.get()
                    .load(R.drawable.test_img)
                    .transform(new CircleTransform())
                    .into(holder.userPic);

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0 ? R.color.notification_bg2 : R.color.notification_bg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return msgs.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        ImageView userPic;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            context = itemView.getContext();
            comment = (TextView) itemView.findViewById(R.id.userComment);
            userPic = (ImageView) itemView.findViewById(R.id.userPic);
        }
    }

}
