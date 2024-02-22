package com.tvisha.trooptime.activity.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.adapter.BreaksAdapter;
import com.tvisha.trooptime.activity.activity.adapter.EventsAdaper;
import com.tvisha.trooptime.activity.activity.adapter.HolidaysAdapter;
import com.tvisha.trooptime.activity.activity.adapter.SelfRequestDataAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Break;
import com.tvisha.trooptime.activity.activity.apiPostModels.Breaks_list;
import com.tvisha.trooptime.activity.activity.apiPostModels.CheckUpdateResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.HomePageResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfRequest;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpcomingEvent;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpcomingHolidy;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    public static Handler ha = null;
    RelativeLayout selfAttendanceLayout, teamAttendanceLayout, selfRequestLayout, teamRequestLayout;
    ApiInterface apiService;
    TextView checkinTime, totalHours, totalMinutes, checkInStatus, totalBreakTime, mins, hrs, totalSalesTime, totalTransitTime, totalOthersTime,
            breaksTitle, requestsTitle, upcomingEventsTitle, upcomingHolidaysTitle;
    TextView moreRequests, isHolidayStatus;
    String loginTime = "", currentTime = "", logoutTime = "";
    LinearLayout breaksLayout, requestsLayout, totalBreakTimeLayout, teamLayout, salesLayout, transitLayout, othersLayout;
    View selfLayoutLine;
    int breaktime = 0, salesTime = 0, transitTime, othersTime = 0;
    SwipeRefreshLayout swipe_refresh;
    RecyclerView.LayoutManager breaksLayoutManager;
    BreaksAdapter breaksAdapter, salesAdapter, transitAdapter, othersAdapter;
    List<Break> breaksLists = new ArrayList<>();
    List<Break> salesList = new ArrayList<>();
    List<Break> transitList = new ArrayList<>();
    List<Break> othersList = new ArrayList<>();
    List<Break> tempBreaksLists = new ArrayList<>();
    List<Break> tempSalesList = new ArrayList<>();
    List<Break> tempTransitList = new ArrayList<>();
    List<Break> tempOthersList = new ArrayList<>();
    RecyclerView.LayoutManager eventsLayoutManager;
    EventsAdaper eventsAdapter;
    List<UpcomingEvent> upcomingEvents = new ArrayList<>();
    RecyclerView.LayoutManager todayEventsLayoutManager;
    EventsAdaper todayEventsAdapter;
    List<UpcomingEvent> todayEvents = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    SelfRequestDataAdapter selfRequestDataAdapter;
    List<SelfRequest> selfRequestArrayList = new ArrayList<>();
    List<SelfRequest> tempSelfRequestArrayList = new ArrayList<>();
    RecyclerView.LayoutManager holidaysLayoutManager;
    HolidaysAdapter holidaysAdapter;
    List<UpcomingHolidy> upcomingHolidies = new ArrayList<>();
    RecyclerView.LayoutManager todayHolidaysLayoutManager;
    HolidaysAdapter todayHolidaysAdapter;
    List<UpcomingHolidy> todayHolidies = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userId, email, apiKey;
    boolean teamLead, isRefresh = false, isFirstTime = true;
    LinearLayout checkinLayout, todayLayout, holidaysLayout, eventsLayout, todayEventsLayout, todayHolidaysLayout;
    String dateFormat = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    View line;
    CustomProgressBar customProgressBar;
    View rootView;
    private Context context;
    private Activity activity;
    private RecyclerView breaksRecycleView, sales_recycler_view, transit_recycler_view, others_recycler_view;
    private RecyclerView eventsRecycleView;
    private RecyclerView todayEventsRecyclerView;
    private RecyclerView requestListRecycleView;
    private RecyclerView holidaysRecycleView;
    private RecyclerView todayHolidaysRecyclerView;

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
    public void onPause() {
        if (ha != null) {
            ha.removeCallbacksAndMessages(null);
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        isRefresh = true;
        callHomePageApi();
        super.onResume();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = ((HomeActivity) getActivity()).getContext();
        activity = ((HomeActivity) getActivity()).getActivity();


        sharedPreferences = activity.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
            shareprefernceData();
        }
        NestedScrollView nestedScrollView = rootView.findViewById(R.id.nestedScroll);
        nestedScrollView.setNestedScrollingEnabled(false);


        initViews();
        initListeners();
        checkVersion();

     /*   if(MyApplication.homePageResponse!=null)
        {
            callHomePageApi();

          *//*  breaktime=0;
            salesTime=0;
            othersTime=0;
            transitTime=0;
            updateViews(MyApplication.homePageResponse);*//*
        }
        else
        {
            callHomePageApi();

        }*/


        return rootView;

    }

    private void initListeners() {
        selfRequestLayout.setOnClickListener(this);
        selfAttendanceLayout.setOnClickListener(this);
        teamRequestLayout.setOnClickListener(this);
        teamAttendanceLayout.setOnClickListener(this);
        moreRequests.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.selfRequestLayout:
                callSelfRequestFragment();
                break;
            case R.id.selfAttendanceLayout:
                callSelfAttendanceFragment();
                break;
            case R.id.teamRequestLayout:
                teamRequestFragment();
                break;
            case R.id.teamAttendanceLayout:
                teamAttendanceFragment();
                break;
            case R.id.moreRequests:
                moreRequests();
                break;

        }

    }

    private void callHomePageApi() {
        breaktime = 0;
        salesTime = 0;
        othersTime = 0;
        transitTime = 0;

        if (Utilities.isNetworkAvailable(context)) {

            callHomeDetails();

        } else {
            swipe_refresh.setRefreshing(false);
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

        }
    }

    private void checkVersion() {
        PackageInfo pInfo = null;
        String version = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
            if (Utilities.isNetworkAvailable(context)) {

                checkVersionApi(version);

            } else {
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                //  Utilities.ShowSnackbar(context,eventsRecycleView,getResources().getString(R.string.no_internet_connection));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void moreRequests() {
        Intent intent = new Intent(context, RequestActivity.class);
        intent.putExtra("type", "1");
        intent.putExtra("requestType", "");
        intent.putExtra("fromDate", "");
        intent.putExtra("toDate", "");
        intent.putExtra("users", "");
        startActivity(intent);

    }

    private void teamAttendanceFragment() {
        updateStatus();
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra("type", "4");
        intent.putExtra("filterType", false);
        startActivity(intent);


    }

    private void callSelfAttendanceFragment() {
        updateStatus();
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra("type", "2");
        intent.putExtra("filterType", false);
        startActivity(intent);

    }

    private void callSelfRequestFragment() {
        updateStatus();
        Intent intent = new Intent(context, RequestActivity.class);
        intent.putExtra("type", "1");
        intent.putExtra("requestType", "");
        intent.putExtra("fromDate", "");
        intent.putExtra("toDate", "");
        intent.putExtra("users", "");
        intent.putExtra("filter", false);
        startActivity(intent);


    }

    private void teamRequestFragment() {
        updateStatus();
        Intent intent = new Intent(context, RequestActivity.class);
        intent.putExtra("type", "3");
        intent.putExtra("requestType", " ");
        intent.putExtra("fromDate", " ");
        intent.putExtra("toDate", " ");
        intent.putExtra("users", " ");
        intent.putExtra("filter", false);
        startActivity(intent);

    }

    private void initViews() {
        customProgressBar = new CustomProgressBar(activity);
        selfRequestLayout = rootView.findViewById(R.id.selfRequestLayout);
        totalBreakTimeLayout = rootView.findViewById(R.id.totalBreakTimeLayout);
        selfAttendanceLayout = rootView.findViewById(R.id.selfAttendanceLayout);
        teamAttendanceLayout = rootView.findViewById(R.id.teamAttendanceLayout);
        teamRequestLayout = rootView.findViewById(R.id.teamRequestLayout);
        apiService = new ApiClient().getInstance();
        checkinTime = rootView.findViewById(R.id.checkinTime);
        totalHours = rootView.findViewById(R.id.totalHours);
        isHolidayStatus = rootView.findViewById(R.id.isHolidayStatus);
        totalMinutes = rootView.findViewById(R.id.totalMinutes);
        checkInStatus = rootView.findViewById(R.id.checkinStatus);
        breaksLayout = rootView.findViewById(R.id.breaksLayout);
        salesLayout = rootView.findViewById(R.id.salesLayout);
        transitLayout = rootView.findViewById(R.id.transitLayout);
        othersLayout = rootView.findViewById(R.id.othersLayout);

        requestsLayout = rootView.findViewById(R.id.requestsLayout);
        moreRequests = rootView.findViewById(R.id.moreRequests);
        totalBreakTime = rootView.findViewById(R.id.totalBreakTime);
        totalSalesTime = rootView.findViewById(R.id.totalSalesTime);
        totalTransitTime = rootView.findViewById(R.id.totalTransitTime);
        totalOthersTime = rootView.findViewById(R.id.totalOthersTime);
        checkinLayout = rootView.findViewById(R.id.checkinLayout);
        breaksTitle = rootView.findViewById(R.id.breaks);
        requestsTitle = rootView.findViewById(R.id.requests);
        upcomingEventsTitle = rootView.findViewById(R.id.upcomingEvents);
        upcomingHolidaysTitle = rootView.findViewById(R.id.upcomingHolidays);
        holidaysLayout = rootView.findViewById(R.id.holidaysLayout);
        eventsLayout = rootView.findViewById(R.id.eventsLayout);
        todayHolidaysLayout = rootView.findViewById(R.id.todayHolidaysLayout);
        todayEventsLayout = rootView.findViewById(R.id.todayEventsLayout);


        breaksRecycleView = rootView.findViewById(R.id.breaks_recycler_view);
        sales_recycler_view = rootView.findViewById(R.id.sales_recycler_view);
        transit_recycler_view = rootView.findViewById(R.id.transit_recycler_view);
        others_recycler_view = rootView.findViewById(R.id.others_recycler_view);
        eventsRecycleView = rootView.findViewById(R.id.upcoming_events_recycler_view);
        requestListRecycleView = rootView.findViewById(R.id.requests_recycler_view);
        holidaysRecycleView = rootView.findViewById(R.id.upcoming_holidays_recycler_view);
        todayEventsRecyclerView = rootView.findViewById(R.id.today_events_recycler_view);
        todayHolidaysRecyclerView = rootView.findViewById(R.id.today_holidays_recycler_view);


        eventsRecycleView.setNestedScrollingEnabled(false);
        requestListRecycleView.setNestedScrollingEnabled(false);
        holidaysRecycleView.setNestedScrollingEnabled(false);
        breaksRecycleView.setNestedScrollingEnabled(false);
        todayEventsRecyclerView.setNestedScrollingEnabled(false);
        todayHolidaysRecyclerView.setNestedScrollingEnabled(false);


        mins = rootView.findViewById(R.id.mins);
        hrs = rootView.findViewById(R.id.hrs);
        todayLayout = rootView.findViewById(R.id.todayLayout);
        line = rootView.findViewById(R.id.line);
        selfLayoutLine = rootView.findViewById(R.id.selfLayoutLine);
        teamLayout = rootView.findViewById(R.id.teamLayout);

        breaksRecycleView.setHasFixedSize(true);
        breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        breaksRecycleView.setLayoutManager(breaksLayoutManager);
        breaksAdapter = new BreaksAdapter(context, breaksLists);
        breaksRecycleView.setAdapter(breaksAdapter);
        breaksRecycleView.setNestedScrollingEnabled(false);


        sales_recycler_view.setHasFixedSize(true);
        breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        sales_recycler_view.setLayoutManager(breaksLayoutManager);
        salesAdapter = new BreaksAdapter(context, salesList);
        sales_recycler_view.setAdapter(salesAdapter);
        sales_recycler_view.setNestedScrollingEnabled(false);


        transit_recycler_view.setHasFixedSize(true);
        breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        transit_recycler_view.setLayoutManager(breaksLayoutManager);
        transitAdapter = new BreaksAdapter(context, transitList);
        transit_recycler_view.setAdapter(transitAdapter);
        transit_recycler_view.setNestedScrollingEnabled(false);

        others_recycler_view.setHasFixedSize(true);
        breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        others_recycler_view.setLayoutManager(breaksLayoutManager);
        othersAdapter = new BreaksAdapter(context, othersList);
        others_recycler_view.setAdapter(othersAdapter);
        others_recycler_view.setNestedScrollingEnabled(false);


        eventsRecycleView.setHasFixedSize(true);
        eventsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        eventsRecycleView.setLayoutManager(eventsLayoutManager);
        eventsAdapter = new EventsAdaper(context, upcomingEvents);
        eventsRecycleView.setAdapter(eventsAdapter);
        eventsRecycleView.setNestedScrollingEnabled(false);

        todayEventsRecyclerView.setHasFixedSize(true);
        todayEventsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        todayEventsRecyclerView.setLayoutManager(todayEventsLayoutManager);
        todayEventsAdapter = new EventsAdaper(context, todayEvents);
        todayEventsRecyclerView.setAdapter(todayEventsAdapter);
        todayEventsRecyclerView.setNestedScrollingEnabled(false);


        requestListRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        requestListRecycleView.setLayoutManager(layoutManager);
        selfRequestDataAdapter = new SelfRequestDataAdapter(context, selfRequestArrayList, true, "", "");
        requestListRecycleView.setAdapter(selfRequestDataAdapter);
        requestListRecycleView.setNestedScrollingEnabled(false);


        holidaysRecycleView.setHasFixedSize(true);
        holidaysLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        holidaysRecycleView.setLayoutManager(holidaysLayoutManager);
        holidaysAdapter = new HolidaysAdapter(context, upcomingHolidies);
        holidaysRecycleView.setAdapter(holidaysAdapter);
        holidaysRecycleView.setNestedScrollingEnabled(false);

        todayHolidaysRecyclerView.setHasFixedSize(true);
        todayHolidaysLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        todayHolidaysRecyclerView.setLayoutManager(todayHolidaysLayoutManager);
        todayHolidaysAdapter = new HolidaysAdapter(context, todayHolidies);
        todayHolidaysRecyclerView.setAdapter(todayHolidaysAdapter);
        todayHolidaysRecyclerView.setNestedScrollingEnabled(false);


        breaksLayout.setVisibility(View.GONE);
        checkinLayout.setVisibility(View.GONE);
        todayLayout.setVisibility(View.GONE);
        line.setVisibility(View.GONE);

        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);


        if (!teamLead) {
            selfLayoutLine.setVisibility(View.GONE);
            teamLayout.setVisibility(View.GONE);
            teamAttendanceLayout.setVisibility(View.GONE);
            teamRequestLayout.setVisibility(View.GONE);

        }

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    swipe_refresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_green_light);
                    swipe_refresh.setRefreshing(true);
                    isRefresh = true;
                    callHomePageApi();

                } else {
                    swipe_refresh.setRefreshing(false);
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateStatus() {
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CLICKED, false).apply();

    }


    private void shareprefernceData() {
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
        apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);

    }


    private void callHomeDetails() {

        if (!isRefresh || isFirstTime) {
            isFirstTime = false;
            openProgress();
        } else {
            isRefresh = false;
        }


        Call<HomePageResponse> call = apiService.getHomeDetails(apiKey, userId);
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                HomePageResponse apiResponse = response.body();
                swipe_refresh.setRefreshing(false);
                if (apiResponse != null) {
                    closeProgress();

                    if (apiResponse.isSuccess()) {
                        updateViews(apiResponse);
                    } else {
                        Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        // Utilities.ShowSnackbar(context,eventsRecycleView,apiResponse.getMessage());
                    }

                } else {
                    closeProgress();
                    Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    //  Utilities.ShowSnackbar(context,eventsRecycleView,getResources().getString(R.string.somthing_went_wrong));
                }

            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                closeProgress();
                swipe_refresh.setRefreshing(false);


            }

        });
    }

    private void updateViews(HomePageResponse apiResponse) {

        breaktime = 0;
        salesTime = 0;
        othersTime = 0;
        transitTime = 0;

        try {

            MyApplication.homePageResponse = apiResponse;


            if (apiResponse.getToday() != null) {
                //checkin
                if (apiResponse.getToday().getCheck_in() != null && !apiResponse.getToday().getCheck_in().equals("0000-00-00 00:00:00")) {



                    checkinLayout.setVisibility(View.VISIBLE);
                    todayLayout.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);

                    String checkIn = apiResponse.getToday().getCheck_in();
                    String checkOut = "";
                    String time1 = "";
                    if (!apiResponse.getToday().getCheck_out().equals("0000-00-00 00:00:00") && !apiResponse.getToday().getCheck_out().isEmpty()) {
                        checkOut = apiResponse.getToday().getCheck_out();
                        time1 = checkOut.substring(11);
                    }
                    String time = checkIn.substring(11);


                    Log.e("check in", checkIn + "  ==" + apiResponse.getToday().getShift_start_time());

                    loginTime = time;
                    logoutTime = time1;


                    int t = Integer.parseInt(checkIn.substring(11, 13));
                    if (t > 12) {
                        t = t - 12;
                        if (t < 10) {
                            checkinTime.setText("0" + t + checkIn.substring(13, 16) + " PM");
                        } else {
                            checkinTime.setText(t + checkIn.substring(13, 16) + " PM");
                        }

                    } else {
                        checkinTime.setText(checkIn.substring(11, 16) + " AM");
                    }

                    if (apiResponse.getToday().getShift_start_time() != null && !apiResponse.getToday().getShift_start_time().equals("00:00:00") && !apiResponse.getToday().getShift_start_time().isEmpty()) {
                        updateCheckInStatus(apiResponse.getToday().getShift_start_time());
                    }


                    int breakTime = apiResponse.getToday().getBreak_time();

                    if (breakTime == 0) {
                        totalBreakTimeLayout.setVisibility(View.GONE);
                    } else {
                        totalBreakTimeLayout.setVisibility(View.VISIBLE);

                    }

                    //breaks
                    if (apiResponse.getToday().getBreaks_list() != null) {

                        Breaks_list breaks_list = apiResponse.getToday().getBreaks_list();

                        if (breaks_list.getBreaks() != null) {
                            if (breaksLists != null && breaksLists.size() > 0) {
                                breaksLists.clear();
                            }


                            tempBreaksLists = breaks_list.getBreaks();


                            for (int i = 0; i < tempBreaksLists.size(); i++) {

                                boolean status = timeDifferenceOfBreaks(tempBreaksLists.get(i).getStart_time(), tempBreaksLists.get(i).getEnd_time());

                                if (status) {
                                    breaksLists.add(tempBreaksLists.get(i));

                                }


                            }
                        }
                        if (breaks_list.getSales() != null && breaks_list.getSales().size() > 0) {
                            if (salesList != null && salesList.size() > 0) {
                                salesList.clear();
                            }
                            tempSalesList = breaks_list.getSales();



                            for (int i = 0; i < tempSalesList.size(); i++) {

                                boolean status = timeDifferenceOfBreaks(tempSalesList.get(i).getStart_time(), tempSalesList.get(i).getEnd_time());
                                if (status) {
                                    salesList.add(tempSalesList.get(i));
                                }
                            }
                        }
                        if (breaks_list.getTransit() != null && breaks_list.getTransit().size() > 0) {

                            if (transitList != null && transitList.size() > 0) {
                                transitList.clear();
                            }
                            tempTransitList = breaks_list.getTransit();


                            for (int i = 0; i < tempTransitList.size(); i++) {

                                boolean status = timeDifferenceOfBreaks(tempTransitList.get(i).getStart_time(), tempTransitList.get(i).getEnd_time());
                                if (status) {
                                    transitList.add(tempTransitList.get(i));
                                }
                            }
                        }
                        if (breaks_list.getOthers() != null && breaks_list.getOthers().size() > 0) {
                            if (othersList != null && othersList.size() > 0) {
                                othersList.clear();
                            }
                            tempOthersList = breaks_list.getOthers();
                            for (int i = 0; i < tempOthersList.size(); i++) {

                                boolean status = timeDifferenceOfBreaks(tempOthersList.get(i).getStart_time(), tempOthersList.get(i).getEnd_time());
                                if (status) {
                                    othersList.add(tempOthersList.get(i));
                                }
                            }

                        }


                    }


                    if (breaksLists != null && breaksLists.size() > 0) {
                        breaksLayout.setVisibility(View.VISIBLE);
                        Collections.reverse(breaksLists);
                        updateBreaksAdapter();
                        for (int i = 0; i < breaksLists.size(); i++) {
                            timeDifference(breaksLists.get(i).getStart_time(), breaksLists.get(i).getEnd_time(), Constants.Breaks.breaks);
                        }
                        totalBreakTime.setVisibility(View.VISIBLE);
                        totalBreakTime.setText(breaktime + "");

                        if (breaktime <= 60) {
                            totalBreakTime.setText(breaktime + "");
                        } else {
                            int temp, hrs = 0, mins = 0;
                            temp = breaktime;
                            hrs = breaktime / 60;
                            mins = breaktime % 60;
                            totalBreakTime.setText(hrs + " hr " + mins);
                        }
                    } else {
                        breaksLayout.setVisibility(View.GONE);
                        totalBreakTime.setVisibility(View.GONE);
                    }


                    if (salesList != null && salesList.size() > 0) {
                        salesLayout.setVisibility(View.VISIBLE);
                        Collections.reverse(salesList);
                        salesAdapter.notifyDataSetChanged();
                        for (int i = 0; i < salesList.size(); i++) {
                            timeDifference(salesList.get(i).getStart_time(), salesList.get(i).getEnd_time(), Constants.Breaks.sales);
                        }


                        if (salesTime <= 60) {
                            totalSalesTime.setText(salesTime + "");
                        } else {
                            int temp, hrs = 0, mins = 0;
                            temp = salesTime;
                            hrs = salesTime / 60;
                            mins = salesTime % 60;
                            totalSalesTime.setText(hrs + " hr " + mins);
                        }


                    } else {
                        salesLayout.setVisibility(View.GONE);

                    }

                    if (transitList != null && transitList.size() > 0) {
                        transitLayout.setVisibility(View.VISIBLE);
                        Collections.reverse(transitList);

                        transitAdapter.notifyDataSetChanged();
                        for (int i = 0; i < transitList.size(); i++) {
                            timeDifference(transitList.get(i).getStart_time(), transitList.get(i).getEnd_time(), Constants.Breaks.transit);
                        }

                        totalTransitTime.setText(transitTime + "");

                        if (transitTime <= 60) {
                            totalTransitTime.setText(transitTime + "");
                        } else {
                            int temp, hrs = 0, mins = 0;
                            temp = transitTime;
                            hrs = transitTime / 60;
                            mins = transitTime % 60;
                            totalTransitTime.setText(hrs + " hr " + mins);
                        }


                    } else {
                        transitLayout.setVisibility(View.GONE);

                    }

                    if (othersList != null && othersList.size() > 0) {
                        othersLayout.setVisibility(View.VISIBLE);
                        Collections.reverse(othersList);
                        othersAdapter.notifyDataSetChanged();
                        for (int i = 0; i < othersList.size(); i++) {
                            timeDifference(othersList.get(i).getStart_time(), othersList.get(i).getEnd_time(), Constants.Breaks.others);
                        }
                        totalOthersTime.setText(othersTime + "");
                        if (othersTime <= 60) {
                            totalOthersTime.setText(othersTime + "");
                        } else {
                            int temp, hrs = 0, mins = 0;
                            temp = othersTime;
                            hrs = othersTime / 60;
                            mins = othersTime % 60;
                            totalOthersTime.setText(hrs + " hr " + mins);
                        }
                    } else {
                        othersLayout.setVisibility(View.GONE);

                    }

                    findWorkingTime();
                } else {


                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.IS_CHECKED, true).apply();

                    breaksLayout.setVisibility(View.GONE);
                    checkinLayout.setVisibility(View.VISIBLE);
                    todayLayout.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);

                    updateHolidayStatus(apiResponse);


                }

            } else {

                breaksLayout.setVisibility(View.GONE);
                checkinLayout.setVisibility(View.VISIBLE);
                todayLayout.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);


            }


            if (apiResponse.getTodayEvents() != null && apiResponse.getTodayEvents().size() > 0) {
                todayEvents = apiResponse.getTodayEvents();
            }
            //events
            if (apiResponse.getUpcomingEvents() != null && apiResponse.getUpcomingEvents().size() > 0) {
                upcomingEvents = apiResponse.getUpcomingEvents();
            }

            if (todayEvents != null && todayEvents.size() > 0) {
                for (int i = 0; i < todayEvents.size(); i++) {
                    upcomingEvents.add(todayEvents.get(i));
                }
            }


            if (upcomingEvents != null && upcomingEvents.size() > 0) {
                Collections.sort(upcomingEvents, new Comparator<UpcomingEvent>() {
                    public int compare(UpcomingEvent o1, UpcomingEvent o2) {
                        try {

                            return getDate(o1.getTmp_date()).compareTo(getDate(o2.getTmp_date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                // Collections.reverse(upcomingEvents);


                eventsLayout.setVisibility(View.VISIBLE);
                updateEventsAdapter();
                //holidays

            }


           /* if(todayEvents!=null && todayEvents.size()>0)
            {

                todayEventsLayout.setVisibility(View.VISIBLE);
                if (todayEventsAdapter!=null){
                    todayEventsAdapter.setList(todayEvents);
                }
                todayEventsAdapter.notifyDataSetChanged();
            }*/

            if (apiResponse.getTodayHolidays() != null && apiResponse.getTodayHolidays().size() > 0) {
                todayHolidies = apiResponse.getTodayHolidays();

            }


            if (apiResponse.getUpcomingHolidys() != null && apiResponse.getUpcomingHolidys().size() > 0) {
                upcomingHolidies = apiResponse.getUpcomingHolidys();

            }

            if (todayHolidies != null && todayHolidies.size() > 0) {
                for (int i = 0; i < todayHolidies.size(); i++) {
                    upcomingHolidies.add(todayHolidies.get(i));
                }
            }


            if (upcomingHolidies != null && upcomingHolidies.size() > 0) {
                //Collections.reverse(upcomingHolidies);
                holidaysLayout.setVisibility(View.VISIBLE);
                updateHolidaysAdapter();
            }







        /*    if(todayHolidies!=null && todayHolidies.size()>0)
            {
                todayHolidaysLayout.setVisibility(View.VISIBLE);
                if (todayHolidaysAdapter!=null){
                    todayHolidaysAdapter.setList(todayHolidies);
                }
                todayHolidaysAdapter.notifyDataSetChanged();
            }
*/


            //requests
            if (apiResponse.getRequest_self() != null && apiResponse.getRequest_self().size() > 0) {
                if (tempSelfRequestArrayList != null && tempSelfRequestArrayList.size() > 0) {
                    tempSelfRequestArrayList.clear();
                }

                if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                    selfRequestArrayList.clear();
                }


                selfRequestArrayList.addAll(apiResponse.getRequest_self());



            }

            if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                requestsLayout.setVisibility(View.VISIBLE);
                updateRequestAdapter();
            } else {
                requestsLayout.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateHolidayStatus(HomePageResponse apiResponse) {
        if (apiResponse.getToday().getIs_holiday() != null && !apiResponse.getToday().getIs_holiday().isEmpty()) {
            String isHoliday = apiResponse.getToday().getIs_holiday();

            isHolidayStatus.setVisibility(View.VISIBLE);
            checkinLayout.setVisibility(View.GONE);
            todayLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);

            if (isHoliday.equals(Constants.HOLIDAY_TYPE_NONE)) {
                //isHolidayStatus.setText("ABSENT");
                checkinLayout.setVisibility(View.VISIBLE);
                todayLayout.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                isHolidayStatus.setText("");
            } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_WEEKOFF)) {

                isHolidayStatus.setText("WEEK OFF");
            } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_CONFIRM)) {

                isHolidayStatus.setText("HOLIDAY");
            } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_OPTIONAL)) {

                isHolidayStatus.setText("OPTIONAL HOLIDAY");
            } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_LEAVE)) {

                isHolidayStatus.setText("LEAVE");
            } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_COMPOFF)) {

                isHolidayStatus.setText("COMP OFF");
            }


        }
    }

    private void updateCheckInStatus(String shifTime) {
        double shftTime = 0, logintime = 0;
        shftTime = getSecs(shifTime);
        logintime = getSecs(loginTime);
        if (logintime > shftTime) {
            double secs = logintime - shftTime;
            if (secs > 59) {
                double mins = 0;
                double hours = 0;
                double totalmins = secs / 60;
                hours = totalmins / 60;
                mins = totalmins % 60;

                checkInStatus.setVisibility(View.VISIBLE);
                if (!String.valueOf((int) hours).equals("0")) {
                    if (String.valueOf((int) hours).equals("1")) {
                        checkInStatus.setText("Late Login ( " + String.valueOf((int) hours) + " hr " + String.valueOf((int) mins) + " mins )");
                    } else {
                        checkInStatus.setText("Late Login ( " + String.valueOf((int) hours) + " hrs " + String.valueOf((int) mins) + " mins )");
                    }

                } else {
                    checkInStatus.setText("Late Login ( " + String.valueOf((int) mins) + " mins )");
                }

                // totalMinutes.setText(String.valueOf((int)mins));

            }
        } else {
            checkInStatus.setVisibility(View.INVISIBLE);
        }
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


    }

    private void findWorkingTime() {

        Date currentTime = Calendar.getInstance().getTime();
        String curTime = (String.valueOf(currentTime)).substring(11, 19);
        double presebtTime = 0, logintime = 0;

        if (logoutTime.equals("")) {
            presebtTime = getSecs(curTime);
        } else {
            presebtTime = getSecs(logoutTime);
        }
        logintime = getSecs(loginTime);
        double secs = presebtTime - logintime;
        if (secs > 0) {
            double mins = 0;
            double hours = 0;
            double totalmins = secs / 60;
            hours = totalmins / 60;
            mins = totalmins % 60;
            totalHours.setText(String.valueOf((int) hours));
            totalMinutes.setText(String.valueOf((int) mins));
        }

        if (logoutTime != null && logoutTime.equals("")) {
            updateWotkingHours();

        }


    }

    private void updateWotkingHours() {
        ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                Date currentTime = Calendar.getInstance().getTime();
                String curTime = (String.valueOf(currentTime)).substring(11, 19);
                double presebtTime = 0, logintime = 0;
                presebtTime = getSecs(curTime);
                logintime = getSecs(loginTime);
                double secs = presebtTime - logintime;
                if (secs > 0) {
                    double mins = 0;
                    double hours = 0;
                    double totalmins = secs / 60;
                    hours = totalmins / 60;
                    mins = totalmins % 60;
                    if (totalHours != null) {
                        totalHours.setText(String.valueOf((int) hours));
                    }
                    if (totalMinutes != null) {
                        totalMinutes.setText(String.valueOf((int) mins));
                    }
                }
                ha.postDelayed(this, 10000);
            }
        }, 10000);
    }

    private double getSecs(String time) {
        double total = 0;
        if (time != null && time.length() > 0) {

            String hours, mins, secs;
            hours = time.substring(0, 2);
            mins = time.substring(3, 5);
            secs = time.substring(6);

            total = total + Double.parseDouble(hours) * 60 * 60;
            total = total + Double.parseDouble(mins) * 60;
            total = total + Double.parseDouble(secs);
        }
        return total;
    }

    private void updateBreaksAdapter() {
        if (breaksLists != null && breaksLists.size() > 0) {
               /* if (breaksAdapter!=null){
                    breaksAdapter.setList(breaksLists);
                }*/
            breaksAdapter.notifyDataSetChanged();
        }

    }

    private void updateEventsAdapter() {
        if (upcomingEvents != null && upcomingEvents.size() > 0) {

            if (eventsAdapter != null) {
                eventsAdapter.setList(upcomingEvents);
            }
            eventsAdapter.notifyDataSetChanged();
        }


    }

    private void updateHolidaysAdapter() {
        if (upcomingHolidies != null && upcomingHolidies.size() > 0) {

            if (holidaysAdapter != null) {
                holidaysAdapter.setList(upcomingHolidies);
            }
            holidaysAdapter.notifyDataSetChanged();
        }


    }


    private void updateRequestAdapter() {
        if (selfRequestDataAdapter != null) {
            //  selfRequestDataAdapter.setList(selfRequestArrayList);
            selfRequestDataAdapter.notifyDataSetChanged();
        }


    }

    private Date getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("MM-dd", Locale.US).parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        // c.add(Calendar.DATE, 1);
        Date nextDate = c.getTime();
        /* return sdf.format(c.getTime());*/
        return c.getTime();
    }


    public void timeDifference(String startTime, String endTime, int type) {

        Date startDate, endDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            startDate = simpleDateFormat.parse(startTime);
            endDate = simpleDateFormat.parse(endTime);
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();


            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;


            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;


            switch (type) {
                case Constants.Breaks.breaks:
                    if ((int) elapsedSeconds > 30) {
                        breaktime = breaktime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                    } else {
                        breaktime = breaktime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                    }
                    break;
                case Constants.Breaks.sales:
                    if ((int) elapsedSeconds > 30) {
                        salesTime = salesTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                    } else {
                        salesTime = salesTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                    }
                    break;
                case Constants.Breaks.transit:
                    if ((int) elapsedSeconds > 30) {
                        transitTime = transitTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                    } else {
                        transitTime = transitTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                    }
                    break;
                case Constants.Breaks.others:
                    if ((int) elapsedSeconds > 30) {
                        othersTime = othersTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                    } else {
                        othersTime = othersTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                    }
                    break;

            }


        } catch (ParseException e) {
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


    private void checkVersionApi(String version) {


        Call<CheckUpdateResponse> call = apiService.checkUpdateAvailable(apiKey, userId, "1", version);
        call.enqueue(new Callback<CheckUpdateResponse>() {
            @Override
            public void onResponse(Call<CheckUpdateResponse> call, Response<CheckUpdateResponse> response) {
                CheckUpdateResponse apiResponse = response.body();

                if (apiResponse != null) {

                    if (apiResponse.isSuccess()) {


                        if (apiResponse.isUpdate()) {
                            Navigation.getInstance().openUpdatePage(context);
                            activity.finish();
                           /*
                            Intent intent=new Intent(context,UpdateActivity.class);
                            startActivity(intent);*/

                        }


                    } else {
                        if (apiResponse.isUpdate()) {
                            Navigation.getInstance().openUpdatePage(context);
                            activity.finish();

                        }
                        // Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<CheckUpdateResponse> call, Throwable t) {


            }

        });
    }


    public boolean timeDifferenceOfBreaks(String startTime, String endTime) {

        boolean status = false;

        Date startDate, endDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            startDate = simpleDateFormat.parse(startTime);
            endDate = simpleDateFormat.parse(endTime);
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;




           /* long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;*/


            long seconds = 0, mins = 0;

            seconds = different / secondsInMilli;
            mins = different / minutesInMilli;


            if (mins > 0 || (mins == 0 && seconds > 30)) {
                status = true;
                return true;
            } else {
                status = false;
                return false;
            }


        } catch (ParseException e) {
            e.printStackTrace();


        }

        return status;
    }


}