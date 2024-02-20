package com.tvisha.trooptime.activity.activity.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.tvisha.trooptime.activity.activity.apiPostModels.Comment;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfRequest;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.RequestActivity;
import com.tvisha.trooptime.activity.activity.RoundishImageView;
import com.tvisha.trooptime.activity.activity.TeamRequestDetailsActivity;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TeamRequestAdapter extends RecyclerView.Adapter<TeamRequestAdapter.ViewHolder> {
    private static String fromDate = "", toDate = "";
    String dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    String type = "";
    private List<SelfRequest> items = new ArrayList<>();
    private Context context;

    public TeamRequestAdapter(Context context, List<SelfRequest> items, String fromDate, String toDate) {
        this.items = items;
        this.context = context;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    @Override
    public TeamRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_request_row_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TeamRequestAdapter.ViewHolder viewHolder, final int i) {


        try {




            String request_type = items.get(i).getRequest_type();

            String f = "", t = "";
            if (i == getItemCount() - 1) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }

            viewHolder.subRequestType.setVisibility(View.GONE);
            viewHolder.requestType.setVisibility(View.VISIBLE);
            viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
            viewHolder.permissionDate.setVisibility(View.GONE);
            viewHolder.arrow1.setVisibility(View.GONE);

            viewHolder.employeeName.setText(Helper.getInstance().capitalize(items.get(i).getRequest_by_name()));
            String reason = items.get(i).getReason();
            if (reason != null && !reason.trim().isEmpty()) {
                viewHolder.message.setVisibility(View.VISIBLE);
                if (reason.length() > 100) {
                    viewHolder.message.setText(reason.substring(0, 100) + "...");
                } else {
                    viewHolder.message.setText(items.get(i).getReason());
                }
            } else {
                viewHolder.message.setVisibility(View.GONE);
            }


            String from = items.get(i).getStart_date();
            String to = items.get(i).getEnd_date();
            String reqDt = items.get(i).getCreated_at();

            String curDate = getCurrentDateTime();
            try {
                viewHolder.fromdate.setVisibility(View.VISIBLE);
                viewHolder.toDate.setVisibility(View.VISIBLE);
                viewHolder.requestedDate.setVisibility(View.VISIBLE);
                viewHolder.fromdate.setText(getDate(from) + " ");
                viewHolder.toDate.setText(" " + getDate(to));
                f = getDate(from);
                t = getDate(to);
                String rDt = getDate(reqDt);
                viewHolder.fromdate.setText(f.substring(0, 6) + "' " + f.substring(7) + " - ");
                viewHolder.permissionDate.setText(f.substring(0, 6) + "' " + f.substring(7) + " - ");
                viewHolder.toDate.setText(" " + t.substring(0, 6) + "' " + t.substring(7) + " ");
                if (curDate.equals(rDt)) {
                    viewHolder.requestedDate.setText(" Today");
                } else {
                    viewHolder.requestedDate.setText(" " + rDt.substring(0, 6) + "' " + rDt.substring(7) + " ");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Integer.parseInt(request_type) == Constants.REQUEST_LEAVE) {
                viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
                viewHolder.permissionDate.setVisibility(View.GONE);
                viewHolder.arrow1.setVisibility(View.GONE);

                viewHolder.requestType.setVisibility(View.VISIBLE);
                viewHolder.subRequestType.setVisibility(View.VISIBLE);
                viewHolder.requestType.setText("L");
                viewHolder.subRequestType.setText("");
                type = "Leave";


                if (items.get(i).getIs_half_day().equals("1")) {
                    if (items.get(i).getDays_count() != null && !items.get(i).getDays_count().trim().isEmpty() && items.get(i).getDays_count().equals("Half Day")) {
                        viewHolder.subRequestType.setVisibility(View.VISIBLE);

                        viewHolder.subRequestType.setText("Half Day - ");
                    } else {
                        viewHolder.subRequestType.setVisibility(View.GONE);
                        viewHolder.subRequestType.setText("");
                    }

                } else {
                    viewHolder.subRequestType.setText("");
                    viewHolder.subRequestType.setVisibility(View.GONE);
                }

            } else if (Integer.parseInt(request_type) == Constants.REQUEST_PERMISSION) {
                viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
                viewHolder.permissionDate.setVisibility(View.VISIBLE);
                viewHolder.arrow1.setVisibility(View.GONE);
                viewHolder.requestType.setVisibility(View.VISIBLE);
                viewHolder.subRequestType.setVisibility(View.GONE);

                viewHolder.requestType.setText("P");
                String type = items.get(i).getType();
                if (items.get(i).getData() != null) {
                    if (items.get(i).getData().getStart_time() != null && !items.get(i).getData().getStart_time().trim().isEmpty()) {
                        viewHolder.fromdate.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(type) == Constants.PERMISSION_TYPE_OTHERS || Integer.parseInt(type) == Constants.PERMISSION_REQUEST_EXTENDED_LOGOUT) {
                            if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty()) {
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + " - ");
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
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + " ");
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
                            viewHolder.fromdate.setText(items.get(i).getData().getStart_time() + " ");
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
                        viewHolder.fromdate.setVisibility(View.GONE);
                        viewHolder.fromdate.setText("");
                    }
                    if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty()) {
                        viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + " ");
                        String startTime = items.get(i).getData().getEnd_time();
                        if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + "am ");
                        } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time() + "pm ");
                        } else {
                            int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                            viewHolder.toDate.setText(remain + items.get(i).getData().getEnd_time().substring(2) + "pm ");
                        }
                    } else {
                        viewHolder.toDate.setText("");
                        viewHolder.toDate.setVisibility(View.GONE);

                    }
                }


                if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_EARLY_LOGOUT) {
                    viewHolder.subRequestType.setVisibility(View.VISIBLE);
                    viewHolder.subRequestType.setText("Early logout - ");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_LATE_LOGIN) {
                    viewHolder.subRequestType.setVisibility(View.VISIBLE);
                    viewHolder.subRequestType.setText("Late login - ");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_TYPE_OTHERS) {
                    viewHolder.subRequestType.setVisibility(View.VISIBLE);
                    viewHolder.subRequestType.setText("Other - ");
                } else if (Integer.parseInt(type) == Constants.PERMISSION_REQUEST_EXTENDED_LOGOUT) {
                    viewHolder.subRequestType.setVisibility(View.VISIBLE);
                    viewHolder.subRequestType.setText("Extended Logout -");
                } else {
                    viewHolder.subRequestType.setVisibility(View.GONE);
                    viewHolder.subRequestType.setText("");
                }
            } else if (Integer.parseInt(request_type) == Constants.REQUEST_COMPOFF) {
                viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
                viewHolder.permissionDate.setVisibility(View.GONE);
                viewHolder.arrow1.setVisibility(View.GONE);
                viewHolder.subRequestType.setVisibility(View.GONE);
                viewHolder.subRequestType.setText("");
                viewHolder.requestType.setVisibility(View.VISIBLE);
                viewHolder.requestType.setText("C");

                type = "Comp Off";

                String dt = items.get(i).getData().getDate();
                try {
                    dt = getDate(dt);
                    viewHolder.fromdate.setVisibility(View.VISIBLE);
                    viewHolder.fromdate.setText(dt.substring(0, 6) + "' " + dt.substring(7) + " - ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (Integer.parseInt(request_type) == Constants.REQUEST_SWAP) {
                viewHolder.subRequestType.setVisibility(View.GONE);
                viewHolder.subRequestType.setText("");
                viewHolder.permissionDate.setVisibility(View.GONE);
                viewHolder.requestType.setVisibility(View.VISIBLE);
                viewHolder.swapWithEmployeeName.setVisibility(View.VISIBLE);
                viewHolder.arrow1.setVisibility(View.VISIBLE);

                viewHolder.requestType.setText("S");
                viewHolder.employeeName.setText(Helper.getInstance().capitalize(items.get(i).getRequest_by_name()) + " ");
                viewHolder.swapWithEmployeeName.setText(" " + Helper.getInstance().capitalize(items.get(i).getSwap_to_name()));


            } else if (Integer.parseInt(request_type) == Constants.REQUEST_OVERTIME) {

                viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
                viewHolder.permissionDate.setVisibility(View.GONE);
                viewHolder.arrow1.setVisibility(View.GONE);
                viewHolder.subRequestType.setVisibility(View.GONE);
                viewHolder.subRequestType.setText("");
                viewHolder.requestType.setVisibility(View.VISIBLE);
                viewHolder.requestType.setText("OT");
                String type = items.get(i).getType();
                if (items.get(i).getData() != null) {
                    if (items.get(i).getData().getStart_time() != null && !items.get(i).getData().getStart_time().trim().isEmpty()) {
                        viewHolder.fromdate.setVisibility(View.VISIBLE);
                        if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty() && !items.get(i).getData().getEnd_time().trim().equals("00:00:00")) {
                            //00:00:00

                            viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + " - ");
                            String startTime = items.get(i).getData().getStart_time();
                            if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + "am - ");
                            } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + "pm - ");
                            } else {
                                int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                                viewHolder.fromdate.setText(remain + items.get(i).getData().getStart_time().substring(2, 5) + "pm - ");
                            }
                        } else {
                            viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + " ");
                            String startTime = items.get(i).getData().getStart_time();
                            if (Integer.parseInt(startTime.substring(0, 2)) < 12) {
                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + "am ");
                            } else if (Integer.parseInt(startTime.substring(0, 2)) == 12) {

                                viewHolder.fromdate.setText(items.get(i).getData().getStart_time().substring(0, 5) + "pm ");
                            } else {
                                int remain = Integer.parseInt(startTime.substring(0, 2)) - 12;
                                viewHolder.fromdate.setText(remain + items.get(i).getData().getStart_time().substring(2, 5) + "pm ");
                            }
                        }
                    } else {
                        viewHolder.fromdate.setVisibility(View.GONE);
                        viewHolder.fromdate.setText("");
                    }
                    if (items.get(i).getData().getEnd_time() != null && !items.get(i).getData().getEnd_time().trim().isEmpty() && !items.get(i).getData().getEnd_time().trim().equals("00:00:00")) {
                        viewHolder.toDate.setVisibility(View.VISIBLE);
                        viewHolder.toDate.setText(items.get(i).getData().getEnd_time().substring(0, 5) + " ");
                        String endTime = items.get(i).getData().getEnd_time();
                        if (Integer.parseInt(endTime.substring(0, 2)) < 12) {
                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time().substring(0, 5) + "am ");
                        } else if (Integer.parseInt(endTime.substring(0, 2)) == 12) {

                            viewHolder.toDate.setText(items.get(i).getData().getEnd_time().substring(0, 5) + "pm ");
                        } else {
                            int remain = Integer.parseInt(endTime.substring(0, 2)) - 12;
                            viewHolder.toDate.setText(remain + items.get(i).getData().getEnd_time().substring(2, 5) + "pm ");
                        }
                    } else {
                        viewHolder.toDate.setVisibility(View.GONE);
                        viewHolder.toDate.setText("");
                    }
                }


            } else {
                // viewHolder.requestType.setVisibility(View.GONE);
                viewHolder.subRequestType.setVisibility(View.GONE);
                viewHolder.swapWithEmployeeName.setVisibility(View.GONE);
                viewHolder.arrow1.setVisibility(View.GONE);
            }


            String status = items.get(i).getStatus();
            String swap_status = items.get(i).getSwap_status();


            if (Integer.parseInt(request_type) != Constants.REQUEST_SWAP) {
                if (Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                    viewHolder.status.setText("Pending");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.pending_color));
                    viewHolder.statusImage.setImageResource(R.drawable.pending);
                    viewHolder.actionBy.setVisibility(View.GONE);
                } else if (Integer.parseInt(status) == Constants.REQUEST_STATUS_APPROVED) {
                    viewHolder.status.setText("Accepted");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.accepted_color));
                    viewHolder.statusImage.setImageResource(R.drawable.accpeted);
                    viewHolder.actionBy.setVisibility(View.VISIBLE);
                    viewHolder.actionBy.setText("Accepted by : "+items.get(i).getAccepted_by());
                } else {
                    viewHolder.status.setText("Rejected");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.rejected_color));
                    viewHolder.statusImage.setImageResource(R.drawable.reject);
                    viewHolder.actionBy.setVisibility(View.VISIBLE);
                    viewHolder.actionBy.setText("Rejected by : "+items.get(i).getAccepted_by());
                }
            } else {
                if (Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING || Integer.parseInt(swap_status) == Constants.REQUEST_STATUS_PENDING) {
                    viewHolder.status.setText("Pending");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.pending_color));
                    viewHolder.statusImage.setImageResource(R.drawable.pending);
                    viewHolder.actionBy.setVisibility(View.GONE);
                } else if (Integer.parseInt(status) == Constants.REQUEST_STATUS_APPROVED && Integer.parseInt(swap_status) == Constants.REQUEST_STATUS_APPROVED) {
                    viewHolder.status.setText("Accepted");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.accepted_color));
                    viewHolder.statusImage.setImageResource(R.drawable.accpeted);
                    viewHolder.actionBy.setVisibility(View.VISIBLE);
                    viewHolder.actionBy.setText("Accepted by : "+items.get(i).getAccepted_by());
                } else {
                    viewHolder.status.setText("Rejected");
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.rejected_color));
                    viewHolder.statusImage.setImageResource(R.drawable.reject);
                    viewHolder.actionBy.setVisibility(View.VISIBLE);
                    viewHolder.actionBy.setText("Rejected by : "+items.get(i).getAccepted_by());
                }
            }


            final String finalF = f;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                    intent.putExtra("userAvtar", items.get(i).getRequest_by_user_avatar());
                    intent.putExtra("id", items.get(i).getId());
                    intent.putExtra("type", items.get(i).getType());
                    intent.putExtra("status", items.get(i).getStatus());
                    intent.putExtra("requestDate", viewHolder.requestedDate.getText());
                    intent.putExtra("status", items.get(i).getStatus());
                    intent.putExtra("swapStatus", items.get(i).getSwap_status());
                    intent.putExtra("daysCount", items.get(i).getDays_count());
                    intent.putExtra("acceptedBy", items.get(i).getAccepted_by());
                    ArrayList<Comment> comments = (ArrayList<Comment>) items.get(i).getComments();
                    intent.putParcelableArrayListExtra("comments", (ArrayList<? extends Parcelable>) comments);
                    intent.putExtra("swapWithName", items.get(i).getSwap_to_name());
                    intent.putExtra("userId", items.get(i).getRequest_by());
                    intent.putExtra("reportingUsers", items.get(i).getRequest_to());
                    if (Integer.parseInt(request_type) == Constants.REQUEST_OVERTIME) {

                        intent.putExtra("ot_duration", items.get(i).getData().getOt_duration());

                    } else {
                        intent.putExtra("ot_duration", "");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    if (context instanceof RequestActivity) {

                    }

                }
            });

            String user_avatar = items.get(i).getRequest_by_user_avatar();
            if (user_avatar != null && !user_avatar.trim().isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .error(R.drawable.avtar_placeholder_rectangle)
                        .priority(Priority.HIGH);
                Glide.with(context).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(options)
                        .into(viewHolder.profileImage);
            } else {
                Glide.with(context).load(R.drawable.avtar_placeholder_rectangle).into(viewHolder.profileImage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public void setList(List<SelfRequest> selfRequestArrayList) {
        try {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            items.addAll(selfRequestArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateRange(String fromDate, String toDate) {
        try {
            this.fromDate = fromDate;
            this.toDate = toDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            // c.add(Calendar.DATE, 1);
            Date nextDate = c.getTime();
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

        ImageView statusImage, arrow1;
        private TextView employeeName, message, subRequestType, fromdate, toDate, status, requestedDate,
                swapWithEmployeeName, permissionDate,actionBy;
        private RoundishImageView profileImage;
        private TextView requestType;
        private View line;

        public ViewHolder(View view) {
            super(view);

            employeeName = view.findViewById(R.id.employeeName);
            swapWithEmployeeName = view.findViewById(R.id.swapWithEmployeeName);
            message = view.findViewById(R.id.message);
            status = view.findViewById(R.id.status);
            arrow1 = view.findViewById(R.id.arrow1);
            requestType = view.findViewById(R.id.requestType);
            requestedDate = view.findViewById(R.id.requestedDate);
            subRequestType = view.findViewById(R.id.subRequestType);
            statusImage = view.findViewById(R.id.statusImg);
            fromdate = view.findViewById(R.id.from);
            toDate = view.findViewById(R.id.to);
            profileImage = view.findViewById(R.id.profileImage);
            line = view.findViewById(R.id.line);
            permissionDate = view.findViewById(R.id.permissionDate);
            actionBy = view.findViewById(R.id.actionBy);


        }
    }
}

