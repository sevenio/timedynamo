package com.tvisha.trooptime.activity.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.apiPostModels.PinUnpinResponse;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.NewAttendanceActivity;
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
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class TroopTimeAdapter extends RecyclerSwipeAdapter<TroopTimeAdapter.ViewHolder> {

    boolean status = false;
    Context context;
    List<Attendance> data_arrayList = new ArrayList<>();
    List<Attendance> data_arrayList1 = new ArrayList<>();
    List<Attendance> allDataList = new ArrayList<>();
    ApiInterface apiService;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    ;
    String remarks_array;

    String who = "";
    View view;

    String activity_class;


    SharedPreferences sharedPreferences;
    String userId = "", apiKey = "";
    boolean self = false, team = false, admin = false;
    boolean is_self = false;
    String dateFormat1 = "yyyy-MM-dd hh:mm:ss";
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    int tab_position = 2;


    public TroopTimeAdapter(Context applicationContext, List<Attendance> data_arrayList, String self, String employee) {
        context = applicationContext;
        this.data_arrayList = data_arrayList;
        allDataList.addAll(data_arrayList);
        who = self;
        if (who.equals("team")) {
            is_self = false;
        } else {
            is_self = true;
        }
        activity_class = employee;
        apiService = ApiClient.getInstance();
        sharedPreferences = context.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
            shareprefernceData();
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sample_swipe;
    }

    @Override
    public TroopTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.team_employees_row_design, parent, false);
        return new ViewHolder(view);
    }

    public void setarrayList(List<Attendance> attendence_arrayList) {

        try {

            List<Attendance> temp = new ArrayList<>();
            temp.addAll(attendence_arrayList);

            if (data_arrayList != null && data_arrayList.size() > 0) {

                data_arrayList.clear();
            }

            if (data_arrayList == null) {
                data_arrayList = new ArrayList<>();
            }

            if (allDataList != null && allDataList.size() > 0) {
                allDataList.clear();
            }
            allDataList.addAll(temp);
            data_arrayList.addAll(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBindViewHolder(final TroopTimeAdapter.ViewHolder holder, final int position) {

        try {

            shareprefernceData();
            holder.sample_swipe.close();

            if (data_arrayList == null || data_arrayList.isEmpty() || data_arrayList.size() < 0) {
                return;
            }

            holder.redpinImage.setVisibility(View.GONE);
            holder.greypinImage.setVisibility(View.GONE);

            try {

                Attendance model = data_arrayList.get(position);
                if (position % 2 == 0) {
                    holder.singleitem_parent.setBackgroundColor(ContextCompat.getColor(context, R.color.first_item_col));
                } else {
                    holder.singleitem_parent.setBackgroundColor(ContextCompat.getColor(context, R.color.second_item_col));
                }


                if (is_self) {

                    holder.employee_profile_pic.setVisibility(View.GONE);
                    holder.employee_name.setVisibility(View.VISIBLE);
                    holder.sample_swipe.setSwipeEnabled(false);
                    holder.redpinImage.setVisibility(View.GONE);
                    holder.greypinImage.setVisibility(View.GONE);
                    holder.employee_id.setVisibility(View.GONE);

                    try {
                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(model.getDate());
                        String that_day = new SimpleDateFormat("dd MMM yyyy").format(d);
                        String[] strings = that_day.split(" ");
                        String day = strings[0];
                        String mon = strings[1];
                        String yea = strings[2];
                        String complete_day = day + " " + mon + "' " + yea;


                        holder.employee_name.setText(complete_day);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


                    holder.employee_id.setVisibility(View.VISIBLE);
                    holder.sample_swipe.setSwipeEnabled(true);
                    holder.employee_name.setVisibility(View.VISIBLE);
                    holder.employee_profile_pic.setVisibility(View.VISIBLE);

                    int pinType = 0, pinStatus = 0;
                    boolean showPin = false;
                    if (data_arrayList.get(position).getPinType() != null && data_arrayList.get(position).getIsPinned() != null) {
                        pinType = Integer.parseInt(data_arrayList.get(position).getPinType());
                        pinStatus = Integer.parseInt(data_arrayList.get(position).getIsPinned());
                        if (pinStatus != 0) {
                            if (team) {
                                if (pinType == Constants.PIN_TYPE_TEAM) {
                                    showPin = true;
                                }
                            } else if (admin) {
                                if (pinType == Constants.PIN_TYPE_ALL) {
                                    showPin = true;
                                }
                            } else {
                                showPin = false;
                            }
                        } else {
                            showPin = false;
                        }


                        if (showPin) {


                            if (data_arrayList.get(position).getIsPinned() != null && data_arrayList.get(position).getPinType() != null) {


                                if (pinStatus == Constants.PIN_STATUS_TOP_PINNED) {
                                    holder.redpinImage.setVisibility(View.VISIBLE);
                                    holder.pin_icon.setImageResource(R.drawable.unpin);
                                    holder.pin_icon.setTag("1");
                                    holder.pin_textview.setText("Unpin");
                                } else if (pinStatus == Constants.PIN_STATUS_BOTTOM_PINNED) {
                                    holder.greypinImage.setVisibility(View.VISIBLE);
                                    holder.bottom_icon.setImageResource(R.drawable.bottom_cross);
                                    holder.bottom_icon.setTag("1");
                                    holder.bottom_textview.setText("Unpin");
                                } else if ((pinStatus == Constants.PIN_STATUS_NONE)) {
                                    holder.pin_icon.setImageResource(R.drawable.pin);
                                    holder.pin_icon.setTag("0");
                                    holder.pin_textview.setText("pin");
                                    holder.redpinImage.setVisibility(View.GONE);

                                    holder.bottom_icon.setImageResource(R.drawable.bottom);
                                    holder.bottom_icon.setTag("0");
                                    holder.bottom_textview.setText("Bottom");
                                    holder.greypinImage.setVisibility(View.GONE);

                                }


                            } else {

                                holder.redpinImage.setVisibility(View.GONE);
                                holder.greypinImage.setVisibility(View.GONE);

                                holder.pin_icon.setImageResource(R.drawable.pin);
                                holder.pin_icon.setTag("0");
                                holder.pin_textview.setText("pin");
                                holder.redpinImage.setVisibility(View.GONE);

                                holder.bottom_icon.setImageResource(R.drawable.bottom);
                                holder.bottom_icon.setTag("0");
                                holder.bottom_textview.setText("Bottom");
                                holder.greypinImage.setVisibility(View.GONE);
                            }

                        } else {

                            holder.redpinImage.setVisibility(View.GONE);
                            holder.greypinImage.setVisibility(View.GONE);

                            holder.pin_icon.setImageResource(R.drawable.pin);
                            holder.pin_icon.setTag("0");
                            holder.pin_textview.setText("pin");
                            holder.redpinImage.setVisibility(View.GONE);

                            holder.bottom_icon.setImageResource(R.drawable.bottom);
                            holder.bottom_icon.setTag("0");
                            holder.bottom_textview.setText("Bottom");
                            holder.greypinImage.setVisibility(View.GONE);
                        }

                    } else {


                        holder.redpinImage.setVisibility(View.GONE);
                        holder.greypinImage.setVisibility(View.GONE);

                        holder.pin_icon.setImageResource(R.drawable.pin);
                        holder.pin_icon.setTag("0");
                        holder.pin_textview.setText("pin");
                        holder.redpinImage.setVisibility(View.GONE);

                        holder.bottom_icon.setImageResource(R.drawable.bottom);
                        holder.bottom_icon.setTag("0");
                        holder.bottom_textview.setText("Bottom");
                        holder.greypinImage.setVisibility(View.GONE);
                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.employee_id.setText(Html.fromHtml("<font color='#555555'><i> " + model.getEmpId() + "</i></font>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.employee_id.setText(Html.fromHtml("<font color='#555555'><i>  " + model.getEmpId() + "</i></font>"));
                    }
                    if (model.getDisplayName() != null && !model.getDisplayName().trim().isEmpty() && !model.getDisplayName().trim().equals("null")) {
                        String text = model.getName();
                        holder.employee_name.setText(text);
                    } else if (model.getName() != null && !model.getName().trim().isEmpty() && !model.getName().trim().equals("null")) {
                        String text = model.getName();
                        holder.employee_name.setText(text);
                    } else {
                        holder.employee_name.setText("");
                    }
                    if (model.getUserAvatar() != null && !model.getUserAvatar().isEmpty()) {
                        RequestOptions options = new RequestOptions()
                                .circleCropTransform()
                                .error(R.drawable.avatar_placeholder_light)
                                .priority(Priority.HIGH);
                        Glide.with(context).load(MyApplication.AWS_BASE_URL + model.getUserAvatar())
                                .apply(options).into(holder.employee_profile_pic);
                    } else {
                        Glide.with(context).load(R.drawable.avatar_placeholder_light).into(holder.employee_profile_pic);
                    }

                    holder.singleitem_parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tab_position == Constants.Tabclick.ALL || tab_position == Constants.Tabclick.TEAM) {
                                Attendance model1 = data_arrayList.get(position);
                                String emp_user_id = model1.getUserId();
                                String emp_user_name = model1.getName();
                                String emp_image_path = model1.getUserAvatar();
                                Intent intent1 = new Intent(context, NewAttendanceActivity.class);
                                intent1.putExtra("employee_user_id", emp_user_id);
                                intent1.putExtra("employee_user_name", emp_user_name);
                                intent1.putExtra("employee_image_path", emp_image_path);
                                intent1.putExtra("filter_type", tab_position);
                                intent1.putExtra("type", "1");
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                                if (activity_class.equals("employee")) {
                                    Activity activity = (Activity) context;
                                    activity.finish();
                                }



                            }
                        }
                    });


                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            //Remove swiped item from list and notify the RecyclerView

                        }
                    };

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);


                }


                String checkin = Utilities.getAttendenceTime(model.getCheckIn());
                String checkout = Utilities.getAttendenceTime(model.getCheckOut());


                if (Utilities.timeComparision(model.getShiftStartTime(), model.getCheckIn(), Constants.Static.CHECK_IN) && !checkin.trim().equals("N/A")) {
                    holder.under_line_view1.setVisibility(View.VISIBLE);
                } else {
                    holder.under_line_view1.setVisibility(View.INVISIBLE);
                }
                boolean is_true = Utilities.workhoursComparision(model.getShiftWorkingHours(), model.getWorkingHours());
                if (Utilities.timeComparision(model.getShiftEndTime(), model.getCheckOut(), Constants.Static.CHECK_OUT) && !checkout.trim().equals("N/A")) {
                    if (is_true) {
                        holder.under_line_view2.setVisibility(View.VISIBLE);
                    } else {
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                    }

                } else {
                    holder.under_line_view2.setVisibility(View.INVISIBLE);
                /*if (is_true && !checkout.trim().equals("N/A")) {
                    holder.under_line_view2.setVisibility(View.VISIBLE);
                } else {
                    holder.under_line_view2.setVisibility(View.INVISIBLE);
                }*/
                }

          /*  if (Utilities.timeComparision(model.getShift_end_time(), model.getCheck_out(), Constants.Static.CHECK_OUT) && !checkout.trim().equals("N/A") && is_true) {
                holder.under_line_view2.setVisibility(View.VISIBLE);
            } else {
                if (is_true && !checkout.trim().equals("N/A")) {
                    holder.under_line_view2.setVisibility(View.VISIBLE);
                } else {
                    holder.under_line_view2.setVisibility(View.INVISIBLE);
                }
            }*/
                holder.check_in_time.setText(checkin.trim());
                holder.check_out_time.setText(checkout.trim());
                holder.check_in_time.setVisibility(View.VISIBLE);
                holder.check_out_time.setVisibility(View.VISIBLE);
                holder._underscore.setVisibility(View.VISIBLE);


                try {
                    if (model.getCheckIn() != null && model.getCheckOut() != null && !model.getCheckIn().equals("0000-00-00 00:00:00") && !model.getCheckOut().equals("0000-00-00 00:00:00")) {


                        String check_in_date = model.getCheckIn().substring(0, 10);
                        String check_out_date = model.getCheckOut().substring(0, 10);


                        if (!check_in_date.equals(check_out_date)) {
                            try {
                                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(check_out_date);
                                String that_day = new SimpleDateFormat("dd MMM yyyy").format(d);
                                String[] strings = that_day.split(" ");
                                String day = strings[0];
                                String mon = strings[1];
                                String yea = strings[2];
                                String complete_day = day + " " + mon + "' " + yea;
                                holder.checkOutDate.setVisibility(View.VISIBLE);
                                holder.checkOutDate1.setVisibility(View.VISIBLE);

                                holder.checkOutDate.setText("(" + complete_day + ")");
                                holder.checkOutDate1.setText("");
                                complete_day = "";


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            holder.checkOutDate.setVisibility(View.GONE);
                            holder.checkOutDate1.setVisibility(View.GONE);
                        }


                    } else {
                        holder.checkOutDate.setVisibility(View.GONE);
                        holder.checkOutDate1.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                String db_remarks = model.getDb_remarks().trim();
                remarks_array = model.getRemarks();
                if (db_remarks != null && !db_remarks.trim().isEmpty() && !db_remarks.trim().equals("null")) {
                    setTheData(model, holder);
                    holder.status.setText(model.getDb_remarks().toString().trim().replace("[", "").replace("]", ""));
                    holder.status.setVisibility(View.VISIBLE);
                    holder.checked_image.setVisibility(View.GONE);
                } else if (remarks_array != null && !remarks_array.trim().isEmpty() && !remarks_array.trim().equals("null")) {
                    setTheData(model, holder);
                    holder.status.setText(model.getRemarks().toString().trim().replace("[", "").replace("]", ""));
                    holder.status.setVisibility(View.VISIBLE);
                    holder.checked_image.setVisibility(View.GONE);
                } else {
                    holder.status.setVisibility(View.GONE);
                    setTheData(model, holder);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            holder.sample_swipe.setShowMode(SwipeLayout.ShowMode.LayDown);


            holder.pin_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.sample_swipe.close();
                    mItemManger.bindView(holder.itemView, position);

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            String tag = "";
                            tag = holder.pin_icon.getTag().toString();
                            if (tag != null && tag.equals("0")) {

                                if (Utilities.isNetworkAvailable(context)) {
                                    pin(data_arrayList.get(position).getUserId(), "1", position);
                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                }

                            } else {

                                if (Utilities.isNetworkAvailable(context)) {
                                    unpin(data_arrayList.get(position).getUserId(), "0", position);

                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                }


                            }

                        }
                    }, 500);


                }
            });


            holder.sample_swipe.setClickToClose(true);

            holder.bottom_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.sample_swipe.close();
                    mItemManger.bindView(holder.itemView, position);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            String tag = "";
                            tag = holder.bottom_icon.getTag().toString();
                            if (tag != null && tag.equals("0")) {

                                if (Utilities.isNetworkAvailable(context)) {
                                    pin(data_arrayList.get(position).getUserId(), "2", position);

                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                }

                            } else {


                                if (Utilities.isNetworkAvailable(context)) {
                                    unpin(data_arrayList.get(position).getUserId(), "0", position);

                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                    }, 500);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        mItemManger.bindView(holder.itemView, position);

    }

    private void setTheData(Attendance model, ViewHolder holder) {
        try {
            if (!model.getCheckIn().contains("0000-00-00") && !model.getCheckOut().contains("0000-00-00")) {

                holder.checked_image.setVisibility(View.VISIBLE);

                if (!model.getWorkingHours().contains("00:00:00")) {
                    String[] split = model.getWorkingHours().split(":");
                    String hours = split[0];
                    String minutes = split[1];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.work_time.setText(Html.fromHtml("<font color='#555555'><b> " + hours + "</b><i>hr </i><b>" + minutes + "</b><i>min</i></font>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.work_time.setText(Html.fromHtml("<font color='#555555'><b>  " + hours + "</b><i>hr </i><b>" + minutes + "</b><i>min</i></font>"));
                    }
                    holder.work_time.setVisibility(View.VISIBLE);
                } else {
                    holder.work_time.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        holder.work_time.setText(Html.fromHtml("<font color='#555555'>N/A</font>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.work_time.setText(Html.fromHtml("<font color='#555555'>N/A</font>"));
                    }
                    holder.check_in_time.setVisibility(View.VISIBLE);
                    holder.check_out_time.setVisibility(View.VISIBLE);
                }
                holder.check_in_time.setVisibility(View.VISIBLE);
                holder.check_out_time.setVisibility(View.VISIBLE);
            } else {
                getTheStatus(model.getIsHoliday(), holder, model);
                holder.checked_image.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTheTabPosition(int team_all) {
        tab_position = team_all;
    }

    private void getTheStatus(Integer isHoliday, ViewHolder holder, Attendance model) {
        try {
            switch (isHoliday) {
                case Constants.HOLIDAY_TYPE_NONE:
                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {

                        holder.work_time.setText("ABSENT");
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {
                        holder.work_time.setText("N/A");
                    }
                    holder.check_in_time.setVisibility(View.VISIBLE);
                    holder.check_out_time.setVisibility(View.VISIBLE);
                    holder._underscore.setVisibility(View.VISIBLE);
                    holder.status.setVisibility(View.INVISIBLE);
                    //
                    break;
                case Constants.HOLIDAY_TYPE_WEEKOFF:
                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {


                        holder.work_time.setText("WEEK OFF");
                        holder.under_line_view1.setVisibility(View.INVISIBLE);
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                        holder.check_in_time.setVisibility(View.INVISIBLE);
                        holder.check_out_time.setVisibility(View.INVISIBLE);
                        holder._underscore.setVisibility(View.GONE);
                        holder.status.setVisibility(View.INVISIBLE);
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {

                        holder.work_time.setText("N/A");

                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.check_in_time.setVisibility(View.VISIBLE);
                        holder.check_out_time.setVisibility(View.VISIBLE);
                        holder._underscore.setVisibility(View.VISIBLE);
                        holder.checked_image.setVisibility(View.INVISIBLE);
                        holder.status.setVisibility(View.INVISIBLE);


                    }
                    break;
                case Constants.HOLIDAY_TYPE_CONFIRM:
                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {
                        holder.work_time.setText("CONFIRM HOLIDAY");
                        holder.under_line_view1.setVisibility(View.INVISIBLE);
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                        holder.check_in_time.setVisibility(View.INVISIBLE);
                        holder.check_out_time.setVisibility(View.INVISIBLE);
                        holder._underscore.setVisibility(View.GONE);
                        holder.status.setVisibility(View.INVISIBLE);
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {

                        holder.work_time.setText("N/A");

                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.check_in_time.setVisibility(View.VISIBLE);
                        holder.check_out_time.setVisibility(View.VISIBLE);
                        holder._underscore.setVisibility(View.VISIBLE);
                        holder.checked_image.setVisibility(View.INVISIBLE);
                        holder.status.setVisibility(View.INVISIBLE);

                    }
                    break;
                case Constants.HOLIDAY_TYPE_OPTIONAL:
                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {
                        holder.work_time.setText("OPTIONAL HOLIDAY");
                        holder.under_line_view1.setVisibility(View.INVISIBLE);
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                        holder.check_in_time.setVisibility(View.INVISIBLE);
                        holder.check_out_time.setVisibility(View.INVISIBLE);
                        holder._underscore.setVisibility(View.GONE);
                        holder.status.setVisibility(View.INVISIBLE);
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {

                        holder.work_time.setText("N/A");

                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.check_in_time.setVisibility(View.VISIBLE);
                        holder.check_out_time.setVisibility(View.VISIBLE);
                        holder._underscore.setVisibility(View.VISIBLE);
                        holder.checked_image.setVisibility(View.INVISIBLE);
                        holder.status.setVisibility(View.INVISIBLE);


                    }
                    break;

                case Constants.HOLIDAY_TYPE_LEAVE:


                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {
                        holder.work_time.setText("LEAVE");
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.under_line_view1.setVisibility(View.INVISIBLE);
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                        holder.check_in_time.setVisibility(View.INVISIBLE);
                        holder.check_out_time.setVisibility(View.INVISIBLE);
                        holder._underscore.setVisibility(View.GONE);
                        holder.status.setVisibility(View.INVISIBLE);

                    } else {

                        holder.work_time.setText("N/A");

                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.check_in_time.setVisibility(View.VISIBLE);
                        holder.check_out_time.setVisibility(View.VISIBLE);
                        holder._underscore.setVisibility(View.VISIBLE);
                        holder.checked_image.setVisibility(View.INVISIBLE);
                        holder.status.setVisibility(View.INVISIBLE);


                    }
                    break;

                case Constants.HOLIDAY_TYPE_COMPOFF:

                    if (model.getCheckIn().trim().equals("0000-00-00 00:00:00")) {
                        holder.work_time.setText("COMP OFF");
                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.under_line_view1.setVisibility(View.INVISIBLE);
                        holder.under_line_view2.setVisibility(View.INVISIBLE);
                        holder.check_in_time.setVisibility(View.INVISIBLE);
                        holder.check_out_time.setVisibility(View.INVISIBLE);
                        holder._underscore.setVisibility(View.GONE);
                        holder.status.setVisibility(View.INVISIBLE);

                    } else {

                        holder.work_time.setText("N/A");

                        holder.work_time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        holder.check_in_time.setVisibility(View.VISIBLE);
                        holder.check_out_time.setVisibility(View.VISIBLE);
                        holder._underscore.setVisibility(View.VISIBLE);
                        holder.checked_image.setVisibility(View.INVISIBLE);
                        holder.status.setVisibility(View.INVISIBLE);


                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return data_arrayList.size();
    }


    public void setAdapterType(String self) {
        try {
            who = self;
            if (who.trim().equals("team")) {
                is_self = false;
            } else {
                is_self = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void search(String searchQuery) {
        try {
            if (data_arrayList != null && data_arrayList.size() > 0) {
                data_arrayList.clear();
                notifyDataSetChanged();
            }
            if (searchQuery.trim().length() > 0) {
                for (Attendance contact : allDataList) {
                    if (Helper.getInstance().stringMatchWithFirstWordofString(searchQuery.toLowerCase(), contact.getName().toLowerCase())) {
                        data_arrayList.add(contact);
                    }
                }
                if (team) {
                    sortList("1");
                } else {
                    sortList("2");
                }
            } else {
                data_arrayList.addAll(allDataList);
                if (team) {
                    sortList("1");
                } else {
                    sortList("2");
                }
            }
            if (data_arrayList.size() == 0) {

                data_arrayList.addAll(allDataList);
                if (team) {
                    sortList("1");
                } else {
                    sortList("2");
                }
            }
            notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
        team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
        admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);


    }

    private void pin(String pinUserId, final String pinStatus, final int position) {

        try {
            String type = "";

            if (team) {

                type = "1";
            } else {
                type = "2";
            }


            Call<PinUnpinResponse> call = apiService.pin(apiKey, userId, pinUserId, pinStatus, type);
            call.enqueue(new retrofit2.Callback<PinUnpinResponse>() {
                @Override
                public void onResponse(Call<PinUnpinResponse> call, Response<PinUnpinResponse> response) {
                    PinUnpinResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {


                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            if (pinStatus.equals("1")) {

                                Attendance attendance = data_arrayList.get(position);
                                String aid = attendance.getAttendance_id();
                                attendance.setIsPinned(pinStatus);
                                String date = getDate();
                                attendance.setPinnedDate(date);
                                if (team) {
                                    attendance.setPinType("1");
                                } else {
                                    attendance.setPinType("2");
                                }

                                data_arrayList.remove(position);
                                notifyDataSetChanged();
                                data_arrayList.add(0, attendance);
                                notifyDataSetChanged();





                        /*    if(allDataList!=null && allDataList.size()>0){
                                for(int i=0;i<allDataList.size();i++){
                                    if(allDataList.get(i).getAttendance_id()!=null && aid!=null && allDataList.get(i).getAttendance_id().equals(aid)){
                                         attendance = allDataList.get(position);
                                        attendance.setIsPinned(pinStatus);
                                         date=getDate();
                                        attendance.setPinnedDate(date);
                                        if(team){
                                            attendance.setPinType("1");
                                        }
                                        else
                                        {
                                            attendance.setPinType("2");
                                        }

                                        allDataList.remove(position);
                                        notifyDataSetChanged();
                                        allDataList.add(0, attendance);
                                    }
                                }
                            }*/

                            } else {
                                Attendance attendance = data_arrayList.get(position);
                                String aid = attendance.getAttendance_id();
                                attendance.setIsPinned(pinStatus);
                                String date = getDate();
                                attendance.setPinnedDate(date);
                                data_arrayList.remove(position);
                                if (team) {
                                    attendance.setPinType("1");
                                } else {
                                    attendance.setPinType("2");
                                }
                                notifyDataSetChanged();
                                data_arrayList.add(getItemCount(), attendance);

                                notifyDataSetChanged();


                            }


                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    } else {


                        Toast.makeText(context, context.getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<PinUnpinResponse> call, Throwable t) {


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void unpin(String pinUserId, final String pinStatus, final int position) {
        try {
            String type = "";

            if (team) {
                type = "1";
            } else {
                type = "2";
            }

            Call<PinUnpinResponse> call = apiService.unpin(apiKey, userId, pinUserId, pinStatus, type);
            call.enqueue(new retrofit2.Callback<PinUnpinResponse>() {
                @Override
                public void onResponse(Call<PinUnpinResponse> call, Response<PinUnpinResponse> response) {
                    PinUnpinResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {


                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            data_arrayList.get(position).setIsPinned(pinStatus);
                            Attendance attendance = data_arrayList.get(position);
                            String aid = attendance.getAttendance_id();
                            attendance.setIsPinned(pinStatus);

                            if (team) {
                                sortList("1");
                            } else {
                                sortList("2");
                            }
                            notifyDataSetChanged();
                       /* if(allDataList!=null && allDataList.size()>0){
                            for(int i=0;i<allDataList.size();i++){
                                if(allDataList.get(i).getAttendance_id()!=null && aid!=null && allDataList.get(i).getAttendance_id().equals(aid)){
                                   allDataList.get(i).setIsPinned(pinStatus);
                                }
                            }
                        }*/

                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(context, context.getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();


                    }

                }

                @Override
                public void onFailure(Call<PinUnpinResponse> call, Throwable t) {


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sortList(String pinType) {

        try {

            List<Attendance> top_arrayList = new ArrayList<>();
            List<Attendance> bottom_arrayList = new ArrayList<>();
            List<Attendance> middle_arrayList = new ArrayList<>();

            if (data_arrayList != null && !data_arrayList.isEmpty() && data_arrayList.size() > 0) {
                int size = data_arrayList.size();

                String pinStatus = "", pin_type = "";
                for (int i = 0; i < size; i++) {


                    if (data_arrayList.get(i).getIsPinned() != null) {
                        pinStatus = data_arrayList.get(i).getIsPinned();
                        pin_type = data_arrayList.get(i).getPinType();

                        if (pinStatus.equals("1") && pin_type.equals(pinType)) {
                            top_arrayList.add(data_arrayList.get(i));
                        } else if (pinStatus.equals("2") && pin_type.equals(pinType)) {
                            bottom_arrayList.add(data_arrayList.get(i));
                        } else {
                            middle_arrayList.add(data_arrayList.get(i));
                        }


                    }


                }

                if (top_arrayList != null && !top_arrayList.isEmpty() && top_arrayList.size() > 0) {

                    Collections.sort(top_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o2.getPinnedDate()).compareTo(getRequiredDate2(o1.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }

                if (bottom_arrayList != null && !bottom_arrayList.isEmpty() && bottom_arrayList.size() > 0) {

                    Collections.sort(bottom_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o1.getPinnedDate()).compareTo(getRequiredDate2(o2.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }
                if (top_arrayList.size() > 0 || bottom_arrayList.size() > 0 || middle_arrayList.size() > 0) {
                    data_arrayList.clear();

                }

                if (top_arrayList.size() > 0) {
                    for (int i = 0; i < top_arrayList.size(); i++) {
                        data_arrayList.add(top_arrayList.get(i));

                    }
                }
                if (middle_arrayList.size() > 0) {
                    for (int i = 0; i < middle_arrayList.size(); i++) {
                        data_arrayList.add(middle_arrayList.get(i));

                    }
                }
                if (bottom_arrayList.size() > 0) {
                    for (int i = 0; i < bottom_arrayList.size(); i++) {
                        data_arrayList.add(bottom_arrayList.get(i));

                    }
                }


            }
            if (data_arrayList != null && !data_arrayList.isEmpty() && data_arrayList.size() > 0) {
                int size = data_arrayList.size();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDate() {

        try {

            Calendar c = Calendar.getInstance();
            return sdf1.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Date getRequiredDate2(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView check_in_time, check_out_time, employee_name, employee_id, work_time, status, _underscore, checkOutDate, checkOutDate1;
        ImageView employee_profile_pic, checked_image;
        View under_line_view1, under_line_view2, under_line_item;
        LinearLayout singleitem_parent, ll_left_view;
        LinearLayout main_lay;
        SwipeLayout sample_swipe;
        RelativeLayout pin_view, bottom_view;
        ImageView pin_icon, bottom_icon, redpinImage, greypinImage;
        TextView pin_textview, bottom_textview;

        public ViewHolder(View itemView) {
            super(itemView);
            check_in_time = (TextView) itemView.findViewById(R.id.check_in_time);
            checkOutDate = (TextView) itemView.findViewById(R.id.checkOutDate);
            checkOutDate1 = (TextView) itemView.findViewById(R.id.checkOutDate1);
            check_out_time = (TextView) itemView.findViewById(R.id.check_out_time);
            employee_name = (TextView) itemView.findViewById(R.id.employee_name);
            employee_id = (TextView) itemView.findViewById(R.id.employee_id);
            work_time = (TextView) itemView.findViewById(R.id.work_time);
            status = (TextView) itemView.findViewById(R.id.status);
            redpinImage = itemView.findViewById(R.id.redpinImage);
            greypinImage = itemView.findViewById(R.id.greypinImage);
            checked_image = (ImageView) itemView.findViewById(R.id.checked_image);
            employee_profile_pic = (ImageView) itemView.findViewById(R.id.employee_profile_pic);
            singleitem_parent = (LinearLayout) itemView.findViewById(R.id.singleitem_parent);
            under_line_view1 = (View) itemView.findViewById(R.id.under_line_view1);
            under_line_view2 = (View) itemView.findViewById(R.id.under_line_view2);
            under_line_item = itemView.findViewById(R.id.under_line_item);
            _underscore = (TextView) itemView.findViewById(R.id._underscore);
            main_lay = itemView.findViewById(R.id.main_lay);
            sample_swipe = (SwipeLayout) itemView.findViewById(R.id.sample_swipe);
            pin_view = sample_swipe.findViewById(R.id.pin_view);
            bottom_view = sample_swipe.findViewById(R.id.bottom_view);
            pin_icon = sample_swipe.findViewById(R.id.pin_icon);
            bottom_icon = sample_swipe.findViewById(R.id.bottom_icon);
            pin_textview = sample_swipe.findViewById(R.id.pin_textview);
            bottom_textview = sample_swipe.findViewById(R.id.bottom_textview);
            ll_left_view = (LinearLayout) sample_swipe.findViewById(R.id.ll_left_view);
        }
    }

}
