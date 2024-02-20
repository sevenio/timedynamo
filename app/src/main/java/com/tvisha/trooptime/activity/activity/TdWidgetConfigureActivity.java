package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.R;

/**
 * The configuration screen for the {@link TdWidget TdWidget} AppWidget.
 */
public class TdWidgetConfigureActivity extends Activity implements View.OnClickListener {

    private static final String PREFS_NAME = "com.tvisha.trooptime.Activity.Activity.TdWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = TdWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            TdWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    TextView tv_permission, tv_leave, tv_onthe_way, call_manager, call_hr;
    LinearLayout mainLayout;

    public TdWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.simple_popup);

        initViews();
        initListeners();





        /*mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(TdWidgetConfigureActivity.this, mAppWidgetId));*/
    }

    private void initListeners() {

        tv_leave.setOnClickListener(this);
        tv_onthe_way.setOnClickListener(this);
        tv_permission.setOnClickListener(this);
        call_hr.setOnClickListener(this);
        call_manager.setOnClickListener(this);
    }

    private void initViews() {
        // mainLayout=findViewById(R.id.mainLayout);
        // mainLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tv_permission = (TextView) findViewById(R.id.tv_permission);
        tv_leave = (TextView) findViewById(R.id.tv_leave);
        tv_onthe_way = (TextView) findViewById(R.id.tv_onthe_way);
        call_hr = (TextView) findViewById(R.id.call_hr);
        call_manager = (TextView) findViewById(R.id.call_manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_permission:
                Navigation.getInstance().permissionRequest(TdWidgetConfigureActivity.this);
                break;
            case R.id.tv_leave:
                Navigation.getInstance().leaveRequest(TdWidgetConfigureActivity.this);
                break;
            case R.id.call_hr:
                Navigation.getInstance().openCallLog(this);
                break;
            case R.id.call_manager:
                Navigation.getInstance().openCallLog(this);
                break;
            case R.id.tv_onthe_way:
                break;
        }

    }
}

