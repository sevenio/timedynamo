package com.tvisha.trooptime.activity.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.apiPostModels.Comment;
import com.tvisha.trooptime.activity.activity.apiPostModels.PermissionData;
import com.tvisha.trooptime.activity.activity.AttendanceFragment;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.TeamRequestDetailsActivity;
import com.tvisha.trooptime.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PermissionSummaryAdapter extends RecyclerView.Adapter<PermissionSummaryAdapter.ViewHolder> {
    private static String fromDate = "", toDate = "", userAvatar = "", userId = "";
    String dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    AttendanceFragment attendanceFragment;
    private List<PermissionData> items = new ArrayList<>();
    private Context context;

    public PermissionSummaryAdapter(Context context, List<PermissionData> items, AttendanceFragment attendanceFragment, String startDate, String endDate, String userAvatar, String userId) {
        this.items = items;
        this.context = context;
        this.attendanceFragment = attendanceFragment;
        this.fromDate = startDate;
        this.toDate = endDate;
        this.userAvatar = userAvatar;
        this.userId = userId;

    }

    @Override
    public PermissionSummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.permissions_summary_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PermissionSummaryAdapter.ViewHolder viewHolder, final int i) {
        try {

            String f = "", t = "";
            if (i == getItemCount() - 1) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }


            String request_type = items.get(i).getRequest_type();
            String from = items.get(i).getStart_date();
            String to = items.get(i).getEnd_date();
            String reqDt = items.get(i).getCreated_at();


            try {

                f = getDate(from);
                t = getDate(to);
                String rDt = getDate(reqDt);
                String curDate = getCurrentDateTime();
                viewHolder.fromdate.setText(f.substring(0, 6) + "' " + f.substring(7) + " - ");
                viewHolder.toDate.setText(t.substring(0, 6) + "' " + t.substring(7) + " ");

                if (curDate.equals(rDt)) {
                    viewHolder.requestedDate.setText(" Today");
                } else {
                    viewHolder.requestedDate.setText(" " + rDt.substring(0, 6) + "' " + rDt.substring(7) + " ");
                }

                if (Integer.parseInt(request_type) == Constants.REQUEST_LEAVE) {

                    viewHolder.permissionDate.setVisibility(View.GONE);

                } else {
                    viewHolder.permissionDate.setVisibility(View.VISIBLE);
                    viewHolder.permissionDate.setText(f.substring(0, 6) + "' " + f.substring(7) + " - ");

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Integer.parseInt(request_type) == Constants.REQUEST_LEAVE) {


                if (items.get(i).getIs_half_day().equals("1")) {

                    if (items.get(i).getDays_count() != null && !items.get(i).getDays_count().trim().isEmpty() && items.get(i).getDays_count().equals("Half Day")) {
                        viewHolder.halfDay.setVisibility(View.VISIBLE);

                        viewHolder.halfDay.setText("Half Day - ");
                    } else {
                        viewHolder.halfDay.setVisibility(View.GONE);
                        viewHolder.halfDay.setText("");
                    }
            /*    if(f!=null && !f.trim().isEmpty() && f.length()>=7)
                {
                    viewHolder.fromdate.setText(f.substring(0, 6) + "' " + f.substring(7) );
                }
                viewHolder.toDate.setText("");*/
                } else {
                    viewHolder.halfDay.setVisibility(View.GONE);
                }
                String type = items.get(i).getType();
                if (type != null && !type.trim().isEmpty()) {
                    if (Integer.parseInt(type) == Constants.LeaveTypes.CasualLeave) {
                        viewHolder.subRequestType.setText("Casual Leave - ");
                    } else if (Integer.parseInt(type) == Constants.LeaveTypes.SickLeave) {
                        viewHolder.subRequestType.setText("Sick Leave - ");
                    } else if (Integer.parseInt(type) == Constants.LeaveTypes.OptionalHoliday) {
                        // viewHolder.subRequestType.setText("Optional Holiday - ");
                        viewHolder.subRequestType.setText("Optional Leave - ");
                    }
                } else {
                    viewHolder.subRequestType.setText("");
                }

            } else if (Integer.parseInt(request_type) == Constants.REQUEST_PERMISSION) {


                String type = items.get(i).getType();
                if (items.get(i).getData() != null) {
                    if (items.get(i).getData().getStart_time() != null && !items.get(i).getData().getStart_time().trim().isEmpty()) {
                        if (Integer.parseInt(type) == Constants.PERMISSION_TYPE_OTHERS) {
                            if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty()) {
                                String startTime = items.get(i).getData().getStart_time();
                                if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                                    viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "am - ");
                                } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                                    viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "pm - ");
                                } else {
                                    int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                                    viewHolder.fromdate.setText(remain + items.get(i).getData().getStart_time().substring(2) + "pm - ");
                                }

                            } else {
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time());

                                String startTime = items.get(i).getData().getStart_time();
                                if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                                    viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "am ");
                                } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                                    viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "pm ");
                                } else {
                                    int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                                    viewHolder.fromdate.setText(remain + items.get(i).getData().getStart_time().substring(2) + "pm ");
                                }
                            }

                        } else {
                            viewHolder.fromdate.setText(items.get(i).getData().getStart_time());
                            String startTime = items.get(i).getData().getStart_time();
                            if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "am ");
                            } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + "pm ");
                            } else {
                                int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                                viewHolder.fromdate.setText(remain + items.get(i).getData().getStart_time().substring(2) + "pm ");
                            }
                        }

                    } else {
                        viewHolder.fromdate.setText("");
                    }
                    if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty()) {
                        viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + " ");

                        String endTime = items.get(i).getData().getEnd_time();
                        if (Integer.parseInt(endTime.substring(0, 2)) < 12) {
                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + "am ");
                        } else if (Integer.parseInt(endTime.substring(0, 2)) == 12) {

                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + "pm ");
                        } else {
                            int remain = Integer.parseInt(endTime.substring(0, 2)) - 12;
                            viewHolder.toDate.setText(remain + items.get(i).getData().getEnd_time().substring(2) + "pm ");
                        }
                    } else {
                        viewHolder.toDate.setText("");
                    }
                }


                if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_EARLY_LOGOUT) {
                    viewHolder.subRequestType.setText("Early logout - ");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_LATE_LOGIN) {
                    viewHolder.subRequestType.setText("Late login - ");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_TYPE_OTHERS) {
                    viewHolder.subRequestType.setText("Other -");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_EXTENDED_LOGOUT) {
                    viewHolder.subRequestType.setText("Extended Logout -");
                }
                else
                {
                    viewHolder.subRequestType.setText("");
                }
            }


            String status = items.get(i).getStatus();


            viewHolder.statusImage.setVisibility(View.VISIBLE);


            if (Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                viewHolder.actionBy.setVisibility(View.GONE);
                viewHolder.statusImage.setImageResource(R.drawable.pending);
            } else if (Integer.parseInt(status) == Constants.REQUEST_STATUS_APPROVED) {

                viewHolder.statusImage.setImageResource(R.drawable.accpeted);
                viewHolder.actionBy.setVisibility(View.VISIBLE);
                viewHolder.actionBy.setText("Accepted by : "+items.get(i).getAccepted_by());
            } else {

                viewHolder.statusImage.setImageResource(R.drawable.reject);
                viewHolder.actionBy.setVisibility(View.VISIBLE);
                viewHolder.actionBy.setText("Rejected by : "+items.get(i).getAccepted_by());
            }

            String reason = items.get(i).getReason();
            if (reason != null && !reason.trim().isEmpty()) {
                viewHolder.message.setVisibility(View.VISIBLE);
                if (reason.length() > 100) {
                    viewHolder.message.setText(reason.substring(0, 40) + "....");
                } else {
                    viewHolder.message.setText(reason);
                }
            } else {
                viewHolder.message.setVisibility(View.GONE);
            }


            final String finalF = f;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String type = "";

                    String request_type = items.get(i).getRequest_type();
                    if (Integer.parseInt(request_type) == Constants.REQUEST_LEAVE) {
                        type = "Leave";
                    } else if (Integer.parseInt(request_type) == Constants.REQUEST_PERMISSION) {
                        type = "Permission";
                    } else if (Integer.parseInt(request_type) == Constants.REQUEST_SWAP) {
                        type = "Swap";
                    } else if (Integer.parseInt(request_type) == Constants.REQUEST_COMPOFF) {
                        type = "Comp Off";
                    } else if (Integer.parseInt(request_type) == Constants.REQUEST_OVERTIME) {
                        type = "Over Time";
                    } else {
                        type = "";
                    }

                    Intent intent = new Intent(context, TeamRequestDetailsActivity.class);
                    intent.putExtra("employeeName", items.get(i).getRequest_by_name());
                    intent.putExtra("requestTypeName", type);
                    intent.putExtra("requestType", request_type);
                    intent.putExtra("subRequestType", viewHolder.subRequestType.getText().toString());
                    if (Integer.parseInt(request_type) == Constants.REQUEST_PERMISSION) {
                        intent.putExtra("permissionDate", finalF.substring(0, 6) + "' " + finalF.substring(7));
                    } else {
                        intent.putExtra("permissionDate", "");
                    }
                    intent.putExtra("fromDate", viewHolder.fromdate.getText().toString());
                    intent.putExtra("toDate", viewHolder.toDate.getText().toString());
                    intent.putExtra("startDate", fromDate);
                    intent.putExtra("endDate", toDate);
                    intent.putExtra("message", items.get(i).getReason());
                    // intent.putExtra("userAvtar",items.get(i).getRequest_by_user_avatar());
                    intent.putExtra("userAvtar", userAvatar);
                    intent.putExtra("id", items.get(i).getId());
                    intent.putExtra("type", items.get(i).getType());
                    intent.putExtra("status", items.get(i).getStatus());
                    intent.putExtra("requestDate", viewHolder.requestedDate.getText());
                    intent.putExtra("status", items.get(i).getStatus());
                    // intent.putExtra("swapStatus",items.get(i).getSwap_status());
                    intent.putExtra("swapStatus", "");
                    intent.putExtra("daysCount", items.get(i).getDays_count());
                    intent.putExtra("acceptedBy", items.get(i).getAccepted_by());

                    //  ArrayList<Comment> comments= (ArrayList<Comment>) items.get(i).getComments();
                    ArrayList<Comment> comments = new ArrayList<>();
                    intent.putParcelableArrayListExtra("comments", (ArrayList<? extends Parcelable>) comments);
                    // intent.putExtra("swapWithName",items.get(i).getSwap_to_name());
                    intent.putExtra("swapWithName", "");
                    intent.putExtra("userId", userId);
                    intent.putExtra("reportingUsers", items.get(i).getRequest_to());
                    if (Integer.parseInt(request_type) == Constants.REQUEST_OVERTIME) {

                        intent.putExtra("ot_duration", items.get(i).getData().getOt_duration());

                    } else {
                        intent.putExtra("ot_duration", "");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public void setList(List<PermissionData> selfRequestArrayList) {

        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(selfRequestArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getCurrentDateTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            return sdf.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        private TextView subRequestType, fromdate, toDate, requestedDate, message, halfDay, permissionDate,actionBy;
        private ImageView statusImage;
        private View line;

        public ViewHolder(View view) {
            super(view);

            statusImage = view.findViewById(R.id.statusImg);
            subRequestType = view.findViewById(R.id.subRequestType);
            fromdate = view.findViewById(R.id.from);
            message = view.findViewById(R.id.message);
            requestedDate = view.findViewById(R.id.requestedDate);
            toDate = view.findViewById(R.id.to);
            line = view.findViewById(R.id.line);
            layout = view.findViewById(R.id.rr_layout);
            halfDay = view.findViewById(R.id.halfDay);
            permissionDate = view.findViewById(R.id.permissionDate);
            actionBy = view.findViewById(R.id.actionBy);


        }
    }
}

