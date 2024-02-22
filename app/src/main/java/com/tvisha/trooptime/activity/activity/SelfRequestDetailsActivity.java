package com.tvisha.trooptime.activity.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.adapter.CommentsAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Comment;
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.RequestCommentsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.SendCommentResponse;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfRequestDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    String empName = "", reqType = "", subReqType = "", frmDt = "", toDt = "", msg = "", id = "", type = "", reqDt = "", swapWithName = "", status = "", swap_status = "",
            permissionDate = "", daysCount = "", acceptedBy = "", requestedBy = "", swapTo = "", reruestTypeApi = "",ot_duration="";
    TextView employeeName, employeeName1, message, reasonTitle, subRequestType, fromdate, toDate, title, requestDate, swapWithEmployeeName, pendingAtUser,
            pendingAtManager, fromTime, toTime, days_count_tv, action_by_tv, action_by_name_tv,ot_duration_tv;
    Button requestType;
    EditText commentEditText;
    ApiInterface apiService;
    ImageView send, arrow1, arrow;
    LinearLayout backLayout, requestStatusLayout, timeLayout, swapNameLayout, daysCountLayout, statusLayout,
            approveRejectLayout,durationLayout;
    FrameLayout approveLayout, rejectLayout;
    LinearLayout nameLayout;
    ArrayList<Comment> comments = null;
    RecyclerView.LayoutManager manager;
    CommentsAdapter commentsAdapter;
    SharedPreferences sharedPreferences;
    String userId = "", apiKey = "";
    View line;
    CustomProgressBar customProgressBar;
    String startDate = "", endDate = "";
    private RecyclerView recyclerView;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(SelfRequestDetailsActivity.this).isFinishing()) {
                            if (customProgressBar != null && !customProgressBar.isShowing()) {
                                customProgressBar.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeProgress() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(SelfRequestDetailsActivity.this).isFinishing()) {
                            if (customProgressBar != null && customProgressBar.isShowing()) {
                                customProgressBar.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Helper.getInstance().closeKeyBoard(SelfRequestDetailsActivity.this,commentEditText);
        super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_request_details);

        setupUI(findViewById(R.id.rr_main_layout));

        shareprefernceData();
        initViews();
        initListeners();
        processActivity();

    }

    private void processActivity() {

        try {

            closeKeyboard();
            getIntentData();
            updateViews();

            commentEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    commentEditText.setFocusable(true);
                    commentEditText.setCursorVisible(true);
                    commentEditText.requestFocus();

                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);
                    return true;
                }
            });


            commentEditText.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent event) {
                    commentEditText.setCursorVisible(true);
                    // TODO Auto-generated method stub
                    if (view.getId() == R.id.comment) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;

                            case MotionEvent.ACTION_DOWN:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateViews() {
        try {

            if (msg != null && !msg.trim().isEmpty()) {
                message.setText(msg);
            } else {
                reasonTitle.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
            }


            fromdate.setText(frmDt.trim());
            toDate.setText(toDt);
            subRequestType.setText(subReqType);

            requestType.setText(reqType);
            title.setText(reqType);

            if (reqType.trim().equals("Swap")) {
                nameLayout.setVisibility(View.GONE);
                requestType.setVisibility(View.GONE);
                fromdate.setText(frmDt.trim().replace("-", ""));
                arrow.setVisibility(View.VISIBLE);


            }
            if (reqType.trim().equals("Comp Off")) {
                requestType.setVisibility(View.GONE);
                fromdate.setText(frmDt.trim().replace("-", ""));
                arrow.setVisibility(View.VISIBLE);
            }

            if (reqType.trim().equals("Over Time")) {
                requestType.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
                subRequestType.setVisibility(View.GONE);
                if(ot_duration!=null && !ot_duration.trim().isEmpty())
                {
                    durationLayout.setVisibility(View.VISIBLE);
                    ot_duration_tv.setText(ot_duration+"");
                }


            }

            if (reqType.trim().equals("Leave")) {

                if (daysCount != null && !daysCount.trim().isEmpty()) {
                    daysCountLayout.setVisibility(View.VISIBLE);
                    days_count_tv.setText(daysCount);
                }

                if (type != null && !type.trim().isEmpty()) {
                    if (Integer.parseInt(type) == Constants.LeaveTypes.CasualLeave) {
                        requestType.setText("Casual Leave");
                    } else if (Integer.parseInt(type) == Constants.LeaveTypes.SickLeave) {
                        requestType.setText("Sick Leave");
                    } else if (Integer.parseInt(type) == Constants.LeaveTypes.OptionalHoliday) {
                        // requestType.setText("Optional Holiday");
                        requestType.setText("Optional Leave");
                    }
                }

                if (toDt != null && !toDt.trim().isEmpty()) {
                    arrow.setVisibility(View.VISIBLE);
                } else {
                    arrow.setVisibility(View.GONE);
                }

                fromdate.setText(frmDt.trim().replace("-", ""));
            }
            if (reqType.trim().equals("Permission")) {
                requestType.setText(subReqType.replace("-", ""));
                fromdate.setText(permissionDate);
                toDate.setText("");
                arrow.setVisibility(View.GONE);
                timeLayout.setVisibility(View.VISIBLE);
                if (toDt != null && !toDt.trim().isEmpty()) {
                    fromTime.setText(frmDt);
                } else {
                    fromTime.setText(frmDt);
                }

                toTime.setText(toDt);
            } else {
                timeLayout.setVisibility(View.GONE);
            }

            if (reqDt.trim().contains("Today")) {
                requestDate.setText("Applied On" + reqDt);
            } else {
                requestDate.setText("Applied On " + reqDt);
            }

            if (swapWithName != null && !swapWithName.trim().isEmpty() && !swapWithName.trim().equals("0")) {
                swapWithEmployeeName.setText(" " + swapWithName);
                swapWithEmployeeName.setVisibility(View.VISIBLE);
                arrow1.setVisibility(View.VISIBLE);
                employeeName1.setText(empName + " ");

                requestStatusLayout.setVisibility(View.VISIBLE);

                if (status != null && !status.trim().isEmpty() && swap_status != null && !swap_status.trim().isEmpty()) {
                    if (Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                        pendingAtManager.setText("Pending At Reporting Manager");

                    } else if (Integer.parseInt(status) == Constants.REQUEST_STATUS_APPROVED) {
                        pendingAtManager.setText("Accepted By Reporting Manager");

                    } else {
                        pendingAtManager.setText("Rejected By Reporting Manager");

                    }

                    if (Integer.parseInt(swap_status) == Constants.REQUEST_STATUS_PENDING) {
                        pendingAtUser.setText("Pending At " + swapWithName);
                        if (swapTo != null && !swapTo.trim().isEmpty() && swapTo.equals(userId)) {
                            approveRejectLayout.setVisibility(View.VISIBLE);
                        }

                    } else if (Integer.parseInt(swap_status) == Constants.REQUEST_STATUS_APPROVED) {
                        pendingAtUser.setText("Accepted By " + swapWithName);

                    } else {
                        pendingAtUser.setText("Rejected By " + swapWithName);

                    }


                }


            } else {
                requestStatusLayout.setVisibility(View.GONE);
                employeeName1.setVisibility(View.GONE);
                employeeName.setText(empName + " ");
                swapNameLayout.setVisibility(View.GONE);
            }

            if (status != null && !status.trim().isEmpty() && status.equals("1")) {
                statusLayout.setVisibility(View.VISIBLE);
                action_by_tv.setText("Approved by: ");
            } else if (status != null && !status.trim().isEmpty() && status.equals("2")) {
                statusLayout.setVisibility(View.VISIBLE);
                action_by_tv.setText("Rejected by: ");
            }

            if (acceptedBy != null && !acceptedBy.trim().isEmpty()) {
                action_by_name_tv.setText(acceptedBy);
            }


            recyclerView.setHasFixedSize(true);
            manager = new LinearLayoutManager(SelfRequestDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setNestedScrollingEnabled(false);
            commentsAdapter = new CommentsAdapter(SelfRequestDetailsActivity.this, comments);
            recyclerView.setAdapter(commentsAdapter);

            if (comments != null && comments.size() > 0) {
                line.setVisibility(View.VISIBLE);
            } else {
                line.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViews() {
        try {
            customProgressBar = new CustomProgressBar(SelfRequestDetailsActivity.this);
            apiService = new ApiClient().getInstance();

            employeeName = findViewById(R.id.employeeName);
            employeeName1 = findViewById(R.id.employeeName1);
            swapWithEmployeeName = findViewById(R.id.swapWithEmployeeName);
            message = findViewById(R.id.message);
            reasonTitle = findViewById(R.id.reasonTitle);
            fromdate = findViewById(R.id.from);
            toDate = findViewById(R.id.to);
            subRequestType = findViewById(R.id.subRequestType);
            nameLayout = findViewById(R.id.nameLayout);
            requestType = findViewById(R.id.requestType);
            backLayout = findViewById(R.id.backLayout);
            requestStatusLayout = findViewById(R.id.requestStatusLayout);
            daysCountLayout = findViewById(R.id.daysCountLayout);
            approveRejectLayout = findViewById(R.id.approveRejectLayout);
            timeLayout = findViewById(R.id.timeLayout);
            statusLayout = findViewById(R.id.statusLayout);
            durationLayout = findViewById(R.id.durationLayout);
            approveLayout = findViewById(R.id.approveLayout);
            rejectLayout = findViewById(R.id.rejectLayout);
            swapNameLayout = findViewById(R.id.swapNameLayout);
            pendingAtUser = findViewById(R.id.pendingAtUser);
            pendingAtManager = findViewById(R.id.pendingAtManager);
            commentEditText = findViewById(R.id.comment);
            send = findViewById(R.id.send);
            arrow1 = findViewById(R.id.arrow1);
            arrow = findViewById(R.id.arrow);
            title = findViewById(R.id.title);
            line = findViewById(R.id.line);
            days_count_tv = findViewById(R.id.days_count_tv);
            action_by_tv = findViewById(R.id.action_by_tv);
            action_by_name_tv = findViewById(R.id.action_by_name_tv);
            ot_duration_tv = findViewById(R.id.ot_duration_tv);
            requestDate = findViewById(R.id.requestDate);
            fromTime = findViewById(R.id.fromTime);
            toTime = findViewById(R.id.toTime);
            recyclerView = findViewById(R.id.single_self_request_recycler_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getIntentData() {

        try {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                empName = bundle.getString("employeeName");
                reqType = bundle.getString("requestTypeName");
                reruestTypeApi = bundle.getString("requestType");
                subReqType = bundle.getString("subRequestType");
                frmDt = bundle.getString("fromDate").replace(" ","");
                toDt = bundle.getString("toDate").replace(" ","");
                startDate = bundle.getString("startDate").replace(" ","");
                endDate = bundle.getString("endDate").replace(" ","");
                msg = bundle.getString("message");
                id = bundle.getString("id");
                type = bundle.getString("type");
                reqDt = bundle.getString("requestDate");
                comments = bundle.getParcelableArrayList("comments");
                swapWithName = bundle.getString("swapWithName");
                status = bundle.getString("status");
                swap_status = bundle.getString("swapStatus");
                permissionDate = bundle.getString("permissionDate");
                daysCount = bundle.getString("daysCount");
                acceptedBy = bundle.getString("acceptedBy");
                requestedBy = bundle.getString("requestedBy");
                requestedBy = bundle.getString("requestedBy");
                swapTo = bundle.getString("swapTo");
                ot_duration = bundle.getString("ot_duration");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListeners() {
        try {
            backLayout.setOnClickListener(this);
            approveLayout.setOnClickListener(this);
            rejectLayout.setOnClickListener(this);
            send.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.backLayout:
                    onBackPressed();
                    break;
                case R.id.send:
                    validateForm();
                    break;
                case R.id.approveLayout:
                    approveLayout.setClickable(false);
                    if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {
                        approveRequest(commentEditText.getText().toString(), "0");
                    } else {
                        approveLayout.setClickable(true);
                        Toast.makeText(SelfRequestDetailsActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.rejectLayout:
                    rejectLayout.setClickable(false);
                    if (commentEditText != null && !commentEditText.getText().toString().isEmpty() && commentEditText.getText().toString().length() > 0) {
                        if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {

                            rejectRequest(commentEditText.getText().toString(), "2");
                        } else {
                            rejectLayout.setClickable(true);
                            Toast.makeText(SelfRequestDetailsActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        closeKeyboard();
                        Utilities.showSnackBar(commentEditText, "Please enter reason and tap on Reject");
                        rejectLayout.setClickable(true);

                        commentEditText.setFocusable(true);
                        commentEditText.setCursorVisible(true);
                        commentEditText.requestFocus();

                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);

                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateForm() {
        try {
            send.setClickable(false);
            if (commentEditText != null && !commentEditText.getText().toString().isEmpty() && commentEditText.getText().toString().length() > 0) {
                if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {
                    sendComment(commentEditText.getText().toString(), "0");

                } else {
                    send.setClickable(true);

                    Toast.makeText(SelfRequestDetailsActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }


            } else {
                closeKeyboard();
                Utilities.showSnackBar(commentEditText, "Please enter comment");
                send.setClickable(true);
                commentEditText.setFocusable(true);
                commentEditText.setCursorVisible(true);
                commentEditText.requestFocus();

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendComment(String comment, String commentType) {
        try {
            closeKeyboard();
            openProgress();
            Call<SendCommentResponse> call = apiService.sendComment(apiKey, userId, id, comment, commentType);
            call.enqueue(new Callback<SendCommentResponse>() {
                @Override
                public void onResponse(Call<SendCommentResponse> call, Response<SendCommentResponse> response) {
                    SendCommentResponse apiResponse = response.body();

                    if (apiResponse != null) {

                        send.setClickable(true);
                        if (apiResponse.isSuccess()) {

                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);

                            //callUserRequestListApi();
                            if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {
                                callCommentsApi();

                            }


                        } else {
                            Toast.makeText(SelfRequestDetailsActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(SelfRequestDetailsActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                        closeProgress();
                        send.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<SendCommentResponse> call, Throwable t) {


                    closeProgress();
                    send.setClickable(true);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callCommentsApi() {
        try {
            Call<RequestCommentsResponse> call = apiService.getRequestComments(apiKey, userId, id);
            call.enqueue(new Callback<RequestCommentsResponse>() {
                @Override
                public void onResponse(Call<RequestCommentsResponse> call, Response<RequestCommentsResponse> response) {
                    RequestCommentsResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getComments() != null && !apiResponse.getComments().isEmpty() && apiResponse.getComments().size() > 0) {
                                comments = (ArrayList<Comment>) apiResponse.getComments();
                                commentsAdapter.setList(comments);
                                commentsAdapter.notifyDataSetChanged();
                                if (comments != null && !comments.isEmpty() && comments.size() > 0) {
                                    line.setVisibility(View.VISIBLE);
                                } else {
                                    line.setVisibility(View.GONE);
                                }
                            }

                        }

                    } else {
                        closeProgress();
                    }

                }

                @Override
                public void onFailure(Call<RequestCommentsResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupUI(View view) {
        try {


            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        closeKeyboard();
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void approveRequest(String comment, String commentType) {
        try {
            closeKeyboard();
            openProgress();

            String statusType = "";

            if (reruestTypeApi.trim().equals("4")) {
                statusType = String.valueOf(Constants.ROLE.USER);
            }

            Call<CommonResponse> call = apiService.approveRequest(apiKey, userId, id, comment, commentType, reruestTypeApi, statusType);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        approveLayout.setClickable(true);

                        if (apiResponse.isSuccess()) {


                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);
                            Utilities.showSnackBar(commentEditText, apiResponse.getMessage());
                            approveRejectLayout.setVisibility(View.GONE);
                            //callTeamRequestListApi();
                            pendingAtUser.setText("Accepted By " + swapWithName);
                            if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {
                                callCommentsApi();
                            }

                        } else {
                            Toast.makeText(SelfRequestDetailsActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(SelfRequestDetailsActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                        closeProgress();
                        approveLayout.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {


                    approveLayout.setClickable(true);
                    closeProgress();

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rejectRequest(String comment, String commentType) {
        try {
            closeKeyboard();
            openProgress();
            String statusType = "";

            if (reruestTypeApi.trim().equals("4")) {
                statusType = String.valueOf(Constants.ROLE.USER);
            }
            Call<CommonResponse> call = apiService.rejectRequest(apiKey, userId, id, comment, commentType, reruestTypeApi, statusType);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        rejectLayout.setClickable(true);

                        if (apiResponse.isSuccess()) {


                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);
                            Utilities.showSnackBar(commentEditText, apiResponse.getMessage());
                            approveRejectLayout.setVisibility(View.GONE);
                            //callTeamRequestListApi();
                            pendingAtUser.setText("Rejected By " + swapWithName);
                            if (Utilities.isNetworkAvailable(SelfRequestDetailsActivity.this)) {
                                callCommentsApi();
                            }

                        } else {
                            Toast.makeText(SelfRequestDetailsActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        rejectLayout.setClickable(true);
                        closeProgress();

                        Toast.makeText(SelfRequestDetailsActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    closeProgress();

                    rejectLayout.setClickable(true);


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
