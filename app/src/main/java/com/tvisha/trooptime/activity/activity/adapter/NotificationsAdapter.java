package com.tvisha.trooptime.activity.activity.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tvisha.trooptime.activity.activity.helper.CircleTransform;
import com.tvisha.trooptime.activity.activity.model.NotificationModel;
import com.tvisha.trooptime.R;

import java.util.List;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    List<NotificationModel> notificationModels;
    Context context;

    public NotificationsAdapter(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final NotificationModel model = getItem(position);
            if (position == 0) {
                holder.progressLineTop.setVisibility(View.INVISIBLE);
            } else {
                holder.progressLineTop.setVisibility(View.VISIBLE);
            }

            holder.lateLoginOptions.setVisibility(View.GONE);
            holder.commentsList.setVisibility(View.GONE);

            if (model.getNotificationType() == 0 || model.getNotificationType() == 5) {
                holder.lateLoginOptions.setAdapter(new LateLoginOptionsAdapter("1"));
                holder.lateLoginOptions.setVisibility(View.VISIBLE);
            }
            if (model.getNotificationType() == 2) {
                holder.commentsList.setAdapter(new NotificationCommentAdapter());
                holder.commentsList.setVisibility(View.VISIBLE);
            }

            if (model.getNotificationType() == 0) {
                holder.userPic.setImageResource(R.drawable.ic_late_login_list_icon);
            } else {
                Picasso.get()
                        .load(R.drawable.test_img)
                        .transform(new CircleTransform())
                        .into(holder.userPic);
            }
            holder.title.setText(model.getTitle());
            holder.time.setText(model.getTime());
            holder.subject.setText(model.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public NotificationModel getItem(int position) {
        return notificationModels.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject, time;
        View progressLineTop, progressLineBottom;
        RecyclerView lateLoginOptions, commentsList;
        ImageView userPic;
        StaggeredGridLayoutManager layoutManager, commentsLayoutManager;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            progressLineTop = (View) itemView.findViewById(R.id.progressLineTop);
            progressLineBottom = (View) itemView.findViewById(R.id.progressLineBottom);
            userPic = (ImageView) itemView.findViewById(R.id.userPic);
            title = (TextView) itemView.findViewById(R.id.notificationTitle);
            time = (TextView) itemView.findViewById(R.id.notificationTime);
            subject = (TextView) itemView.findViewById(R.id.notificationSubject);
            lateLoginOptions = (RecyclerView) itemView.findViewById(R.id.lateLoginOptions);
            commentsList = (RecyclerView) itemView.findViewById(R.id.commentsList);
            progressLineTop.setVisibility(View.GONE);


            layoutManager = new StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL);
            commentsLayoutManager = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
            lateLoginOptions.setLayoutManager(layoutManager);
            commentsList.setLayoutManager(commentsLayoutManager);

            commentsList.setVisibility(View.GONE);
            lateLoginOptions.setVisibility(View.GONE);
        }
    }
}
