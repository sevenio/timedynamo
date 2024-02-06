package com.tvisha.trooptime.activity.activity.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.tvisha.trooptime.activity.activity.Adapter.TroopTimeAdapter;

import com.tvisha.trooptime.activity.activity.ApiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.ApiPostModels.FilterAllAttendanceApi;
import com.tvisha.trooptime.activity.activity.ApiPostModels.FilterAttendanceApi;
import com.tvisha.trooptime.activity.activity.ApiPostModels.SelfAttendenceApi;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.Model.CalenderModel;

import com.tvisha.trooptime.activity.activity.AttendanceActivity;
import com.tvisha.trooptime.R;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tvisha on 16/2/17.
 */
public  class FilterFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    View view;
    ImageView back_button,filter_image_filter,filter_tv_reset,close_filter_worktime,close_filter_checkout,close_filter_checkin,close_filter_name;
    TextView filter_text,text_nodata,filter_name_of_month,filter_previous_month,filter_next_month,
            filter_month_name,filter_text_team,filter_text_self,filter_text_all,name_of_employee,employee_check_in,notification_count,
            employee_checkout,work_time_of_employee;
    LinearLayout employee_check_in_rel,employee_checkout_rel;
    RecyclerView filter_recycle_view,filter_month_caledar;
    SwitchCompat filter_button_switch;
    SwipeRefreshLayout swipe_refresh;
    HorizontalScrollView filter_add_layout;
    RelativeLayout notification,search_view;
    ImageView closearch;
    EditText et_search;
    Bundle bundle;

    String emp_userId="";
    int tab_selected_position = 2;
    List<Attendance> attendence_arrayList =new ArrayList<>();
    List<CalenderModel> calenderModels;
    int selected_position,month_position,select_year,month,year,current_month,current_year,current_day;
    String userId,start_mon,end_dat,selected_date,success,emp_id,check_in,check_out,
            work_time,employee_name,check_In="",check_Out="",get_date,bundle_date,month_name,
            start_date,end_date,select_day;
    public static String clciked_date="";
    LinearLayoutManager manager;
    GridCellAdapter adapter;
    Calendar startDate,endDate,_calendar;
    boolean isNetAvailable = false,teamtext_selected=true;
    Attendance model;
    int count=0,notification_num,attendance_filter=0,filte_working_time=0,filter_type=2;
    DatePickerDialog pic_dialog;
    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    TroopTimeAdapter timeAdapter;
    boolean self=false,team=false,admin=false;
    String fromDate="",toDate="";
    String dateFormat2 = "dd MMM yy";
    SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2, Locale.US);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.filter_list_layout,container,false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        shareprefernceData();
        bundle = getArguments();

        if (bundle!=null) {
            getBundleData();
        }
        initializeWidgets();
        eventHandlings();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();

                Intent intent=new Intent(getActivity(),AttendanceActivity.class);
                if(admin||team)
                {
                    intent.putExtra("type","2");
                    intent.putExtra("filterType",false);
                }
                else
                {
                    intent.putExtra("type","1");
                    intent.putExtra("filterType",false);
                }

                startActivity(intent);

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filter_text.setText(Html.fromHtml("<font color='#ffffff'><b>TROOP</b></font><font color='#f06b7c'><b> TIME</b></font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            filter_text.setText(Html.fromHtml("<font color='#ffffff'><b>TROOP</b></font><font color='#f06b7c'><b> TIME</b></font>"));
        }
        addToTextview();
        //setTheCalendateAndPosition();

         if (attendence_arrayList==null || attendence_arrayList.size()==0) {
             filter_recycle_view.setVisibility(View.GONE);
             text_nodata.setVisibility(View.VISIBLE);
         }else {
             text_nodata.setVisibility(View.GONE);
             filter_recycle_view.setVisibility(View.VISIBLE);
             /*if (timeAdapter!=null){

                 timeAdapter.setarrayList(attendence_arrayList);
                 timeAdapter.setAdapterType("team");
                 timeAdapter.notifyDataSetChanged();
             }*/
         }
        return view;
    }

    private void setTheCalendateAndPosition() {
        try {
            String[] splitting =  selected_date.split("-");
            String select_years = splitting[0];
            String select_mon  = splitting[1];
            String select_day  = splitting[2];
            Calendar calendar = Calendar.getInstance();
            String days        = new SimpleDateFormat("dd").format(calendar.getTime());
            selected_position = Integer.parseInt(select_day)-1;
            current_day       = Integer.parseInt(days);
            String months      = new SimpleDateFormat("MM").format(calendar.getTime());
            month_name = new SimpleDateFormat("MMM").format(calendar.getTime());
            current_month  = Integer.parseInt(months);
            month_position    = Integer.parseInt(select_mon)-1;
            String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
            select_year              = Integer.parseInt(select_years);
            this.current_year = Integer.parseInt(current_year);
            //selfisOnedCalenderView(year);
            getTheCalenderDayView();
            if (bundle_date!=null && !bundle_date.isEmpty())
            {
                String[] strings = bundle_date.split("-");
                String mon       = strings[1];
                month            = Integer.parseInt(mon);
                if (month < current_month || select_year < this.current_year) {
                    filter_next_month.setClickable(true);
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.arrow_image);
                    filter_next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    filter_next_month.setTextColor(ContextCompat.getColor(getActivity(), R.color.next_bt_color));
                } else {
                    filter_next_month.setClickable(false);
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.light_col_arrow_imag);
                    filter_next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    filter_next_month.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_team));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addToTextview() {
        if (employee_name!=null && !employee_name.isEmpty())
        {
            String[] split = employee_name.split(",");
            int last = employee_name.toString().trim().length() - 1;
            String name    = employee_name.substring(0,last);
            name_of_employee.setText(name);
        }else {
            name_of_employee.setText("All");
        }
        if (attendance_filter==Constants.ATTENDENCE_FILTER_NONE) {
            employee_check_in_rel.setVisibility(View.GONE);
            employee_checkout_rel.setVisibility(View.GONE);
        }else if (attendance_filter==Constants.ATTENDENCE_FILTER_LATE_LOGIN){
            employee_check_in_rel.setVisibility(View.VISIBLE);
            employee_check_in.setText(getResources().getString(R.string.late_login));
            employee_checkout_rel.setVisibility(View.GONE);
        }else if (attendance_filter==Constants.ATTENDENCE_FILTER_EARLY_LOGOUT){
            employee_checkout_rel.setVisibility(View.VISIBLE);
            employee_checkout.setText(getResources().getString(R.string.early_logouts));
            employee_check_in_rel.setVisibility(View.GONE);
        }
        if (work_time.trim().toLowerCase().equals("any")) {
            work_time_of_employee.setText("Any");
        } else {
            work_time_of_employee.setText(work_time);
        }
    }
    private void shareprefernceData() {
        sharedPreferences = getActivity().getSharedPreferences(SharePreferenceKeys.SP_NAME, getActivity().MODE_PRIVATE);
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT,0);
        self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
        team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
        admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);


    }


  /*  private void shareprefernceData1() {
        SharedPreferences shared = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);

        String last_name = (shared.getString("last_name", ""));
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
        apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
        self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
        team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
        admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);

    }*/
    /*private void selfisOnedCalenderView(int year)
    {
        filter_month_name.setText(" "+ year +" ");
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);


        String[] months = new DateFormatSymbols().getMonths();
        calenderModels = new ArrayList<>();
        int count=0;
        int month_no = 1;
        for (int i =0; i < months.length; i++)
        {
            String month = months[i];
            String month_name = month.substring(0,3);
            Calendar calendar = Calendar.getInstance();
            calendar.set(this.year,count,1);
            int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String start_mon = ""+ this.year +"-"+month_no+"-01";
            String end_mon   = ""+ this.year +"-"+month_no+"-"+numDays;

            count++;
            month_no++;
            CalenderModel model = new CalenderModel();
            model.setMonthStartDate(start_mon);
            model.setMonthEndDate(end_mon);
            model.setMonth_Name(month_name);
            model.setYears(year);
            calenderModels.add(model);
        }
        adapter = new GridCellAdapter(getActivity(), month, this.year,"self");
        adapter.notifyDataSetChanged();
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        filter_month_caledar.setLayoutManager(manager);
        filter_month_caledar.setAdapter(adapter);
    }*/
    private void setGridCellAdapterToDate(int month, int year)
    {
        adapter = new GridCellAdapter(getActivity(), month, year, "team");
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        adapter.notifyDataSetChanged();
        filter_month_caledar.setAdapter(adapter);
    }

    private void eventHandlings() {

        filter_previous_month.setOnClickListener(this);
        filter_next_month.setOnClickListener(this);
        filter_tv_reset.setOnClickListener(this);
        filter_image_filter.setOnClickListener(this);
        //filter_button_switch.setOnCheckedChangeListener(FilterFragment.this);
        swipe_refresh.setOnRefreshListener(this);
        name_of_employee.setOnClickListener(this);
        employee_checkout.setOnClickListener(this);
        employee_check_in.setOnClickListener(this);
        work_time_of_employee.setOnClickListener(this);
        filter_text_self.setOnClickListener(this);
        filter_text_team.setOnClickListener(this);
        close_filter_name.setOnClickListener(this);
        close_filter_checkin.setOnClickListener(this);
        close_filter_checkout.setOnClickListener(this);
        close_filter_worktime.setOnClickListener(this);
        filter_name_of_month.setOnClickListener(this);
        notification.setOnClickListener(this);
        filter_text_all.setOnClickListener(this);
    }

    private void getBundleData() {
        attendence_arrayList = (List<Attendance>) bundle.getSerializable("myclass");
        emp_userId           = bundle.getString("employee_userid");
        selected_date        = bundle.getString("selected_date");
        check_in             = bundle.getString("check_in");
        check_out            = bundle.getString("check_out");
        work_time            = bundle.getString("work_time");
        employee_name        = bundle.getString("employee_name");
        bundle_date          = bundle.getString("date");
        attendance_filter    = bundle.getInt("attendance_filter");
        filte_working_time   = bundle.getInt("filte_working_time");
        filter_type          = bundle.getInt("filter_type");
        fromDate=bundle.getString("fromDate");
        toDate=bundle.getString("toDate");

        clciked_date = selected_date;
        tab_selected_position = filter_type;

        if (attendence_arrayList.size()==0) {
            getActivity().finish();
        }
    }

    private void initializeWidgets() {
        closearch = (ImageView) view.findViewById(R.id.closearch);
        search_view = (RelativeLayout) view.findViewById(R.id.search_view);
        et_search = (EditText) view.findViewById(R.id.et_search);


        back_button = (ImageView) view.findViewById(R.id.back_button);
        filter_text = (TextView) view.findViewById(R.id.filter_text);
        employee_checkout_rel = (LinearLayout)view.findViewById(R.id.employee_checkout_rel);
        employee_check_in_rel = (LinearLayout)view.findViewById(R.id.employee_check_in_rel);

        filter_recycle_view = (RecyclerView) view.findViewById(R.id.filter_recycle_view);
        text_nodata = (TextView) view.findViewById(R.id.text_nodata);
        filter_image_filter  = (ImageView) view.findViewById(R.id.filter_image_filter);
        filter_tv_reset      = (ImageView) view.findViewById(R.id.filter_tv_reset);
        filter_name_of_month = (TextView) view.findViewById(R.id.filter_name_of_month);
        filter_previous_month= (TextView) view.findViewById(R.id.filter_previous_month);
        filter_next_month    = (TextView) view.findViewById(R.id.filter_next_month);
        filter_month_name    = (TextView) view.findViewById(R.id.filter_month_name);
        filter_text_team     = (TextView) view.findViewById(R.id.filter_text_team);
        //filter_button_switch = (SwitchCompat) view.findViewById(R.id.filter_button_switch);
        filter_text_self     = (TextView) view.findViewById(R.id.filter_text_self);
        filter_text_all     = (TextView) view.findViewById(R.id.filter_text_all);
        filter_month_caledar = (RecyclerView) view.findViewById(R.id.filter_month_caledar);
        swipe_refresh        = (SwipeRefreshLayout) view.findViewById(R.id.filter_swipe_refresh);
        employee_checkout    = (TextView) view.findViewById(R.id.employee_checkout);
        employee_check_in    = (TextView) view.findViewById(R.id.employee_check_in);
        name_of_employee     = (TextView) view.findViewById(R.id.name_of_employee);
        filter_add_layout    = (HorizontalScrollView) view.findViewById(R.id.filter_add_layout);
        work_time_of_employee = (TextView) view.findViewById(R.id.work_time_of_employee);
        close_filter_name    = (ImageView) view.findViewById(R.id.close_filter_name);
        close_filter_checkin = (ImageView) view.findViewById(R.id.close_filter_checkin);
        close_filter_checkout = (ImageView) view.findViewById(R.id.close_filter_checkout);
        close_filter_worktime = (ImageView) view.findViewById(R.id.close_filter_worktime);
        notification          = (RelativeLayout) view.findViewById(R.id.notification);
        notification_count    = (TextView) view.findViewById(R.id.notification_count);
        notification_count.setText(""+notification_num);
        if (sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE,0)==Constants.UserRole.ADMIN || admin){
            filter_text_all.setVisibility(View.VISIBLE);
            if (sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD,false)) {
                filter_text_team.setVisibility(View.VISIBLE);
            }else {
                filter_text_team.setVisibility(View.GONE);
            }
        }else {
            if (sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD,false)) {
                filter_text_team.setVisibility(View.VISIBLE);
            }else {
                filter_text_team.setVisibility(View.GONE);
            }
            filter_text_all.setVisibility(View.GONE);
        }
        if (team){

            try {

                filter_name_of_month.setText(getRequiredDate1(selected_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setTheData(Constants.Tabclick.TEAM,false);

        }else if (admin){

            try {

                filter_name_of_month.setText(getRequiredDate1(selected_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setTheData(Constants.Tabclick.ALL,false);

        }
        else
        {
            search_view.setVisibility(View.GONE);
            try {

                filter_name_of_month.setText(getRequiredDate1(fromDate)+" - "+getRequiredDate1(toDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            callToServerToGetSelfData("");
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        filter_recycle_view.setLayoutManager(linearLayoutManager);
        timeAdapter = new TroopTimeAdapter(getActivity(), attendence_arrayList, "team", "employee");
        timeAdapter.setTheTabPosition(filter_type);
        filter_recycle_view.setAdapter(timeAdapter);

       /* ImageView back_button     = (ImageView) getActivity().getWindow().findViewById(R.id.back_button);
        back_button.setVisibility(View.GONE);*/
        TextView  cler_button     = (TextView) getActivity().getWindow().findViewById(R.id.cler_button);
        cler_button.setVisibility(View.GONE);
        RelativeLayout date_picker= (RelativeLayout) getActivity().getWindow().findViewById(R.id.date_picker);
        date_picker.setVisibility(View.GONE);
        LinearLayout employees  = (LinearLayout) getActivity().getWindow().findViewById(R.id.employees);
        employees.setVisibility(View.GONE);
        RadioButton filter_check_in   = (RadioButton) getActivity().getWindow().findViewById(R.id.rb_latelogin);
        filter_check_in.setVisibility(View.GONE);
        RadioButton filter_check_out  = (RadioButton) getActivity().getWindow().findViewById(R.id.rb_early_logout);
        filter_check_out.setVisibility(View.GONE);
        RadioGroup radioGroup  = (RadioGroup) getActivity().getWindow().findViewById(R.id.radio_group);
        radioGroup.setVisibility(View.GONE);
        Spinner filter_work       = (Spinner) getActivity().getWindow().findViewById(R.id.filter_work);
        filter_work.setVisibility(View.GONE);
        TextView tv_search        = (TextView) getActivity().getWindow().findViewById(R.id.tv_search);
        tv_search.setVisibility(View.GONE);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length()>0){
                    if (timeAdapter!=null){
                        timeAdapter.search(s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setTheData(int tab_click,boolean call) {
        try {
            switch (tab_click) {
                case Constants.Tabclick.TEAM:
                    if (et_search!=null){
                        et_search.getText().clear();
                    }

                    tab_selected_position = Constants.Tabclick.TEAM;
                    teamtext_selected = true;
                    filter_text_self.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_notselected));
                    filter_text_self.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_notselected));

                    if (filter_text_team != null && filter_text_team.getVisibility() == View.VISIBLE) {
                        filter_text_team.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_selected));
                        filter_text_team.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_sleceted));
                    }
                    if (filter_text_all != null && filter_text_all.getVisibility() == View.VISIBLE) {
                        filter_text_all.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_notselected));
                        filter_text_all.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_notselected));
                    }
                    if (call) {
                        getTheTroopTeamData();
                    }
                    break;
                case Constants.Tabclick.ALL:
                    if (et_search!=null){
                        et_search.getText().clear();
                    }
                    tab_selected_position = Constants.Tabclick.ALL;
                    teamtext_selected = true;

                    filter_text_self.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_notselected));
                    filter_text_self.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_notselected));
                    if (filter_text_team != null && filter_text_team.getVisibility() == View.VISIBLE) {
                        filter_text_team.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_notselected));
                        filter_text_team.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_notselected));
                    }
                    if (filter_text_all != null && filter_text_all.getVisibility() == View.VISIBLE) {
                        filter_text_all.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_team_selected));
                        filter_text_all.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_sleceted));
                    }
                    if (call) {
                        getTheTroopTeamData();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
       /* ImageView back_button     = (ImageView) getActivity().getWindow().findViewById(R.id.back_button);
        back_button.setVisibility(View.VISIBLE);*/
        TextView  cler_button     = (TextView) getActivity().getWindow().findViewById(R.id.cler_button);
        cler_button.setVisibility(View.VISIBLE);
        RelativeLayout date_picker= (RelativeLayout) getActivity().getWindow().findViewById(R.id.date_picker);
        date_picker.setVisibility(View.VISIBLE);
        LinearLayout employees  = (LinearLayout) getActivity().getWindow().findViewById(R.id.employees);
        employees.setVisibility(View.VISIBLE);
        RadioButton filter_check_in   = (RadioButton) getActivity().getWindow().findViewById(R.id.rb_latelogin);
        filter_check_in.setVisibility(View.VISIBLE);
        RadioButton filter_check_out  = (RadioButton) getActivity().getWindow().findViewById(R.id.rb_early_logout);
        filter_check_out.setVisibility(View.VISIBLE);
        RadioGroup radioGroup  = (RadioGroup) getActivity().getWindow().findViewById(R.id.radio_group);
        radioGroup.setVisibility(View.VISIBLE);
        Spinner filter_work       = (Spinner) getActivity().getWindow().findViewById(R.id.filter_work);
        filter_work.setVisibility(View.VISIBLE);
        TextView tv_search        = (TextView) getActivity().getWindow().findViewById(R.id.tv_search);
        tv_search.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notification:

                notification_num=0;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SharePreferenceKeys.NOTIFICATION_COUNT,notification_num);
                editor.apply();
                notification_count.setText(""+notification_num);
                break;
            case R.id.filter_name_of_month:

              /*  teamtext_selected=true;
                final Calendar pic_calendar = Calendar.getInstance();
                final int pic_year = pic_calendar.get(Calendar.YEAR);
                final int pic_month = pic_calendar.get(Calendar.MONTH);
                int pic_day = pic_calendar.get(Calendar.DAY_OF_MONTH);

                pic_dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int pi_year, int day_month, int dayOfMonth)
                    {
                        if (view.isShown())
                        {
                            if ((day_month+1)<10)
                            {
                                String mon = "0" + "" + (day_month + 1);
                                if (dayOfMonth<10) {
                                    String day = "0" + "" + dayOfMonth;
                                    select_day = "" + String.valueOf(pi_year) + "-" + mon + "-" + day;
                                }else {
                                    select_day = "" + String.valueOf(pi_year) + "-" + mon + "-" + dayOfMonth;
                                }
                            }else {
                                if (dayOfMonth<10) {
                                    String day = "0" + "" + dayOfMonth;
                                    select_day = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month+1) + "-" + day;
                                }else {
                                    select_day = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month+1) + "-" + dayOfMonth;
                                }
                            }
                            month = day_month + 1;
                            year = pi_year;
                            select_year = year;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(pi_year,day_month,dayOfMonth);

                            String month_name = new SimpleDateFormat("MMM").format(calendar.getTime());

                            filter_name_of_month.setText(month_name+", "+select_year);
                            if (teamtext_selected) {

                                if (select_day!=null && !select_day.isEmpty()){
                                    clciked_date = select_day;
                                    selected_date = "";
                                    select_day="";
                                }
                                getTheTroopTeamData();
                            }
                        }

                    }
                }, pic_year, pic_month, pic_day);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    pic_dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                }else {
                    pic_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                pic_dialog.show();*/
                break;
            case R.id.filter_text_self:
                Intent intent1 = new Intent(getActivity(),AttendanceActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("type","2");
                intent1.putExtra("filterType",false);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.filter_text_team:
                teamtext_selected=true;
                tab_selected_position = Constants.Tabclick.TEAM;
                setTheData(Constants.Tabclick.TEAM,true);
                break;
            case R.id.filter_text_all:
                tab_selected_position = Constants.Tabclick.ALL;
                teamtext_selected=true;
                setTheData(Constants.Tabclick.ALL,true);
                break;
            case R.id.close_filter_worktime:
                count++;
                work_time="";
                filte_working_time = 0;
                String dayq = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty()){
                    clciked_date =select_day;
                    select_day = "";
                }
                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                if (count==4)
                {
                    filter_add_layout.setVisibility(View.GONE);
                }
                work_time_of_employee.setVisibility(View.GONE);
                close_filter_worktime.setVisibility(View.GONE);
                break;
            case R.id.close_filter_name:
                count++;
                emp_userId="";
                String day = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date = select_day;
                    select_day = "";
                }
                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                if (count==4)
                {
                    filter_add_layout.setVisibility(View.GONE);
                }

                name_of_employee.setVisibility(View.GONE);
                close_filter_name.setVisibility(View.GONE);
                break;
            case R.id.close_filter_checkin:
                count++;
                check_in="";
                attendance_filter = 0;
                String days = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date = select_day;
                    select_day="";
                }

                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                if (count==4)
                {
                    filter_add_layout.setVisibility(View.GONE);
                }
                employee_check_in.setVisibility(View.GONE);
                close_filter_checkin.setVisibility(View.GONE);
                break;
            case R.id.close_filter_checkout:
                count++;
                check_out="";
                attendance_filter = 0;
                String day2 = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date = select_day;
                    select_day="";
                }
                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                if (count==4)
                {
                    filter_add_layout.setVisibility(View.GONE);
                }
                employee_checkout.setVisibility(View.GONE);
                close_filter_checkout.setVisibility(View.GONE);
                break;




           /* case R.id.nav_request:
                drawerLayout.closeDrawer(GravityCompat.START);
                RequestDialog dialog=new RequestDialog(getActivity());
                dialog.show();
                break;
            case R.id.todo_task:
                drawerLayout.closeDrawer(GravityCompat.START);
                Navigation.getInstance().home(getActivity());
                break;*/
            case R.id.filter_previous_month:

                calenderModels.removeAll(calenderModels);
                if (current_month <= 1) {
                    current_month = 12;
                    select_year--;
                } else {
                    current_month--;
                }
                if (current_month == 1)
                {
                    filter_name_of_month.setText("jan, "+select_year);
                    filter_month_name.setText(""+select_year);
                } else if (current_month == 2)
                {
                    filter_name_of_month.setText("feb, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month== 3)
                {
                    filter_name_of_month.setText("mar, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 4)
                {
                    filter_name_of_month.setText("apr, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month== 5)
                {
                    filter_name_of_month.setText("may, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 6)
                {
                    filter_name_of_month.setText("jun, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month== 7)
                {
                    filter_name_of_month.setText("jul, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 8)
                {
                    filter_name_of_month.setText("aug, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 9)
                {
                    filter_name_of_month.setText("sep, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 10)
                {
                    filter_name_of_month.setText("oct, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 11)
                {
                    filter_name_of_month.setText("nov, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 12)
                {
                    filter_name_of_month.setText("dec, "+select_year);
                    filter_month_name.setText("" + select_year);
                }

                setGridCellAdapterToDate(current_month, select_year);
                Calendar calendar = Calendar.getInstance();
                String day3 = new SimpleDateFormat("dd").format(calendar.getTime());
                String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                String day4 = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date = select_day;
                    select_day = "";
                }else
                if (clciked_date!=null)
                {
                    String[] split_date = clciked_date.split("-");
                    day4 = split_date[2];
                    if (current_month<10)
                    {
                        String mon = "0"+""+current_month;
                        clciked_date = select_year+"-"+mon+"-"+day4;
                    }else {
                        clciked_date = select_year + "-" + current_month + "-" + day4;
                    }
                    selected_position = Integer.parseInt(day4)-1;
                }else {
                    clciked_date = selected_date;
                }
                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                break;
            case R.id.filter_next_month:
                calenderModels.removeAll(calenderModels);
                if (current_month > 11) {
                    current_month = 1;
                    select_year++;
                } else {
                    current_month++;
                }
                if (current_month== 1)
                {
                    filter_name_of_month.setText("jan, "+select_year);
                    filter_month_name.setText(""+select_year);
                } else if (current_month == 2)
                {
                    filter_name_of_month.setText("feb, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 3)
                {
                    filter_name_of_month.setText("mar, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month== 4)
                {
                    filter_name_of_month.setText("apr, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 5)
                {
                    filter_name_of_month.setText("may, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 6)
                {
                    filter_name_of_month.setText("jun, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 7)
                {
                    filter_name_of_month.setText("jul, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 8)
                {
                    filter_name_of_month.setText("aug, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 9)
                {
                    filter_name_of_month.setText("sep, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 10)
                {
                    filter_name_of_month.setText("oct, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 11)
                {
                    filter_name_of_month.setText("nov, "+select_year);
                    filter_month_name.setText("" + select_year);
                } else if (current_month == 12)
                {
                    filter_name_of_month.setText("dec, "+select_year);
                    filter_month_name.setText("" + select_year);
                }

                setGridCellAdapterToDate(current_month, select_year);
                Calendar calendar_a = Calendar.getInstance();
                String day_a = new SimpleDateFormat("dd").format(calendar_a.getTime());
                String cur_date_a = new SimpleDateFormat("yyyy-MM-dd").format(calendar_a.getTime());
                String day5 = "";
                if (selected_date!=null && !selected_date.isEmpty())
                {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date = select_day;
                    select_day = "";
                }else
                if (clciked_date!=null)
                {
                    String[] split_date = clciked_date.split("-");
                    day5 = split_date[2];
                    if (current_month<10)
                    {
                        String mon  = "0"+""+current_month;
                        clciked_date = select_year+"-"+mon+"-"+day5;
                    }else {
                        clciked_date = select_year + "-" + current_month+ "-" + day5;
                    }
                    selected_position = Integer.parseInt(day5)-1;
                }else {
                    clciked_date = selected_date;
                }

                if (Utilities.getConnectivityStatus(getActivity())>0)
                {
                    getTheTroopTeamData();
                }else {
                    Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                }
                break;
            case R.id.filter_tv_reset:
                emp_userId="";
                check_in="";
                check_out="";
                work_time="";
                String day1="";
                filter_add_layout.setVisibility(View.GONE);
                //setGridCellAdapterToDate(month, year);
                Calendar calendars = Calendar.getInstance();
                String dayss = new SimpleDateFormat("dd").format(calendars.getTime());
                String cur_dates = new SimpleDateFormat("yyyy-MM-dd").format(calendars.getTime());
                String daysss = "";

                String[] split_date = cur_dates.split("-");
                String year_d  = split_date[0];
                int month_d = Integer.parseInt(split_date[1]);
                int day_d   = Integer.parseInt(split_date[2]);

                if (month_d<10)
                {
                    String mon = "0" + month_d;
                    if (day_d<10) {
                        String day_as = "0"+day_d;
                        clciked_date = year_d+"-"+mon+"-"+day_as;
                        selected_position = day_d - 1;
                    }else {
                        clciked_date = year_d+"-"+mon+"-"+String.valueOf(day_d);
                        selected_position = day_d- 1;
                    }
                }else {
                    if (day_d<10) {
                        String day_as = "0"+day_d;
                        clciked_date = year_d+"-"+String.valueOf(month_d)+"-"+day_as;
                        selected_position = day_d- 1;
                    }else {
                        clciked_date = year_d+"-"+String.valueOf(month_d)+"-"+String.valueOf(day_d);
                        selected_position = day_d- 1;
                    }
                }

                getTheTroopTeamData();

                break;
            case R.id.filter_image_filter:
                /*Intent filter_intent  = new Intent(getActivity(),AttendanceFilterActivity.class);
                startActivity(filter_intent);
                getActivity().finish();*/
                Navigation.getInstance().openFilterPage(getActivity(),tab_selected_position);
                break;
        }
    }

    private void getTheTroopTeamData()
    {
        if (et_search!=null){
            et_search.getText().clear();
        }
        swipe_refresh.setRefreshing(true);
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        if (attendence_arrayList!=null && attendence_arrayList.size() > 0) {
            attendence_arrayList.removeAll(attendence_arrayList);
            if (timeAdapter!=null){
                timeAdapter.notifyDataSetChanged();
            }
        }
        attendence_arrayList = new ArrayList<>();
        if (tab_selected_position==Constants.Tabclick.ALL){
            callToserverToGetAllThedata("");
        }else  if(tab_selected_position==Constants.Tabclick.TEAM){
            callToserverToGetThedata("");
        }




    }
    private void callToserverToGetThedata(String date) {
        try {
            retrofit2.Call<FilterAttendanceApi.FilterAttendenceApiResponce> call = FilterAttendanceApi.getApiService()
                    .getFilterAttendence(userId,clciked_date,sharedPreferences.getString(SharePreferenceKeys.API_KEY,""),
                            emp_userId,attendance_filter+"",filte_working_time+"");
            call.enqueue(new retrofit2.Callback<FilterAttendanceApi.FilterAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<FilterAttendanceApi.FilterAttendenceApiResponce> call, @NonNull Response<FilterAttendanceApi.FilterAttendenceApiResponce> response) {
                    if (response.code()== Constants.RESPONCE_SUCCESSFUL){
                        FilterAttendanceApi.FilterAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce!=null){
                            if (attendenceApiResponce.getSuccess()){
                                if (attendence_arrayList!=null && attendence_arrayList.size()>0){
                                    attendence_arrayList.clear();
                                }
                                attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                sortList();
                                addTheDataToAdapter("team");
                            }
                        }else {
                            swipe_refresh.setRefreshing(false);
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<FilterAttendanceApi.FilterAttendenceApiResponce> call,@NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callToServerToGetSelfData(String date) {
        try {
            retrofit2.Call<SelfAttendenceApi.SelfAttendenceApiResponce> call =
                    SelfAttendenceApi.getApiService().getSelefAttendence(userId,sharedPreferences.getString(SharePreferenceKeys.API_KEY,""),
                            fromDate,toDate,String.valueOf(attendance_filter),String .valueOf(filte_working_time));
            call.enqueue(new retrofit2.Callback<SelfAttendenceApi.SelfAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<SelfAttendenceApi.SelfAttendenceApiResponce> call, @NonNull Response<SelfAttendenceApi.SelfAttendenceApiResponce> response) {
                    if (response.code()== Constants.RESPONCE_SUCCESSFUL){
                        SelfAttendenceApi.SelfAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce!=null){
                            if (attendenceApiResponce.getSuccess()){
                                if (attendence_arrayList!=null && attendence_arrayList.size()>0){
                                    attendence_arrayList.clear();

                                }
                                attendence_arrayList=attendenceApiResponce.getAttendance();
                                //attendence_arrayList.addAll(attendenceApiResponce.getAttendance());

                               // addTheDataToAdapter("self");
                                if(attendence_arrayList.size()>0)
                                {
                                    timeAdapter.setarrayList(attendence_arrayList);
                                    timeAdapter.setAdapterType("self");
                                    timeAdapter.notifyDataSetChanged();
                                }
                                else {
                                    text_nodata.setVisibility(View.VISIBLE);
                                    filter_recycle_view.setVisibility(View.GONE);
                                }

                            }
                        }else {
                            swipe_refresh.setRefreshing(false);
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<SelfAttendenceApi.SelfAttendenceApiResponce> call,@NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void callToserverToGetAllThedata(String date) {
        try {

            retrofit2.Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call = FilterAllAttendanceApi.getApiService()
                    .getFilterAllAttendence(userId,clciked_date,sharedPreferences.getString(SharePreferenceKeys.API_KEY,""),
                            emp_userId,attendance_filter,filte_working_time);
            call.enqueue(new retrofit2.Callback<FilterAllAttendanceApi.FilterAllAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call, @NonNull Response<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> response) {
                    if (response.code()== Constants.RESPONCE_SUCCESSFUL){
                        FilterAllAttendanceApi.FilterAllAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce!=null){
                            if (attendenceApiResponce.getSuccess()){
                                if (attendence_arrayList!=null && attendence_arrayList.size()>0){
                                    attendence_arrayList.clear();
                                }
                                attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                sortList();
                                addTheDataToAdapter("team");
                            }
                        }else {
                            swipe_refresh.setRefreshing(false);
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call,@NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void addTheDataToAdapter(String team) {
        try {
            int day = 0;
            swipe_refresh.setRefreshing(false);
            if (clciked_date != null && !clciked_date.isEmpty()) {
                String[] split = clciked_date.split("-");
                String date = split[2];
                day = Integer.parseInt(date);
            }
            if (attendence_arrayList.size() > 0) {
               /* if (tab_selected_position==2 || tab_selected_position==3)
                {
                    Collections.sort(attendence_arrayList, new Comparator<Attendance>() {
                        @Override
                        public int compare(Attendance o1, Attendance o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }*/
                text_nodata.setVisibility(View.GONE);
                filter_recycle_view.setVisibility(View.VISIBLE);
                if (timeAdapter != null && filter_recycle_view.getVisibility() == View.VISIBLE) {
                    timeAdapter.setTheTabPosition(tab_selected_position);
                    timeAdapter.setarrayList(attendence_arrayList);
                    timeAdapter.setAdapterType(team);
                    timeAdapter.notifyDataSetChanged();
                }
            } else {
                text_nodata.setVisibility(View.VISIBLE);
                filter_recycle_view.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b)
        {
            _calendar = Calendar.getInstance(Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            select_year = _calendar.get(Calendar.YEAR);
            if (month == 1) {
                filter_name_of_month.setText("jan");
                filter_month_name.setText("" + select_year);
            } else if (month == 2) {
                filter_name_of_month.setText("feb");
                filter_month_name.setText("" + select_year);
            } else if (month == 3) {
                filter_name_of_month.setText("mar");
                filter_month_name.setText("" + select_year);
            } else if (month == 4) {
                filter_name_of_month.setText("apr");
                filter_month_name.setText("" + select_year);
            } else if (month == 5) {
                filter_name_of_month.setText("may");
                filter_month_name.setText("" + select_year);
            } else if (month == 6) {
                filter_name_of_month.setText("jun");
                filter_month_name.setText("" + select_year);
            } else if (month == 7) {
                filter_name_of_month.setText("jul");
                filter_month_name.setText("" + select_year);
            } else if (month == 8) {
                filter_name_of_month.setText("aug");
                filter_month_name.setText("" + select_year);
            } else if (month == 9) {
                filter_name_of_month.setText("sep");
                filter_month_name.setText("" + select_year);
            } else if (month == 10) {
                filter_name_of_month.setText("oct");
                filter_month_name.setText("" + select_year);
            } else if (month == 11) {
                filter_name_of_month.setText("nov");
                filter_month_name.setText("" + select_year);
            } else if (month == 12) {
                filter_name_of_month.setText("dec");
                filter_month_name.setText("" + select_year);
            }
            adapter = new GridCellAdapter(getActivity(), month, select_year, "team");
            adapter.notifyDataSetChanged();
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            filter_month_caledar.setLayoutManager(manager);
            filter_month_caledar.setAdapter(adapter);
            Calendar calendar = Calendar.getInstance();
            String days = new SimpleDateFormat("dd").format(calendar.getTime());
            String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String day = "";
            if (clciked_date!=null)
            {
                String[] split_date = clciked_date.split("-");
                day = split_date[2];
                clciked_date = select_year+"-"+month+"-"+day;
                selected_position = Integer.parseInt(day)-1;
            }else {
                clciked_date = selected_date;
            }
            if (Utilities.getConnectivityStatus(getActivity())>0) {
                filter_text_team.setTextColor(ContextCompat.getColor(getActivity(), R.color.filter_bg_color));
                filter_text_self.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_team));
                getTheTroopTeamData();

            } else {
                Toast.makeText(getActivity(), "Network Not Available", Toast.LENGTH_SHORT).show();
            }
        }else {
            Intent intent1 = new Intent(getActivity(),AttendanceActivity.class);
            intent1.putExtra("type","2");
            intent1.putExtra("filterType",false);
            startActivity(intent1);
            getActivity().finish();
        }
    }

    private void getTheCalenderDayView()
    {
        filter_name_of_month.setVisibility(View.VISIBLE);
        if (selected_date!=null && !selected_date.isEmpty())
        {
            month_position = month_position+1;
            if (month_position == 1) {
                filter_name_of_month.setText("jan, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 2) {
                filter_name_of_month.setText("feb, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 3) {
                filter_name_of_month.setText("mar, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 4) {
                filter_name_of_month.setText("apr, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 5) {
                filter_name_of_month.setText("may, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 6) {
                filter_name_of_month.setText("jun, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 7) {
                filter_name_of_month.setText("jul, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 8) {
                filter_name_of_month.setText("aug, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 9) {
                filter_name_of_month.setText("sep, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 10) {
                filter_name_of_month.setText("oct, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 11) {
                filter_name_of_month.setText("nov, "+select_year);
                filter_month_name.setText("" + select_year);
            } else if (month_position == 12) {
                filter_name_of_month.setText("dec, "+select_year);
                filter_month_name.setText("" + select_year);
            }
            adapter = new GridCellAdapter(getActivity(), month_position, select_year, "team");
            adapter.notifyDataSetChanged();
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            filter_month_caledar.setLayoutManager(manager);
            filter_month_caledar.setAdapter(adapter);
        }
    }

    @Override
    public void onRefresh() {
        if (et_search!=null){
            et_search.getText().clear();
        }
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        if (Utilities.getConnectivityStatus(getActivity())>0) {
            if (teamtext_selected) {
                if (selected_date!=null && !selected_date.isEmpty()) {
                    clciked_date = selected_date;
                    selected_date ="";
                }else
                if (select_day!=null && !select_day.isEmpty())
                {
                    clciked_date= select_day;
                    select_day = "";
                }
                if (et_search!=null){
                    et_search.getText().clear();
                }

                try {
                    SimpleDateFormat current_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String[] spliting_date = clciked_date.split("-");
                    int day1 = Integer.parseInt(spliting_date[2]);
                    int month1 = Integer.parseInt(spliting_date[1]);
                    int year1 = Integer.parseInt(spliting_date[0]);
                    Calendar calendar_s = Calendar.getInstance();
                    //calendar_s.set(year1, (month1 - 1), day1);
                    String serverDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar_s.getTime());
                    Date server_date = current_dateFormat.parse(clciked_date);
                    Date cuurent_date= current_dateFormat.parse(serverDate);

                    if (server_date.after(cuurent_date)) {
                        swipe_refresh.setRefreshing(false);
                        text_nodata.setVisibility(View.VISIBLE);
                        filter_recycle_view.setVisibility(View.GONE);
                    }else {
                        getTheTroopTeamData();

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else {
            Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
        }
    }

    public class GridCellAdapter extends RecyclerView.Adapter<GridCellAdapter.MyHolder> implements View.OnClickListener
    {

        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView num_events_per_day;
        String which_one;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        // Days in Current Month

        public GridCellAdapter(Context applicationContext, int month, int year, String team)
        {
            this._context = applicationContext;
            this.list = new ArrayList<String>();
            which_one = team;
            _calendar = Calendar.getInstance();
            setCurrentDayOfMonth(_calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(_calendar.get(Calendar.DAY_OF_WEEK));

            // Print Month
            if (which_one.equals("team")) {
                printMonth(month, year);
            }
            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }


        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy)
        {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;
            int pos=0;
            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Calendar cal1 = Calendar.getInstance();
            cal1.set(yy,mm,1);

            SimpleDateFormat df = new SimpleDateFormat("EEE yyyy MMM dd");
            calenderModels = new ArrayList<>();
            for (int i=1;i<=daysInMonth;i++)
            {
                cal1.set(yy,currentMonth, i);
                String date = df.format(cal1.getTime());
                String[] split = date.split(" ");
                CalenderModel model = new CalenderModel();
                String day_name = split[0];
                String year = split[1];
                String month = split[2];
                String day = split[3];
                String dates = df.format(cal1.getTime());
                model.setDayName(day_name);
                model.setYear(year);
                model.setMonth(month);
                model.setDay(day);
                model.setDate(dates);
                calenderModels.add(model);
            }

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);


            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;


            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.month_view, parent, false);
            return new GridCellAdapter.MyHolder(row);
        }

        @Override
        public void onBindViewHolder(final GridCellAdapter.MyHolder holder, final int position)
        {

            final CalenderModel model = calenderModels.get(position);


            if (which_one.equals("team"))
            {
                String dayname = model.getDayName();

                String dayDate = model.getDay();
                holder.day_date.setText(dayDate);
                holder.day_name.setText(dayname);
                filter_month_caledar.getLayoutManager().scrollToPosition(selected_position+4);
                if(selected_position == position)
                {
                    if (dayname.equals("Sat"))
                    {
                        holder.day_date.setTextColor(ContextCompat.getColor(_context,R.color.sat_sun_col));
                    }else if (dayname.equals("Sun"))
                    {

                        holder.day_date.setTextColor(ContextCompat.getColor(_context,R.color.sat_sun_col));
                    }else {
                        holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.select_text));
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.day_date.setText(Html.fromHtml("<u>" + model.getDay() + " </u>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.day_date.setText(Html.fromHtml("<u>" + model.getDay() + "  </u>"));
                    }
                }else
                {
                    if (dayname.equals("Sat"))
                    {
                        holder.day_date.setTextColor(ContextCompat.getColor(_context,R.color.sat_sun_col));
                    }else if (dayname.equals("Sun"))
                    {

                        holder.day_date.setTextColor(ContextCompat.getColor(_context,R.color.sat_sun_col));
                    }else {
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.day_date.setText(Html.fromHtml(model.getDay(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.day_date.setText(Html.fromHtml(model.getDay()));
                    }
                }
                holder.month_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        notifyItemChanged(selected_position);
                        selected_position = position;
                        notifyItemChanged(selected_position);
                        filter_month_caledar.getLayoutManager().scrollToPosition(selected_position+4);

                        CalenderModel model1 = calenderModels.get(position);
                        String select_date = model1.getDate();
                        Date date = new Date(select_date);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        clciked_date = simpleDateFormat.format(date);
                        if (Utilities.getConnectivityStatus(getActivity())>0)
                        {
                            getTheTroopTeamData();
                        }else {
                            Utilities.ShowSnackbar(getActivity(),filter_recycle_view, "Please Check Network Connection");
                        }
                    }
                });
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount()
        {
            return calenderModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        public class MyHolder extends RecyclerView.ViewHolder
        {
            TextView day_name,day_date,month_name;
            LinearLayout month_layout;
            public MyHolder(View itemView) {
                super(itemView);

                day_date = (TextView) itemView.findViewById(R.id.day_date);
                day_name = (TextView) itemView.findViewById(R.id.day_name);
                month_layout = (LinearLayout) itemView.findViewById(R.id.month_layout);

            }
        }
    }


    private void sortList()
    {

        List<Attendance> top_arrayList = new ArrayList<>();
        List<Attendance> bottom_arrayList = new ArrayList<>();
        List<Attendance> middle_arrayList = new ArrayList<>();

        if(attendence_arrayList!=null && !attendence_arrayList.isEmpty() && attendence_arrayList.size()>0)
        {
            int size=attendence_arrayList.size();

            String pinStatus="";
            String pinnedDate="";
            for(int i=0;i<size;i++)
            {
                if(attendence_arrayList.get(i).getIsPinned()!=null && !attendence_arrayList.get(i).getIsPinned().isEmpty())
                {
                    pinStatus=attendence_arrayList.get(i).getIsPinned();
                    pinnedDate=attendence_arrayList.get(i).getPinnedDate();
                    if(pinnedDate!=null && !pinnedDate.isEmpty())
                    {


                    }

                    if(pinStatus.equals("1"))
                    {
                        top_arrayList.add(attendence_arrayList.get(i));
                    }
                    else  if(pinStatus.equals("2"))
                    {
                        bottom_arrayList.add(attendence_arrayList.get(i));
                    }
                    else
                    {
                        middle_arrayList.add(attendence_arrayList.get(i));
                    }


                }

            }
            if(top_arrayList!=null && !top_arrayList.isEmpty() && top_arrayList.size()>0) {

                int len=top_arrayList.size();
                int i,j;

                for ( i=0;i<len;i++){

                    for(j=i+1;j<len;j++){

                        String firstPinnedTime=top_arrayList.get(i).getPinnedDate();
                        Long f_time=0L,s_time=0L;

                        if(firstPinnedTime!=null && !firstPinnedTime.isEmpty())
                        {

                            try {

                                f_time= Long.valueOf(getRequiredDate(firstPinnedTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        String secondPinnedTime=top_arrayList.get(j).getPinnedDate();

                        if(secondPinnedTime!=null && !secondPinnedTime.isEmpty())
                        {

                            try {

                                s_time= Long.valueOf(getRequiredDate(secondPinnedTime));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        if(f_time>s_time){

                            Attendance attendance= top_arrayList.get(i);
                            top_arrayList.add(i, top_arrayList.get(j));
                            top_arrayList.add(j, attendance);
                        }
                        else
                        {

                        }
                    }
                }

                /* for (i=0;i<5;i++){
                    for(j=i+1;j<5;j++){
                        if(num[i]>num[j]){
                            temp = num[i];
                            num[i] = num[j];
                            num[j] = temp;
                        }
                    }
                }*/
            }
            if(bottom_arrayList!=null && !bottom_arrayList.isEmpty() && bottom_arrayList.size()>0) {

                int len = bottom_arrayList.size();
                int i, j;

                for (i = 0; i < len; i++) {

                    for (j = i + 1; j < len; j++) {

                        String firstPinnedTime = bottom_arrayList.get(i).getPinnedDate();
                        Long f_time = 0L, s_time = 0L;

                        if (firstPinnedTime != null && !firstPinnedTime.isEmpty()) {

                            try {

                                f_time = Long.valueOf(getRequiredDate(firstPinnedTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        String secondPinnedTime = bottom_arrayList.get(j).getPinnedDate();

                        if (secondPinnedTime != null && !secondPinnedTime.isEmpty()) {

                            try {

                                s_time = Long.valueOf(getRequiredDate(secondPinnedTime));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        if (f_time < s_time) {

                            Attendance attendance = bottom_arrayList.get(i);
                            bottom_arrayList.add(i, bottom_arrayList.get(j));
                            bottom_arrayList.add(j, attendance);
                        } else {

                        }
                    }
                }
            }


            if(top_arrayList.size()>0 || bottom_arrayList.size()>0 || middle_arrayList.size()>0)
            {
                attendence_arrayList.clear();
            }

            if(top_arrayList.size()>0)
            {
                for(int i=0;i<top_arrayList.size();i++)
                {
                    attendence_arrayList.add(top_arrayList.get(i));

                }
            }
            if(middle_arrayList.size()>0)
            {
                for(int i=0;i<middle_arrayList.size();i++)
                {
                    attendence_arrayList.add(middle_arrayList.get(i));

                }
            }
            if(bottom_arrayList.size()>0)
            {
                for(int i=0;i<bottom_arrayList.size();i++)
                {
                    attendence_arrayList.add(bottom_arrayList.get(i));

                }
            }


        }
        if(attendence_arrayList!=null && !attendence_arrayList.isEmpty() && attendence_arrayList.size()>0) {
            int size = attendence_arrayList.size();

        }

    }
    public String getRequiredDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.getTimeInMillis()+"";
    }
    public String getRequiredDate1(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return sdf2.format(c.getTime());
    }
}
