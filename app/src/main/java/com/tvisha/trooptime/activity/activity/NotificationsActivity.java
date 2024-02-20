package com.tvisha.trooptime.activity.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.adapter.NotificationsAdapter;
import com.tvisha.trooptime.activity.activity.model.NotificationModel;
import com.tvisha.trooptime.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton actionButton1;
    TextView actionLable;
    RecyclerView notificatuinList;
    LinearLayoutManager layoutManager;

    List<NotificationModel> notificationModels;

    NotificationsAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        actionLable = (TextView) findViewById(R.id.actionLable);
        actionLable.setText("Notifications");
        actionButton1 = (ImageButton) findViewById(R.id.actionButton1);
        actionButton1.setVisibility(View.GONE);
        notificatuinList = (RecyclerView) findViewById(R.id.notificatuinList);
        layoutManager = new LinearLayoutManager(this);
        notificatuinList.setLayoutManager(layoutManager);

        setNotifications();
    }


    public void setNotifications() {
        try {
            if (notificationModels == null) {
                notificationModels = new ArrayList<NotificationModel>();
            }
            for (int n = 0; n < 15; n++) {
                NotificationModel model = new NotificationModel();
                model.setTitle("Title " + n);
                model.setSubject("Subject " + n);
                model.setTime("Time " + n);
                model.setNotificationType(n);
                notificationModels.add(model);
            }

            if (nAdapter == null) {
                nAdapter = new NotificationsAdapter(notificationModels);
                notificatuinList.setAdapter(nAdapter);
            } else {
                nAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionBack:
                onBackPressed();
                break;
        }

    }
}
