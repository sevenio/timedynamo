package com.tvisha.trooptime.activity.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.Adapter.SelfRequestDataAdapter;
import com.tvisha.trooptime.activity.activity.Adapter.TeamRequestAdapter;
import com.tvisha.trooptime.activity.activity.ApiPostModels.SelfRequest;
import com.tvisha.trooptime.activity.activity.ApiPostModels.UserRequestListResponse;
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.ToastUtil;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SelfRequestFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public static LinearLayout dateLayout, filterDateLayout;
    public static int dialog_month = 0, dialog_year = 0;
    public static String start_date = "", end_date = "";
    public static Handler handler;
    public static boolean filter = false;
    public Context context;
    public Activity activity;
    RecyclerView.LayoutManager layoutManager;
    SelfRequestDataAdapter selfRequestDataAdapter;
    List<SelfRequest> selfRequestArrayList = new ArrayList<>();
    List<SelfRequest> tempSelfRequestArrayList = new ArrayList<>();
    ApiInterface apiService;
    int year, current_year, next_year, current_month, current_day, selected_position, month_position;
    RecyclerView month_calendar;
    ImageView closeImage;
    LinearLayout tabsLayout;
    TextView text_self, text_team, team_all, text_cc, filterDate, name_of_month;
    RecyclerView.LayoutManager teamLayoutManager;
    TeamRequestAdapter teamRequestAdapter;
    List<SelfRequest> teamRequestArrayList = new ArrayList<>();
    List<SelfRequest> tempTeamRequestArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userId, email, apiKey;
    String type = "";
    boolean self = false, team = false, admin = false, adminRole = false, is_full_access = false, request_all_tab=false,request_team_tab=false,isRefresh = false, cc = false;
    View rootView;
    FrameLayout addRequestLayout;
    String requestType = "", fromDate = "", toDate = "", users = "";
    ImageView noDataImageView;
    boolean teamLead = false;
    String dateFormat1 = "yyyy-MM-dd";
    String dateFormat = "dd MMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    String dateFormat2 = "dd MMM yy";
    SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2, Locale.US);
    String dateFormat3 = "dd MMM yyyy";
    SimpleDateFormat sdf3 = new SimpleDateFormat(dateFormat3, Locale.US);
    DatePickerDialog dialog;
    Calendar calendar;
    int month, day;
    Typeface poppins_bold, poppins_regular;
    CustomProgressBar customProgressBar;
    SwipeRefreshLayout swipe_refresh;
    int limit = 1000, offset = 0;
    long lastClicked = 0;
    private RecyclerView requestListRecycleView;
    private RecyclerView teamRequestListRecycleView;

    public void openProgress() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((activity != null)) {
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
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((activity != null)) {
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
    public void onResume() {
        try {

            offset = 0;
            isRefresh = true;
            if (self) {
                userApi();
            } else if (team) {

                teamApi();
            } else if (cc) {

                ccApi();
            } else {

                allApi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_self_request, container, false);
        context = ((RequestActivity) getActivity()).getContext();
        activity = ((RequestActivity) getActivity()).getActivity();

        try {
            customProgressBar = new CustomProgressBar(activity);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            handleIntent();
            shareprefernceData();
            initViews();
            initializeViews(rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;

    }

    private void handleIntent() {

        try {

            Bundle bundle = getArguments();

            if (bundle != null) {
                type = bundle.getString("type");
                requestType = bundle.getString("requestType");
                start_date = bundle.getString("fromDate");
                end_date = bundle.getString("toDate");
                users = bundle.getString("users");
                filter = bundle.getBoolean("filter");


            } else {
                requestType = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            addRequestLayout = rootView.findViewById(R.id.addRequest);
            dateLayout = rootView.findViewById(R.id.dateLayout);
            filterDateLayout = rootView.findViewById(R.id.filterDateLayout);
            filterDate = rootView.findViewById(R.id.filterDate);
            text_self = rootView.findViewById(R.id.text_self);
            text_team = rootView.findViewById(R.id.text_team);
            team_all = rootView.findViewById(R.id.text_all);
            text_cc = rootView.findViewById(R.id.text_cc);
            tabsLayout = rootView.findViewById(R.id.tabsLayout);

            noDataImageView = rootView.findViewById(R.id.noDataImage);
            closeImage = rootView.findViewById(R.id.closeImage);
            noDataImageView.setVisibility(View.GONE);
            swipe_refresh = rootView.findViewById(R.id.swipe_refresh);

            if (filter) {
                swipe_refresh.setEnabled(false);
            }

            poppins_bold = Typeface.createFromAsset(activity.getAssets(), "font/poppins/Poppins-Bold.ttf");
            poppins_regular = Typeface.createFromAsset(activity.getAssets(), "font/poppins/Poppins-Regular.ttf");

            text_self.setTypeface(poppins_regular);
            text_team.setTypeface(poppins_regular);
            filterDate.setTypeface(poppins_regular);
            team_all.setTypeface(poppins_regular);


            if (start_date != null && !start_date.trim().isEmpty() && start_date.length() > 3 && end_date != null && !end_date.trim().isEmpty() && end_date.length() > 3) {
                filterDateLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.GONE);
                try {
                    String fDt = getRequiredDate(start_date);
                    String tDt = getRequiredDate(end_date);

                    filterDate.setText(fDt.substring(0, 6) + "'" + fDt.substring(6) + " - " + tDt.substring(0, 6) + "'" + tDt.substring(6));
                    //filterDate.setText(getRequiredDate(start_date)+" - "+getRequiredDate(end_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                dateLayout.setVisibility(View.VISIBLE);
                filterDateLayout.setVisibility(View.GONE);
            }


            filterDateLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetForm();
                }
            });

            if ((sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE, 0) == Constants.UserRole.ADMIN && request_all_tab)|| (is_full_access && request_all_tab) || request_all_tab) {
                adminRole = true;
                team_all.setVisibility(View.VISIBLE);
                if ((sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false) && request_team_tab) || request_team_tab) {
                    text_team.setVisibility(View.VISIBLE);
                } else {
                    text_team.setVisibility(View.GONE);
                }
                text_cc.setVisibility(View.GONE);
              /*  if (sharedPreferences.getBoolean(SharePreferenceKeys.IS_CC_USER, false)) {
                    text_cc.setVisibility(View.VISIBLE);
                }
                else
                {
                    text_cc.setVisibility(View.GONE);
                }*/
                swipe_refresh.setEnabled(false);
            } else if ((sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false) && request_team_tab) || request_team_tab || sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false)) {
                teamLead = true;
                team_all.setVisibility(View.GONE);
                text_team.setVisibility(View.VISIBLE);
                swipe_refresh.setEnabled(false);
                if (sharedPreferences.getBoolean(SharePreferenceKeys.IS_CC_USER, false)) {
                    text_cc.setVisibility(View.VISIBLE);
                }
                else
                {
                    text_cc.setVisibility(View.GONE);
                }
            } else if (sharedPreferences.getBoolean(SharePreferenceKeys.IS_CC_USER, false)) {
                cc = true;
                team_all.setVisibility(View.GONE);
                text_team.setVisibility(View.GONE);
                text_self.setVisibility(View.VISIBLE);
                text_cc.setVisibility(View.VISIBLE);
                swipe_refresh.setEnabled(false);
            } else {
                team_all.setVisibility(View.GONE);
                text_team.setVisibility(View.GONE);
                text_self.setVisibility(View.GONE);
                swipe_refresh.setEnabled(true);
            }

/*

         if (sharedPreferences.getBoolean(SharePreferenceKeys.IS_CC_USER,false)){

            cc=true;
            team_all.setVisibility(View.GONE);
            text_team.setVisibility(View.GONE);
            text_self.setVisibility(View.VISIBLE);
            text_cc.setVisibility(View.VISIBLE);
            swipe_refresh.setEnabled(false);
        }else {
            team_all.setVisibility(View.GONE);
            text_team.setVisibility(View.GONE);
            text_self.setVisibility(View.GONE);
            swipe_refresh.setEnabled(true);
        }
*/


            initListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListeners() {
        try {
            dateLayout.setOnClickListener(this);
            text_self.setOnClickListener(this);
            text_team.setOnClickListener(this);
            team_all.setOnClickListener(this);
            text_cc.setOnClickListener(this);
            addRequestLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.dateLayout:
                    try {
                        MonthYearDialog pd = new MonthYearDialog();
                        pd.setListener(SelfRequestFragment.this);
                        pd.show(activity.getFragmentManager(), "MonthYearPickerDialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.text_self:
                    setTabData(true, false, false, false);
                    break;
                case R.id.text_team:
                    setTabData(false, true, false, false);
                    break;
                case R.id.text_all:
                    setTabData(false, false, true, false);
                    break;
                case R.id.text_cc:
                    setTabData(false, false, false, true);
                    break;
                case R.id.addRequest:
                    showRequestDialog();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTabData(boolean s, boolean t, boolean a, boolean c) {

        try {


            self = s;
            team = t;
            admin = a;
            cc = c;

            resetForm();

            if (team || admin || cc) {
                requestListRecycleView.setVisibility(View.GONE);
                teamRequestListRecycleView.setVisibility(View.VISIBLE);
            } else {
                requestListRecycleView.setVisibility(View.VISIBLE);
                teamRequestListRecycleView.setVisibility(View.GONE);
            }

            noDataImageView.setVisibility(View.GONE);


            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, self).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, team).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, admin).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.CC_CLICKED, cc).apply();

            if (self) {
                text_self.setTextColor(getResources().getColor(R.color.text_sleceted));
                text_self.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            } else {
                text_self.setTextColor(getResources().getColor(R.color.text_notselected));
                text_self.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            }

            if (team) {
                swipe_refresh.setEnabled(false);
                text_team.setTextColor(getResources().getColor(R.color.text_sleceted));
                text_team.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            } else {
                text_team.setTextColor(getResources().getColor(R.color.text_notselected));
                text_team.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            }

            if (admin) {
                swipe_refresh.setEnabled(false);
                team_all.setTextColor(getResources().getColor(R.color.text_sleceted));
                team_all.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            } else {
                team_all.setTextColor(getResources().getColor(R.color.text_notselected));
                team_all.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            }
            if (cc) {
                swipe_refresh.setEnabled(false);
                text_cc.setTextColor(getResources().getColor(R.color.text_sleceted));
                text_cc.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            } else {
                text_cc.setTextColor(getResources().getColor(R.color.text_notselected));
                text_cc.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            }

            //tabsLayout.setBackgroundColor(getResources().getColor(R.color.menu_bg_color));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void resetForm() {
        try {
            dateLayout.setVisibility(View.VISIBLE);
            filterDateLayout.setVisibility(View.GONE);
            start_date = "";
            end_date = "";

            thisMonth();

            boolean tempFilter = filter;
            filter = false;
            limit = 1000;
            offset = 0;

            requestType = "";
            if (self) {
                //swipe_refresh.setEnabled(true);
                // isRefresh=true;
                if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                    selfRequestArrayList.clear();
                    selfRequestDataAdapter.notifyDataSetChanged();
                }
                userApi();

            } else if (team) {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                teamApi();
            } else if (cc) {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                ccApi();
            } else {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                allApi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSelerequest() {

        if (MyApplication.UserRequestListResponse == null) {
            swipe_refresh.setEnabled(true);
            isRefresh = true;
            userApi();
        } else {

            swipe_refresh.setEnabled(true);
            isRefresh = true;
            userApi();
          /*  selfRequestArrayList.clear();
            selfRequestDataAdapter.notifyDataSetChanged();
            UserRequestListResponse apiResponse=MyApplication.UserRequestListResponse;
            if(apiResponse.getRequestData()!=null && apiResponse.getRequestData().size()>0)
            {
                //selfRequestArrayList = apiResponse.getRequestData();
                if(tempSelfRequestArrayList!=null && tempSelfRequestArrayList.size()>0){
                    tempSelfRequestArrayList.clear();
                }

                tempSelfRequestArrayList = apiResponse.getRequestData();
                selfRequestArrayList.addAll(apiResponse.getRequestData());
                //filterSelfRequests(tempSelfRequestArrayList);
                updateRequestAdapter();
            }*/
        }
    }

    private void showRequestDialog() {

        try {

            final androidx.appcompat.app.AlertDialog alertDialog;
            LayoutInflater factory = LayoutInflater.from(context);
            final View DialogView = factory.inflate(R.layout.dialog_request, null);
            androidx.appcompat.app.AlertDialog.Builder Dialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
            Dialog.setView(DialogView);
            alertDialog = Dialog.create();
            ImageButton requestClose;
            TextView request;
            requestClose = DialogView.findViewById(R.id.requestClose);
            request = DialogView.findViewById(R.id.request);

            alertDialog.show();
            requestClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int checkedID = ((RadioGroup) DialogView.findViewById(R.id.requestGroup)).getCheckedRadioButtonId();
                    if (checkedID == R.id.leaveRequest) {
                        Navigation.getInstance().leaveRequest(context);
                        alertDialog.dismiss();

                    } else if (checkedID == R.id.permissionRequest) {
                        Navigation.getInstance().permissionRequest(context);
                        alertDialog.dismiss();

                    } else if (checkedID == R.id.directClientVistRequest) {
                        Navigation.getInstance().directClientLocation(context);
                        alertDialog.dismiss();

                    } else if (checkedID == R.id.compoffrequest) {

                        Navigation.getInstance().compoffRequest(context);
                        alertDialog.dismiss();

                    } else if (checkedID == R.id.swapRequest) {

                        Navigation.getInstance().swapRequest(context);
                        alertDialog.dismiss();

                    } else {
                        ToastUtil.showToast(context, "Select request type to continue");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        try {
            sharedPreferences = activity.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
            self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
            team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
            admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);
            is_full_access = sharedPreferences.getBoolean(SharePreferenceKeys.FULL_ACCESS, false);
            request_team_tab = sharedPreferences.getBoolean(SharePreferenceKeys.request_team_tab, false);
            request_all_tab = sharedPreferences.getBoolean(SharePreferenceKeys.request_all_tab, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initializeViews(View view) {
        try {
            apiService = ApiClient.getClient().create(ApiInterface.class);
            requestListRecycleView = view.findViewById(R.id.self_request_recycler_view);
            requestListRecycleView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            requestListRecycleView.setLayoutManager(layoutManager);
            selfRequestDataAdapter = new SelfRequestDataAdapter(context, selfRequestArrayList, false, start_date, end_date);
            requestListRecycleView.setAdapter(selfRequestDataAdapter);
            requestListRecycleView.setNestedScrollingEnabled(false);


            teamRequestListRecycleView = view.findViewById(R.id.team_request_recycler_view);
            teamRequestListRecycleView.setHasFixedSize(true);
            teamLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            teamRequestListRecycleView.setLayoutManager(teamLayoutManager);
            teamRequestAdapter = new TeamRequestAdapter(context, teamRequestArrayList, start_date, end_date);
            teamRequestListRecycleView.setAdapter(teamRequestAdapter);
            teamRequestListRecycleView.setNestedScrollingEnabled(false);


            requestListRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerview, int dx, int dy) {
                    super.onScrolled(recyclerview, dx, dy);

                    LinearLayoutManager myLayoutManager = (LinearLayoutManager) requestListRecycleView.getLayoutManager();
                    int totalItemCount = myLayoutManager.getItemCount();
                    int pastVisiblesItems = myLayoutManager.findLastVisibleItemPosition();
                    int scrollPosition = myLayoutManager.findLastVisibleItemPosition();
                    if (dy > 0) {

                        if (Utilities.isNetworkAvailable(context)) {

                            if (selfRequestArrayList.size() == offset && myLayoutManager.findLastCompletelyVisibleItemPosition() == selfRequestArrayList.size() - 1) {
                                //bottom of list!
                                isRefresh = true;
                                userApi();
                            }

                        }


                    }

                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });

            teamRequestListRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerview, int dx, int dy) {
                    super.onScrolled(recyclerview, dx, dy);

                    LinearLayoutManager myLayoutManager = (LinearLayoutManager) teamRequestListRecycleView.getLayoutManager();
                    int totalItemCount = myLayoutManager.getItemCount();
                    int pastVisiblesItems = myLayoutManager.findLastVisibleItemPosition();
                    int scrollPosition = myLayoutManager.findLastVisibleItemPosition();
                    if (dy > 0) {

                        if (Utilities.isNetworkAvailable(context)) {
                            if (teamRequestArrayList.size() == offset && myLayoutManager.findLastCompletelyVisibleItemPosition() == teamRequestArrayList.size() - 1) {


                                if (team) {
                                    isRefresh = true;
                                    teamApi();
                                } else if (admin) {
                                    isRefresh = true;
                                    allApi();
                                } else if (cc) {
                                    isRefresh = true;
                                    ccApi();
                                }
                            }

                        }


                    }

                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });


            if (type.equals("1")) {
                if (!filter) {
                    thisMonth();
                }
                openUser();

            } else {


                if (adminRole || teamLead || is_full_access || cc) {
                    if (!filter) {
                        thisMonth();
                    }
                    if (team) {

                        openTeam();
                    } else if (admin) {
                        openAll();
                    } else if (cc) {
                        openCc();
                    } else {
                        openTeam();
                    }

                }

            }

            month_calendar = view.findViewById(R.id.month_caledar);
            name_of_month = view.findViewById(R.id.name_of_month);
            name_of_month.setTypeface(poppins_regular);

            Calendar c = Calendar.getInstance();
            String currentDate = sdf.format(c.getTime());
            name_of_month.setText(currentDate.substring(3, 6) + ", " + currentDate.substring(6));

            Calendar calendar = Calendar.getInstance();
            String day = new SimpleDateFormat("dd").format(calendar.getTime());
            current_day = Integer.parseInt(day);
            selected_position = Integer.parseInt(day) - 1;
            String months = new SimpleDateFormat("MM").format(calendar.getTime());
            current_month = Integer.parseInt(months);
            month_position = Integer.parseInt(months) - 1;
            String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
            year = Integer.parseInt(current_year);
            this.current_year = Integer.parseInt(current_year);
            next_year = Integer.parseInt(current_year + 1);

            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if (Utilities.isNetworkAvailable(context)) {
                        swipe_refresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_green_light);
                        if (!filter && self) {
                            swipe_refresh.setRefreshing(true);
                            isRefresh = true;
                            userApi();
                        }
                    } else {
                        Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                        swipe_refresh.setRefreshing(false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUser() {
        try {
            text_self.setTextColor(getResources().getColor(R.color.text_sleceted));
            text_self.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            text_team.setTextColor(getResources().getColor(R.color.text_notselected));
            text_team.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            team_all.setTextColor(getResources().getColor(R.color.text_notselected));
            team_all.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            text_cc.setTextColor(getResources().getColor(R.color.text_notselected));
            text_cc.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            requestListRecycleView.setVisibility(View.VISIBLE);
            teamRequestListRecycleView.setVisibility(View.GONE);
            noDataImageView.setVisibility(View.GONE);
            // userApi();
            if (!filter) {

                if (MyApplication.UserRequestListResponse == null) {

                    userApi();
                } else {

                    userApi();
                /*swipe_refresh.setEnabled(true);
                selfRequestArrayList.clear();
                selfRequestDataAdapter.notifyDataSetChanged();
                UserRequestListResponse apiResponse=MyApplication.UserRequestListResponse;
                if(apiResponse.getRequestData()!=null && apiResponse.getRequestData().size()>0)
                {
                    //selfRequestArrayList = apiResponse.getRequestData();
                    if(tempSelfRequestArrayList!=null && tempSelfRequestArrayList.size()>0){
                        tempSelfRequestArrayList.clear();
                    }

                    tempSelfRequestArrayList = apiResponse.getRequestData();
                    selfRequestArrayList.addAll(apiResponse.getRequestData());
                   // filterSelfRequests(tempSelfRequestArrayList);
                    updateRequestAdapter();
                }*/
                }

            } else {

                userApi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTeam() {
        try {
            text_team.setTextColor(getResources().getColor(R.color.text_sleceted));
            text_team.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            text_self.setTextColor(getResources().getColor(R.color.text_notselected));
            text_self.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            team_all.setTextColor(getResources().getColor(R.color.text_notselected));
            team_all.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            text_cc.setTextColor(getResources().getColor(R.color.text_notselected));
            text_cc.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            requestListRecycleView.setVisibility(View.GONE);
            teamRequestListRecycleView.setVisibility(View.VISIBLE);
            teamApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAll() {

        try {

            team_all.setTextColor(getResources().getColor(R.color.text_sleceted));
            team_all.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            text_self.setTextColor(getResources().getColor(R.color.text_notselected));
            text_self.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            text_team.setTextColor(getResources().getColor(R.color.text_notselected));
            text_team.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            text_cc.setTextColor(getResources().getColor(R.color.text_notselected));
            text_cc.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            requestListRecycleView.setVisibility(View.GONE);
            teamRequestListRecycleView.setVisibility(View.VISIBLE);

            allApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCc() {

        try {

            text_cc.setTextColor(getResources().getColor(R.color.text_sleceted));
            text_cc.setBackgroundColor(getResources().getColor(R.color.btn_active_bg_color));
            text_self.setTextColor(getResources().getColor(R.color.text_notselected));
            text_self.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            text_team.setTextColor(getResources().getColor(R.color.text_notselected));
            text_team.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            team_all.setTextColor(getResources().getColor(R.color.text_notselected));
            team_all.setBackgroundColor(getResources().getColor(R.color.btn_inactive_bg_color));
            requestListRecycleView.setVisibility(View.GONE);
            teamRequestListRecycleView.setVisibility(View.VISIBLE);

            ccApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callTeamRequestListApi() {

        try {

            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }


            team = true;
            self = false;
            admin = false;
            cc = false;
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.CC_CLICKED, false).apply();


            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }


            Call<UserRequestListResponse> call = apiService.getTeamRequestList(apiKey, userId, start_date, end_date, requestType, "", limit, offset);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (offset == 0) {
                                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                                    teamRequestArrayList.clear();
                                    //  teamRequestAdapter.notifyDataSetChanged();
                                }
                            }

                            offset = offset + limit;

                    /*    if (teamRequestArrayList!=null && teamRequestArrayList.size()>0) {
                            teamRequestArrayList.clear();
                            teamRequestAdapter.notifyDataSetChanged();
                        }*/

                            if (filter) {


                                if (!users.replace(" ","").equals("")) {



                                    String strings = users.toString().replace("[", "").replace("]", "").trim();
                                    String[] user = strings.split(",");
                                    for (int i = 0; i < user.length; i++) {

                                        if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {

                                            for (int j = 0; j < apiResponse.getRequestData().size(); j++) {
                                                if (apiResponse.getRequestData().get(j).getRequest_by().equals(user[i])) {
                                                    teamRequestArrayList.add(apiResponse.getRequestData().get(j));
                                                }
                                            }
                                        }

                                    }
                                } else {


                                    if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {


                                        teamRequestArrayList.addAll(apiResponse.getRequestData());


                                    }

                                }
                                updateTeamRequestAdapter(teamRequestArrayList, true);
                            } else {
                                if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                    if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                        teamRequestArrayList.addAll(apiResponse.getRequestData());
                                        updateTeamRequestAdapter(teamRequestArrayList, false);
                                    } else {
                                        updateTeamRequestList(apiResponse.getRequestData(), false);
                                    }

                                }

                            }


                        } else {



                            if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                noDataImageView.setVisibility(View.VISIBLE);
                            }
                            if (filter) {
                                teamRequestArrayList.clear();
                                updateTeamRequestAdapter(teamRequestArrayList, false);
                                noDataImageView.setVisibility(View.VISIBLE);
                            }


                        }

                    } else {

                        closeProgress();
                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAllRequestListApi() {
        try {
            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }
            team = false;
            self = false;
            admin = true;
            cc = false;
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.CC_CLICKED, false).apply();

            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }
            Call<UserRequestListResponse> call = apiService.getAllRequestList(apiKey, userId, start_date, end_date, requestType, "", limit, offset);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {


                            if (offset == 0) {
                                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                                    teamRequestArrayList.clear();
                                    //  teamRequestAdapter.notifyDataSetChanged();
                                }
                            }

                            offset = offset + limit;
                       /* if (teamRequestArrayList!=null && teamRequestArrayList.size()>0) {
                            teamRequestArrayList.clear();
                            teamRequestAdapter.notifyDataSetChanged();
                        }*/

                            if (filter) {
                                if (!users.replace(" ","").equals("")) {
                                    String strings = users.toString().replace("[", "").replace("]", "").trim();
                                    String[] user = strings.split(",");
                                    for (int i = 0; i < user.length; i++) {

                                        if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                            for (int j = 0; j < apiResponse.getRequestData().size(); j++) {
                                                if (apiResponse.getRequestData().get(j).getRequest_by().equals(user[i])) {
                                                    teamRequestArrayList.add(apiResponse.getRequestData().get(j));
                                                }
                                            }
                                        }

                                    }
                                } else {
                                    if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                        // teamRequestArrayList = apiResponse.getRequestData();
                                        teamRequestArrayList.addAll(apiResponse.getRequestData());
                                        // filterTeamAllRequests(tempTeamRequestArrayList);


                                    }

                                }


                                updateTeamRequestAdapter(teamRequestArrayList, true);
                            } else {
                                if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {

                                    if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                        teamRequestArrayList.addAll(apiResponse.getRequestData());
                                        updateTeamRequestAdapter(teamRequestArrayList, false);
                                    } else {
                                        updateTeamRequestList(apiResponse.getRequestData(), false);
                                    }


                                }

                            }


                        } else {
                            if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                updateTeamRequestAdapter(teamRequestArrayList, false);
                                noDataImageView.setVisibility(View.VISIBLE);
                            }
                            if (filter) {
                                teamRequestArrayList.clear();
                                updateTeamRequestAdapter(teamRequestArrayList, false);
                                noDataImageView.setVisibility(View.VISIBLE);
                            }


                        }

                    } else {
                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCcRequestListApi() {
        try {
            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }

            team = false;
            self = false;
            admin = false;
            cc = true;
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.CC_CLICKED, true).apply();


            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }

            Call<UserRequestListResponse> call = apiService.getCcRequestList(apiKey, userId, start_date, end_date, requestType, "", limit, offset);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {


                            if (offset == 0) {
                                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                                    teamRequestArrayList.clear();
                                    //  teamRequestAdapter.notifyDataSetChanged();
                                }
                            }

                            offset = offset + limit;

/*

                        if (teamRequestArrayList!=null && teamRequestArrayList.size()>0) {
                            teamRequestArrayList.clear();
                            teamRequestAdapter.notifyDataSetChanged();
                        }
*/

                            if (filter) {

                                if (!users.equals("")) {

                                    String strings = users.toString().replace("[", "").replace("]", "").trim();
                                    String[] user = strings.split(",");
                                    for (int i = 0; i < user.length; i++) {

                                        if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {

                                            for (int j = 0; j < apiResponse.getRequestData().size(); j++) {
                                                if (apiResponse.getRequestData().get(j).getRequest_by().equals(user[i])) {
                                                    teamRequestArrayList.add(apiResponse.getRequestData().get(j));
                                                }
                                            }
                                        }

                                    }
                                } else {
                                    if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {

                                        teamRequestArrayList.addAll(apiResponse.getRequestData());


                                    }

                                }
                                updateTeamRequestAdapter(teamRequestArrayList, true);
                            } else {
                                if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {


                                    if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                        teamRequestArrayList.addAll(apiResponse.getRequestData());
                                        updateTeamRequestAdapter(teamRequestArrayList, false);

                                    } else {
                                        updateTeamRequestList(apiResponse.getRequestData(), false);
                                    }

                           /*  selfRequestArrayList.addAll(apiResponse.getRequestData());
                             updateRequestAdapter();*/
                                }

                            }


                        } else {
                            if (teamRequestArrayList == null || teamRequestArrayList.size() == 0) {
                                noDataImageView.setVisibility(View.VISIBLE);
                            }

                            if (filter) {
                                teamRequestArrayList.clear();
                                updateTeamRequestAdapter(teamRequestArrayList, false);
                                noDataImageView.setVisibility(View.VISIBLE);
                            }


                        }

                    } else {

                        closeProgress();
                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterSelfRequests(List<SelfRequest> tempSelfRequestArrayList) {

        if (tempSelfRequestArrayList != null && tempSelfRequestArrayList.size() > 0) {
            for (int i = 0; i < tempSelfRequestArrayList.size(); i++) {
                boolean isAdd = false;
                String requestType = tempSelfRequestArrayList.get(i).getRequest_type().trim();

                if (Integer.parseInt(requestType) == Constants.REQUEST_LEAVE) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_PERMISSION) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_COMPOFF) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_SWAP) {
                    isAdd = true;
                } else {
                    isAdd = false;
                }

                if (isAdd) {
                    selfRequestArrayList.add(tempSelfRequestArrayList.get(i));
                }
            }

        }
    }

    private void filterTeamAllRequests(List<SelfRequest> tempTeamRequestArrayList) {
        if (tempTeamRequestArrayList != null && tempTeamRequestArrayList.size() > 0) {
            for (int i = 0; i < tempTeamRequestArrayList.size(); i++) {
                boolean isAdd = false;
                String requestType = tempTeamRequestArrayList.get(i).getRequest_type().trim();

                if (Integer.parseInt(requestType) == Constants.REQUEST_LEAVE) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_PERMISSION) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_COMPOFF) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_SWAP) {
                    isAdd = true;
                } else {
                    isAdd = false;
                }

                if (isAdd) {
                    teamRequestArrayList.add(tempTeamRequestArrayList.get(i));
                }
            }

        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //yyyy-MM-dd

        try {

            String d;
            if (month < 10) {
                d = "01" + "-" + "0" + month + "-" + year;
            } else {
                d = "01" + "-" + month + "-" + year;
            }
            try {
                getSelfRequiredDate(d);
                getUserDate(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void thisMonth() {
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentmonthFirst = dateFormat.format(c.getTime());
            // set actual maximum date of previous month
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            String cuurentmonthLast = dateFormat.format(c.getTime());

            start_date = currentmonthFirst;
            end_date = cuurentmonthLast;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserDate(String date) throws ParseException {
        try {


            //yyyy-MM-dd
            Date d = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentmonthFirst = dateFormat.format(c.getTime());
            // set actual maximum date of previous month
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            String cuurentmonthLast = dateFormat.format(c.getTime());

            start_date = currentmonthFirst;
            end_date = cuurentmonthLast;
            filter = true;
            limit = 1000;
            offset = 0;
            requestType = "";
            if (self) {
                if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                    selfRequestArrayList.clear();
                    selfRequestDataAdapter.notifyDataSetChanged();
                }
                userApi();
            } else if (team) {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                teamApi();
            } else if (admin) {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                allApi();
            } else if (cc) {
                if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                    teamRequestArrayList.clear();
                    teamRequestAdapter.notifyDataSetChanged();
                }
                ccApi();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pickFromDate() {

        dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String started_date = "" + String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(started_date);
                    String select_date = dateFormat.format(date);
                    dateLayout.setVisibility(View.GONE);
                    filterDateLayout.setVisibility(View.VISIBLE);
                    filterDate.setText(getRequiredDate(select_date));
                    start_date = select_date;
                    end_date = select_date;
                    requestType = "";
                    filter = false;
                    callTeamRequestListApi();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, year, month, day);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        dialog.show();
    }

    private void callUserRequestListApi() {

        try {


            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }


            self = true;
            team = false;
            admin = false;
            cc = false;
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.CC_CLICKED, false).apply();


            Call<UserRequestListResponse> call = apiService.getUserRequestList(apiKey, userId, start_date, end_date, requestType, "", limit, offset);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {

                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (offset == 0) {
                                if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                                    selfRequestArrayList.clear();
                                    // selfRequestDataAdapter.notifyDataSetChanged();
                                }
                            }

                            offset = offset + limit;

                            if (!filter) {
                                MyApplication.UserRequestListResponse = apiResponse;
                            }
                            if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {

                                sharedPreferences.edit().putString(SharePreferenceKeys.USER_AVATAR,apiResponse.getRequestData().get(0).getRequest_by_user_avatar()).apply();
                                if (selfRequestArrayList == null || selfRequestArrayList.size() == 0) {
                                    noDataImageView.setVisibility(View.GONE);
                                    selfRequestArrayList.addAll(apiResponse.getRequestData());
                                    updateRequestAdapter();
                                } else {
                                    updateSelfRequestList(apiResponse.getRequestData());
                                }


                            }


                        } else {
                            // Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            if (selfRequestArrayList == null || selfRequestArrayList.size() == 0) {
                                updateRequestAdapter();
                                noDataImageView.setVisibility(View.VISIBLE);
                            }
                            if (filter) {
                                selfRequestArrayList.clear();
                                updateRequestAdapter();
                                noDataImageView.setVisibility(View.VISIBLE);
                            }


                        }

                    } else {


                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();
                    swipe_refresh.setRefreshing(false);


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSelfRequestList(List<SelfRequest> requestData) {

        try {

            if (requestData != null && requestData.size() > 0) {
                for (int i = 0; i < requestData.size(); i++) {
                    boolean isPresent = false;
                    for (int j = 0; j < selfRequestArrayList.size(); j++) {
                        if (selfRequestArrayList.get(j).getId().equals(requestData.get(i))) {
                            isPresent = true;
                        }

                    }
                    if (!isPresent) {
                        selfRequestArrayList.add(requestData.get(i));
                    }
                }
                selfRequestDataAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTeamRequestList(List<SelfRequest> requestData, boolean b) {
        try {
            if (requestData != null && requestData.size() > 0) {
                for (int i = 0; i < requestData.size(); i++) {
                    boolean isPresent = false;
                    for (int j = 0; j < teamRequestArrayList.size(); j++) {
                        if (teamRequestArrayList.get(j).getId().equals(requestData.get(i))) {
                            isPresent = true;
                        }

                    }
                    if (!isPresent) {
                        teamRequestArrayList.add(requestData.get(i));
                    }
                }
                teamRequestAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTeamRequestAdapter(List<SelfRequest> teamRequestList, boolean val) {

        try {



            if (teamRequestArrayList != null && teamRequestArrayList.size() > 0) {
                noDataImageView.setVisibility(View.GONE);

            } else {
                teamRequestArrayList.clear();
                noDataImageView.setVisibility(View.VISIBLE);
            }
            teamRequestAdapter.setDateRange(start_date, end_date);
            teamRequestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateRequestAdapter() {
        try {
            selfRequestDataAdapter.setDateRange(start_date, end_date);
            if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                noDataImageView.setVisibility(View.GONE);
            } else {
                selfRequestArrayList.clear();
                noDataImageView.setVisibility(View.VISIBLE);

            }
            selfRequestDataAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getRequiredDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return sdf2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getSelfRequiredDate(String date) throws ParseException {

        try {

            Date d = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            String dt = sdf3.format(c.getTime());
            filterDateLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.GONE);
            filterDate.setText(null);
            filterDate.setText(dt.substring(3, 6) + ", " + dt.substring(6));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void userApi() {

        try {

            if (Utilities.isNetworkAvailable(context)) {
                if (SystemClock.elapsedRealtime() - lastClicked < 100) {
                    return;
                }
                lastClicked = SystemClock.elapsedRealtime();
                callUserRequestListApi();

            } else {

                swipe_refresh.setRefreshing(false);
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void teamApi() {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                if (SystemClock.elapsedRealtime() - lastClicked < 100) {
                    return;
                }
                lastClicked = SystemClock.elapsedRealtime();
                callTeamRequestListApi();

            } else {
                // Utilities.ShowSnackbar(context,requestListRecycleView,getResources().getString(R.string.no_internet_connection));
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ccApi() {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                if (SystemClock.elapsedRealtime() - lastClicked < 100) {
                    return;
                }
                lastClicked = SystemClock.elapsedRealtime();

                callCcRequestListApi();

            } else {
                // Utilities.ShowSnackbar(context,requestListRecycleView,getResources().getString(R.string.no_internet_connection));
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allApi() {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                if (SystemClock.elapsedRealtime() - lastClicked < 100) {
                    return;
                }
                lastClicked = SystemClock.elapsedRealtime();
                callAllRequestListApi();

            } else {
                //Utilities.ShowSnackbar(context,requestListRecycleView,getResources().getString(R.string.no_internet_connection));
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class MonthYearDialog extends DialogFragment {
        private static final int MAX_YEAR = 2099;
        AttendanceActivity mainActivity;
        private DatePickerDialog.OnDateSetListener listener;

        public MonthYearDialog() {

        }

        /*public void setListener(FragmentActivity listener) {
            this.listener = listener;
        }*/

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);


            final int year = 2016;
            final int current_year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(current_year);
            yearPicker.setValue(current_year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            try {
                                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                                dialog_year = yearPicker.getValue();
                                dialog_month = monthPicker.getValue();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Calendar calendar = Calendar.getInstance();
                            calendar.set(dialog_year, dialog_month - 1, 01);

                            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


                            if (dialog_month < 10) {
                                String mon = 0 + "" + dialog_month;
                                start_date = "" + String.valueOf(dialog_year) + "-" + mon + "-" + "01";

                            } else {
                                start_date = "" + String.valueOf(dialog_year) + "-" + String.valueOf(dialog_month) + "-" + "01";

                            }
                            if (dialog_month < 10) {
                                String mon = 0 + "" + dialog_month;
                                end_date = "" + String.valueOf(dialog_year) + "-" + mon + "-" + days;

                            } else {
                                end_date = "" + String.valueOf(dialog_year) + "-" + String.valueOf(dialog_month) + "-" + days;
                            }
                            if (handler != null) {
                                try {
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("month_position", dialog_month - 1);
                                        object.put("year", dialog_year);
                                        object.put("month", dialog_month);
                                        object.put("current_year", current_year);
                                        object.put("start_date", start_date);
                                        object.put("end_date", end_date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    handler.obtainMessage(Constants.Static.CHECK_IN, object).sendToTarget();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            MonthYearDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }
    }
}


