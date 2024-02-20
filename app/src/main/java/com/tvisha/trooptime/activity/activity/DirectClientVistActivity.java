package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;
import com.tvisha.trooptime.activity.activity.apiHelper.ApiHelper;
import com.tvisha.trooptime.activity.activity.dialog.TimePicker;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.ServerUrls;
import com.tvisha.trooptime.activity.activity.helper.ToastUtil;
import com.tvisha.trooptime.activity.activity.model.Person;
import com.tvisha.trooptime.activity.activity.customViews.ContactsCompletionView;
import com.tvisha.trooptime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectClientVistActivity extends AppCompatActivity implements View.OnClickListener {
    ContactsCompletionView toAddress, ccAddress, businessContact;
    EditText date, comment;
    TextView actionLable;
    RadioGroup effectType;

    ArrayAdapter<Person> toAddressAdapter, ccAddressAdapter, businessListAdapter;
    Person people[], businessList[];

    JSONObject apiObject;
    Runnable employeesListInterface = new Runnable() {
        @Override
        public void run() {
            storeEmployeesList(ApiHelper.getInstance().requestServer(DirectClientVistActivity.this, apiObject, ServerUrls.GET_EMPLOYEE_EMAIL_LIST));
        }
    };
    Runnable businessContactListInterface = new Runnable() {
        @Override
        public void run() {
            storeBusinessList(ApiHelper.getInstance().requestServer(DirectClientVistActivity.this, apiObject, ServerUrls.GET_BUSINESS_LIST));
        }
    };
    Runnable formInterface = new Runnable() {
        @Override
        public void run() {
            requestCompleted(ApiHelper.getInstance().requestServer(DirectClientVistActivity.this, apiObject, ServerUrls.CLIENT_VISIT));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_client_vist);
        getEmployeesList();
        getBusinessContactList();
        actionLable = (TextView) findViewById(R.id.actionLable);
        actionLable.setText("Direct Client Visit");

        effectType = (RadioGroup) findViewById(R.id.effectType);
        toAddress = (ContactsCompletionView) findViewById(R.id.toAddress);
        ccAddress = (ContactsCompletionView) findViewById(R.id.ccAddress);
        date = (EditText) findViewById(R.id.date);
        businessContact = (ContactsCompletionView) findViewById(R.id.businessContact);
        comment = (EditText) findViewById(R.id.comment);
        date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionBack:
                onBackPressed();
                break;
            case R.id.date:
                TimePicker.getInstance(TimePicker.TYPE_DATE, TimePicker.CALENDAR_TYPE_FEATURE, (TextView) v).show(getSupportFragmentManager(), "pic_date");
                break;
            case R.id.submitForm:
                if (validateForm()) {
                    ToastUtil.showToast(this, "Request sending..");
                    submitForm();
                    //resetForm();
                }
                break;
            case R.id.actionButton1:
                resetForm();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Helper.getInstance().detachProgress(this);
        super.onDestroy();
    }

    public boolean validateForm() {
        try {

            if (effectType.getCheckedRadioButtonId() == -1) {
                ToastUtil.showToast(this, "Select what will effect..!");
                return false;
            } else if (date.getText().toString().length() == 0) {
                date.requestFocus();
                ToastUtil.showToast(this, "Select Date..!");
                return false;
            } else if (businessContact.getText().toString().length() == 0) {
                businessContact.requestFocus();
                ToastUtil.showToast(this, "Select business contact..!");
                return false;
            } else if (toAddress.getObjects().size() == 0) {
                toAddress.requestFocus();
                ToastUtil.showToast(this, "Enter email to whom  u like to send..!");
                return false;
            } else if (comment.getText().toString().length() == 0) {
                comment.requestFocus();
                ToastUtil.showToast(this, "Enter business contact..!");
                return false;
            }

            apiObject = new JSONObject();
            String to = "";
            String toCopy = "";
            String cc = "";
            String businessContactSelectedList = "";
            for (int t = 0; t < toAddress.getObjects().size(); t++) {
                if (t == 0) {
                    to += toAddress.getObjects().get(t).getUserId();
                    toCopy += "," + toAddress.getObjects().get(t).getUserId() + ",";
                } else {
                    to += "," + toAddress.getObjects().get(t).getUserId();
                    toCopy += toAddress.getObjects().get(t).getUserId() + ",";
                }
            }
            for (int c = 0; c < ccAddress.getObjects().size(); c++) {
                if (toCopy.contains("," + ccAddress.getObjects().get(c).getUserId() + ",")) {
                    ToastUtil.showToast(this, "TO and CC contained duplicate values");
                    return false;
                } else {
                    if (c == 0) {
                        cc += ccAddress.getObjects().get(c).getUserId();
                    } else {
                        cc += "," + ccAddress.getObjects().get(c).getUserId();
                    }
                }
            }

            for (int b = 0; b < businessContact.getObjects().size(); b++) {
                if (b == 0) {
                    businessContactSelectedList += businessContact.getObjects().get(b).getUserId();
                } else {
                    businessContactSelectedList += "," + businessContact.getObjects().get(b).getUserId();
                }
            }

            apiObject.put("request_to", to);
            apiObject.put("cc_to", cc);
            apiObject.put("client_contact_id", businessContactSelectedList);
            apiObject.put("date", date.getText().toString());
            apiObject.put("comment", comment.getText().toString());
            if (effectType.getCheckedRadioButtonId() == R.id.checkIn) {
                apiObject.put("time_effect", 0);
            } else if (effectType.getCheckedRadioButtonId() == R.id.checkOut) {
                apiObject.put("time_effect", 1);
            } else if (effectType.getCheckedRadioButtonId() == R.id.totalDay) {
                apiObject.put("time_effect", 2);
            } else {
                ToastUtil.showToast(this, "Select which time will effect..!");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void resetForm() {
        try {
            comment.setText("");
            date.setText("");

            if (effectType.getCheckedRadioButtonId() != -1) {
                effectType.clearCheck();
            }

            if (toAddress.getObjects().size() > 0) {
                toAddress.clear();
            }
            if (ccAddress.getObjects().size() > 0) {
                ccAddress.clear();
            }
            if (businessContact.getObjects().size() > 0) {
                businessContact.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //services
    //to get employee list
    public void getEmployeesList() {
        Helper.getInstance().openProgress(this);
        Thread thread = new Thread(employeesListInterface);
        thread.start();
    }

    public void storeEmployeesList(final JSONObject object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (object != null && object.getBoolean("success")) {
                        JSONArray usersList = object.getJSONArray("employees");
                        if (usersList.length() > 0) {
                            people = new Person[usersList.length()];
                            for (int i = 0; i < usersList.length(); i++) {
                                JSONObject user = usersList.getJSONObject(i);
                                people[i] = new Person(user.getString("name"), user.getString("email"), user.getString("user_id"));
                            }
                        }
                        setEmployeesList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Helper.getInstance().closeProgress(DirectClientVistActivity.this);
            }
        });
    }

    public void setEmployeesList() {
        toAddressAdapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                convertView.setBackgroundColor(Color.parseColor(position % 2 == 0 ? "#F5F5F5" : "#e5e5e5"));
                ((TextView) convertView.findViewById(R.id.personName)).setText(p.getName());
                ((TextView) convertView.findViewById(R.id.personEmail)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };
        ccAddressAdapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                convertView.setBackgroundColor(Color.parseColor(position % 2 == 0 ? "#F5F5F5" : "#e5e5e5"));
                ((TextView) convertView.findViewById(R.id.personName)).setText(p.getName());
                ((TextView) convertView.findViewById(R.id.personEmail)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };

        toAddress.setAdapter(toAddressAdapter);
        toAddress.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

        ccAddress.setAdapter(ccAddressAdapter);
        ccAddress.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
    }

    //to get employee list
    public void getBusinessContactList() {
        Helper.getInstance().openProgress(this);
        Thread thread = new Thread(businessContactListInterface);
        thread.start();
    }

    public void storeBusinessList(final JSONObject object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (object != null && object.getBoolean("success")) {
                        JSONArray usersList = object.getJSONArray("business_contacts");
                        if (usersList.length() > 0) {
                            businessList = new Person[usersList.length()];
                            for (int i = 0; i < usersList.length(); i++) {
                                JSONObject user = usersList.getJSONObject(i);
                                businessList[i] = new Person(user.getString("contact_name"), user.getString("contact_name"), user.getString("contact_id"));
                            }
                        }
                        setBusinessList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Helper.getInstance().closeProgress(DirectClientVistActivity.this);
            }
        });
    }

    public void setBusinessList() {
        businessListAdapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, businessList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                convertView.setBackgroundColor(Color.parseColor(position % 2 == 0 ? "#F5F5F5" : "#e5e5e5"));
                ((TextView) convertView.findViewById(R.id.personName)).setText(p.getName());
                ((TextView) convertView.findViewById(R.id.personEmail)).setVisibility(View.GONE);

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };

        businessContact.setAdapter(businessListAdapter);
        businessContact.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
    }

    //request
    public void submitForm() {
        Helper.getInstance().openProgress(this);
        Thread thread = new Thread(formInterface);
        thread.start();
    }

    private void requestCompleted(final JSONObject response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response != null && response.getBoolean("success")) {
                        ToastUtil.showToast(DirectClientVistActivity.this, response.getString("message"));
                        resetForm();
                    } else if (response != null) {
                        ToastUtil.showToast(DirectClientVistActivity.this, response.getString("message"));
                    } else {
                        ToastUtil.showToast(DirectClientVistActivity.this, "Fail to send your request");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Helper.getInstance().closeProgress(DirectClientVistActivity.this);
            }
        });
    }

}
