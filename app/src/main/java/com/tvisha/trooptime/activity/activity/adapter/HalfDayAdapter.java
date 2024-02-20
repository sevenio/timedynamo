package com.tvisha.trooptime.activity.activity.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HalfDayAdapter extends RecyclerView.Adapter<HalfDayAdapter.ViewHolder> {
    Context context;
    int viewCount = 0;
    JSONObject object = new JSONObject();
    List<String> stringList = new ArrayList<>();
    ArrayAdapter<CharSequence> leaveTypeAdapter;
    private int count;

    public HalfDayAdapter(Context context, int view_count, List<String> strings) {
        viewCount = view_count;
        this.context = context;
        stringList = strings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.halfday_request_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.leave_date.setText(stringList.get(position));
            object.put(stringList.get(position), Constants.FULL_DAY);

            leaveTypeAdapter = ArrayAdapter.createFromResource(context, R.array.half_day_array, R.layout.item_spiner);
            holder.reuest_spinner.setAdapter(leaveTypeAdapter);
            holder.reuest_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    try {
                        if (pos != -1) {
                            if (pos == 0) {
                                object.put(stringList.get(position), Constants.FULL_DAY);
                            } else if (pos == 1) {
                                object.put(stringList.get(position), Constants.FIRST_HALF_DAY);
                            } else if (pos == 2) {
                                object.put(stringList.get(position), Constants.SECOND_HALF_DAY);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject getObject() {
        return object;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void setCount(int count) {
        viewCount = count;
    }

    public void setList(List<String> dateStringList) {
        try {
            if (stringList != null && stringList.size() > 0) {
                stringList.clear();
            }
            if (object != null) {
                object = null;
            }
            object = new JSONObject();
            stringList.addAll(dateStringList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView leave_date;
        Spinner reuest_spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            leave_date = (TextView) itemView.findViewById(R.id.leave_date);
            reuest_spinner = (Spinner) itemView.findViewById(R.id.reuest_spinner);
        }
    }
}
