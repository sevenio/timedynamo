package com.tvisha.trooptime.activity.activity.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.snackbar.Snackbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.api_handlers.FileSaver;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.activity.activity.db.AttendanceModel;
import com.tvisha.trooptime.activity.activity.db.AttendanceTable;
import com.tvisha.trooptime.activity.activity.db.BranchModel;
import com.tvisha.trooptime.activity.activity.db.BranchTable;
import com.tvisha.trooptime.activity.activity.db.BreakModel;
import com.tvisha.trooptime.activity.activity.db.BreakTable;
import com.tvisha.trooptime.activity.activity.db.UserBreakModel;
import com.tvisha.trooptime.activity.activity.db.UserBreakTable;
import com.tvisha.trooptime.activity.activity.db.UserShiftModel;
import com.tvisha.trooptime.activity.activity.db.UserShiftTable;
import com.tvisha.trooptime.activity.activity.db.UsersModel;
import com.tvisha.trooptime.activity.activity.db.UsersTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.Socket;

public class Utilities {


    /*------ Network status checking --*/
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static int NORMAL_STRING = 0;
    public static int EMAIL = 1;
    public static int NAME = 2;
    public static int DATE = 3;
    public static int TIME = 4;
    public static int PHONE = 5;
    public static int NUMBER = 6;
    public static int URL = 7;
    public static int GUEST_SIGN = 8;
    public static int MANDATORY = 1;
    public static int NotCheckable = 0;
    public static int CheckIfValEnter = 2;
    CustomProgressBar customProgressBar;

    public static String convertString2Date(String str) throws ParseException {
        return str;
    }

    public static void openKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean workhoursComparision(String shift_working_hours, String workingHours) {


        SimpleDateFormat day_format = null;
        Date shifttime = null, worktime = null;
        if ((shift_working_hours != null && !shift_working_hours.trim().isEmpty() && !shift_working_hours.trim().equals("null") && !shift_working_hours.contains("00:00:00")) && (workingHours != null && !workingHours.trim().isEmpty() && !workingHours.trim().equals("null") && !workingHours.contains("00:00:00"))) {
            try {
                day_format = new SimpleDateFormat("HH:mm:ss");
                shifttime = day_format.parse(shift_working_hours);
                worktime = day_format.parse(workingHours);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (shifttime.after(worktime)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static String getReferenceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userrecord", 0);
        return sp.getString("reference_id", "");
    }

    public static int parseReferenceId(Context context, String referenceId) {
        int id = 0;
        try {
            SharedPreferences sp = context.getSharedPreferences("userrecord", 0);
            String deviceId = sp.getString("reference_id", "");
            if (referenceId.contains(deviceId)) {
                String parseId = referenceId.replace(deviceId + "-", "");
                if (parseId != null && !parseId.isEmpty()) {
                    id = Integer.parseInt(parseId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDateTimeNew() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDateNew() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static boolean timeComparision(String shift_time, String check_in_time, int check_in_out) {
        SimpleDateFormat year_format = null;
        SimpleDateFormat day_format = null;
        try {
            if (!check_in_time.contains("0000-00-00")) {
                year_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                day_format = new SimpleDateFormat("HH:mm:ss");
                Date shifttime = day_format.parse(shift_time);
                Date chekintime = year_format.parse(check_in_time);
                String checkTiming = day_format.format(chekintime);
                Date checkTime = day_format.parse(checkTiming);
                switch (check_in_out) {
                    case Constants.Static.CHECK_OUT:
                        if (shifttime.after(checkTime)) {
                            return true;
                        }
                        break;
                    case Constants.Static.CHECK_IN:
                        if (shifttime.before(checkTime)) {
                            return true;
                        }
                        break;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getAttendenceTime(String timestamp) {
        String time = "N/A";
        try {
            if (!timestamp.contains("0000-00-00")) {
                Date d1 = null;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = null;
                if (timestamp != null && !timestamp.equals("null") && !timestamp.isEmpty() && timestamp.contains(".")) {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (timestamp != null) {
                    d1 = form.parse(timestamp);
                } else {
                    String date = form.format(calendar.getTime());
                    d1 = form.parse(date);
                }
                Calendar postDate = Calendar.getInstance();
                postDate.setTime(d1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String date = dateFormat.format(d1);
                //return date.toUpperCase();

                time = (postDate.get(Calendar.HOUR) > 0 ? postDate.get(Calendar.HOUR) : 12) + ":" +
                        (postDate.get(Calendar.MINUTE) > 9 ? postDate.get(Calendar.MINUTE) : ("0" + postDate.get(Calendar.MINUTE))) + " "
                        + ((postDate.get(Calendar.AM_PM)) == 0 ? "AM" : "PM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getAttendenceTime1(String timestamp) {
        String time = "N/A";
        try {
            if (!timestamp.contains("0000-00-00")) {
                Date d1 = null;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = null;
                if (timestamp != null && !timestamp.equals("null") && !timestamp.isEmpty() && timestamp.contains(".")) {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (timestamp != null) {
                    d1 = form.parse(timestamp);
                } else {
                    String date = form.format(calendar.getTime());
                    d1 = form.parse(date);
                }
                Calendar postDate = Calendar.getInstance();
                postDate.setTime(d1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String date = dateFormat.format(d1);
                //return date.toUpperCase();

                time = (postDate.get(Calendar.HOUR) > 0 ? postDate.get(Calendar.HOUR) : 12) + ":" +
                        (postDate.get(Calendar.MINUTE) > 9 ? postDate.get(Calendar.MINUTE) : ("0" + postDate.get(Calendar.MINUTE))) + " "
                        + ((postDate.get(Calendar.AM_PM)) == 0 ? "am" : "pm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void ShowSnackbar(Context context, View view, String msg) {
        /*String viewText = " ";
        Snackbar snackbar = Snackbar.make(view, ( viewText.trim()+" "+msg ).trim(), Snackbar.LENGTH_LONG).setAction("Action", null);
        View sView = snackbar.getView();
        ((FrameLayout.LayoutParams)sView.getLayoutParams()).gravity = Gravity.BOTTOM;
        ((FrameLayout.LayoutParams)sView.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
        //((FrameLayout.LayoutParams)sView.getLayoutParams()).setMargins(30,10,30,0);


//RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sView.getLayoutParams();
*//* params.
sView.setLayoutParams(params);*//*

        TextView textView
                = (TextView) sView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        snackbar.show();*/
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.show();
    }

    public static boolean isValidDate(String inDate) {
        Date date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            pe.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getDeviceHeight(Activity ctx) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        if (width > height) {
            return height;
        } else {
            return width;
        }
    }

    public static int getDeviceWidth(Activity ctx) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        if (width < height) {
            return height;
        } else {
            return width;
        }
    }

    public static boolean validEmail(String email) {
        if (email.trim().length() > 0) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            Pattern p = Pattern.compile(ePattern);
            Matcher m = p.matcher(email);
            return m.matches();
        } else {
            return false;
        }
    }

    public static ColorStateList getColorStateList(int checkedColor, int offColor) {
        return new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_checked, android.R.attr.state_pressed}
                , new int[]{-android.R.attr.state_checked, android.R.attr.state_pressed}
                , new int[]{android.R.attr.state_checked}
                , new int[]{-android.R.attr.state_checked}
        }, new int[]{checkedColor, offColor, checkedColor, offColor}
        );
    }

    public static boolean isValidName(String name) {
        if (name != null) {
            if (name.length() >= 3 && !Utilities.isNumeric(name)) {
                CharSequence inputStr = name;
                //Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
                String regex = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(inputStr);
                return matcher.matches();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /*------ Network status checking  complete--*/

    public static boolean isValidPhone(String phone) {
        if (null != phone) {
            phone = phone.trim();
            if (phone.length() >= 10 && Utilities.isNumeric(phone) && (phone.startsWith("7") || (phone.startsWith("8") || (phone.startsWith("9"))))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int isNullCheck(int val) {
        return 0 != val ? val : 0;
    }

    public static Date isNullDate(Date val) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(String.valueOf(val));
        } catch (ParseException pe) {
            pe.printStackTrace();
            return dateFormat.parse("2000-01-01 00:00:00");
        }
    }

    public static String isNullCheck(String val, String x) {
        //return  (val != null ? val :"");
        return (val != null ? val : x);
    }

    public static String getDateTime(Date val) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = val;
        return dateFormat.format(date);
    }

    public static String getCurrent_time() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static boolean validName(String name) {
        if (null != name) {
            if (name.length() >= 3) {
                CharSequence inputStr = name;
                String regex = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(inputStr);
                return matcher.matches();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validPassword(String pwd) {
        if (pwd.length() >= 3) {
            String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(pwd);
            return matcher.find();
        } else {
            return false;
        }
    }

    public static boolean validAlphaNumeric(String pwd) {
        if (pwd.length() >= 2) {
            String PASSWORD_PATTERN = "^[a-zA-Z0-9_]*$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(pwd);
            return matcher.find();
        } else {
            return false;
        }
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void setSharedPref(Context applicationContext, String key, String value) {
        SharedPreferences sp = applicationContext.getSharedPreferences("userrecord", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setSharedPrefInt(Context applicationContext, String key, int value) {
        SharedPreferences sp = applicationContext.getSharedPreferences("userrecord", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static double checkDimension(Context context) {

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int mWidthPixels = displayMetrics.widthPixels;
        int mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static java.sql.Date convertString2SQLDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = null;
        try {
            parsed = format.parse(date);
            return new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isTabletDevice(Context activityContext) {

        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
        DisplayMetrics metrics = new DisplayMetrics();
        Activity activity = (Activity) activityContext;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (device_large) {
            //Tablet
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_TV) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_280) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_400) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_560) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
                return true;
            }
        } else {
            //Mobile
        }
        return false;
    }

    public static String isFieldValid(String val, int minLength, int Input_Type, int Field_Type) {
        val = val.trim();
        if (Field_Type == NotCheckable) {
            return "true";
        } else if (Field_Type == CheckIfValEnter) {
            if (val.length() == 0) {
                return "true";
            } else if (val.length() >= minLength) {
                if (Input_Type == NORMAL_STRING) {
                    return true + "";
                } else if (Input_Type == EMAIL && !validEmail(val)) {
                    return "Please provide valid Email.";
                } else if (Input_Type == NAME && !isValidName(val)) {
                    return "Please provide valid name.";

                } else if (Input_Type == DATE && !isValidDate(val)) {
                    return "Please provide valid date.";

                } else if (Input_Type == PHONE && !isValidPhone(val)) {
                    return "Please provide phone number.";

                } else if (Input_Type == NUMBER && !isNumeric(val)) {
                    return "Please provide numeric values.";

                } else if (Input_Type == URL && !isValidUrl(val)) {
                    return "Please provide valid url.";

                } else {
                    return "true";
                }
            } else {
                return "Please provide minimum of " + minLength + " characters";
            }
        } else if (Field_Type == MANDATORY) {

            if (val.length() >= minLength) {
                if (Input_Type == NORMAL_STRING) {
                    return true + "";
                } else if (Input_Type == EMAIL && !validEmail(val)) {
                    return "Please provide valid Email.";
                } else if (Input_Type == NAME && !isValidName(val)) {
                    return "Please provide valid name.";

                } else if (Input_Type == DATE && !isValidDate(val)) {
                    return "Please provide valid date.";

                } else if (Input_Type == PHONE && !isValidPhone(val)) {
                    return "Please provide valid phone number.";

                } else if (Input_Type == NUMBER && !isNumeric(val)) {
                    return "Please provide numeric values.";

                } else if (Input_Type == URL && !isValidUrl(val)) {
                    return "Please provide valid url.";

                } else {
                    return "true";
                }
            } else {
                return "please provide minimum of " + minLength + " characters.";
            }
        }
        return "false";
    }

    public static boolean isValidUrl(String val) {
        return true;
    }

    public static void syncAttendance(final Context context) {
        try {
            new Runnable() {
                @Override
                public void run() {
                    AttendanceTable attendanceTable = new AttendanceTable(context);
                    JSONArray attendanceData = attendanceTable.getNonSyncAttendance();
                    if (attendanceData != null && attendanceData.length() > 0) {
                        SocketIo socketIo = SocketIo.getInstance();
                        if (socketIo.isSocketConnected()) {
                            Socket socket = socketIo.getSocket();
                            if (attendanceData.length() > 0) {
                                socket.emit(SocketConstants.EVENT_SYNC_ATTENDANCE, attendanceData);
                            }
                            syncBreaks(context);
                        }
                    }

                }
            }.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAttendance(final Context context, final JSONArray attendanceArray) {
        try {
            new Runnable() {
                @Override
                public void run() {
                    if (attendanceArray != null && attendanceArray.length() > 0) {
                        AttendanceTable attendanceTable = new AttendanceTable(context);
                        attendanceTable.updateAttendance(attendanceArray);
                        syncBreaks(context);
                    }

                }
            }.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncBreaks(final Context context) {
        try {
            new Runnable() {
                @Override
                public void run() {
                    UserBreakTable userBreakTable = new UserBreakTable(context);
                    JSONArray breakData = userBreakTable.getNonSyncBreaks();
                    if (breakData == null || breakData.length() == 0) {
                        return;
                    }
                    SocketIo socketIo = SocketIo.getInstance();
                    if (socketIo.isSocketConnected()) {
                        Socket socket = socketIo.getSocket();
                        socket.emit(SocketConstants.EVENT_SYNC_BREAK, breakData);
                    }
                }
            }.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateBreak(final Context context, final JSONArray breakArray) {
        try {
            new Runnable() {
                @Override
                public void run() {

                    if (breakArray != null && breakArray.length() > 0) {
                        UserBreakTable userBreakTable = new UserBreakTable(context);
                        userBreakTable.updateBreaks(breakArray);
                        syncBreaks(context);
                    }

                }
            }.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAttendance(final Context context, final JSONObject attendanceObj) {
        try {


            if (attendanceObj != null && !attendanceObj.toString().isEmpty()) {
                final AttendanceTable attendanceTable = new AttendanceTable(context);
                final AttendanceModel attendanceModel = new AttendanceModel();

                attendanceModel.setAttendanceId(attendanceObj.optInt("attendance_id"));
                attendanceModel.setUserId(attendanceObj.optString("user_id"));
                attendanceModel.setCheckIn(attendanceObj.optString("check_in"));
                attendanceModel.setCheckOut(attendanceObj.optString("check_out"));
                attendanceModel.setIsCheckinManual(attendanceObj.optInt("is_checkin_manual"));
                attendanceModel.setIsCheckoutManual(attendanceObj.optInt("is_checkout_manual"));
                if (attendanceObj.has("reference_id")) {
                    attendanceModel.setReferenceId(attendanceObj.optString("reference_id"));
                }
                attendanceModel.setIsSync(1);
                attendanceTable.saveAttendance(attendanceModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getLoginStatus(Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences("userrecord", 0);
        return sp.getBoolean("isLogin", false);
    }

    public static void addUserBreak(final Context context, final JSONObject userBreakObj) {
        final UserBreakTable userBreakTable = new UserBreakTable(context);
        final UserBreakModel userBreakModel = new UserBreakModel();

        try {

            if (userBreakObj != null && !userBreakObj.toString().isEmpty()) {
                userBreakModel.setUserBreakId(userBreakObj.optInt("user_break_id"));
                userBreakModel.setaId(0);
                userBreakModel.setAttendanceId(userBreakObj.optInt("attendance_id"));
                userBreakModel.setBreakId(userBreakObj.optInt("break_id"));
                userBreakModel.setStartTime(userBreakObj.optString("start_time"));
                userBreakModel.setEndTime(userBreakObj.optString("end_time"));
                userBreakModel.setReferenceId(userBreakObj.optString("reference_id"));
                userBreakModel.setType(userBreakObj.optInt("type"));
                userBreakModel.setCheck_in_branch_id(userBreakObj.optInt("check_in_branch_id"));
                userBreakModel.setCheck_out_branch_id(userBreakObj.optInt("check_in_branch_id"));
                userBreakModel.setIsSync(1);
                userBreakTable.saveBreaks(userBreakModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBranch(final Context context, final JSONObject branch) {
        final BranchTable branchTable = new BranchTable(context);
        final BranchModel branchModel = new BranchModel();

        try {

            if (branch != null && !branch.toString().isEmpty()) {
                branchModel.setBranch_id(branch.optInt("branch_id"));
                branchModel.setBranch_name(branch.optString("branch_name"));
                branchTable.saveBranch(branchModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBreak(final Context context, final JSONObject breakObj) {
        final BreakTable breakTable = new BreakTable(context);
        final BreakModel breakModel = new BreakModel();


        try {

            if (breakObj != null && !breakObj.toString().isEmpty()) {
                breakModel.setBreakId(breakObj.optInt("break_id"));
                breakModel.setShiftId(breakObj.optInt("shift_id"));
                breakModel.setBreakName(breakObj.optString("break_name"));
                breakModel.setBreakTime(breakObj.optString("break_time"));
                breakTable.saveBreak(breakModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ;

    public static boolean addEmployee(final Context context, final JSONArray empArray) {
        try {
            final UsersTable usersTable = new UsersTable(context);
            JSONObject empObj = null;
            if (empArray != null && empArray.length() > 0) {
                for (int i = 0; i < empArray.length(); i++) {
                    empObj = empArray.optJSONObject(i);
                    final UsersModel usersModel = new UsersModel();

                    usersModel.setName(empObj.optString("name"));

                    usersModel.setDisplayName(empObj.optString("name"));

                    usersModel.setUserId(empObj.optInt("user_id"));

                    usersModel.setEmail(empObj.optString("email"));

                    usersModel.setMobile(empObj.optString("mobile"));


                    usersModel.setEmpId(empObj.optString("emp_id"));

                    usersModel.setFingerPrint(empObj.optString("finger_print"));

                    usersModel.setRole(empObj.optString("role"));

                    usersModel.setShiftId(empObj.optString("shift_id"));

                    usersModel.setStatus(empObj.optString("status"));


                    usersModel.setReportingBossId(empObj.optString("reporting_boss_id"));

                    usersModel.setReportingHrId(empObj.optString("reporting_hr_id"));

                    usersModel.setReportingPermissionId(empObj.optString("reporting_permission_id"));

                    String url = empObj.optString("user_avatar");
                    if (empObj.optString("user_id").equals(MyApplication.sharedPreferences.getString(SharePreferenceKeys.USER_ID,""))){
                        MyApplication.sharedPreferences.edit().putString(SharePreferenceKeys.USER_AVATAR,url).apply();
                    }
                    if (url != null && !url.trim().isEmpty()) {
                        if (url.contains("data") || url.trim().startsWith("user_avatar/")) {
                            url = MyApplication.AWS_BASE_URL + url;
                        }

                        String imgName = url.substring(url.lastIndexOf("/") + 1, url.length());
                        FileSaver saver = new FileSaver(context, url, imgName);
                        saver.execute();
                        saver.setListener(new FileSaver.FileSaverListener() {
                            @Override
                            public void onAfterFileSaved(File file, int Status) {
                                try {
                                    usersModel.setUserAvatar(file.getAbsolutePath());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                usersTable.saveUser(usersModel);
                            }

                            @Override
                            public void onFailedToCompleteTask(Exception e) {
                                usersModel.setUserAvatar("");
                                usersTable.saveUser(usersModel);
                            }
                        });
                    } else {
                        usersModel.setUserAvatar("");
                        usersTable.saveUser(usersModel);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    ;

    public static void syncData(Context context, JSONObject jsonObject) {
        try {
            /*if(jsonObject.has("users")){
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                for(int i = 0;i < jsonArray.length();i++){
                    addEmployee(context,jsonArray.getJSONObject(i));
                }
            }*/

            if (jsonObject.has("shifts")) {
                JSONArray jsonArray = jsonObject.getJSONArray("shifts");

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addUserShift(context, jsonArray.optJSONObject(i));
                    }
                }

            }

            if (jsonObject.has("breaks")) {
                JSONArray jsonArray = jsonObject.getJSONArray("breaks");

                final BreakTable breakTable = new BreakTable(context);
                breakTable.removeBreaks();

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addBreak(context, jsonArray.optJSONObject(i));
                    }
                }

            }

            if (jsonObject.has("attendance")) {
                JSONArray jsonArray = jsonObject.getJSONArray("attendance");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addAttendance(context, jsonArray.optJSONObject(i));
                    }
                }

            }

            if (jsonObject.has("user_breaks")) {
                JSONArray jsonArray = jsonObject.getJSONArray("user_breaks");

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addUserBreak(context, jsonArray.optJSONObject(i));
                    }
                }

            }

            if (jsonObject.has("user_branches")) {
                JSONArray jsonArray = jsonObject.getJSONArray("user_branches");

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addBranch(context, jsonArray.optJSONObject(i));
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ;

    public static void addUserShift(final Context context, final JSONObject userShiftObj) {
        final UserShiftTable userShiftTable = new UserShiftTable(context);
        final UserShiftModel userShiftModel = new UserShiftModel();
        try {

            if (userShiftObj != null && !userShiftObj.toString().isEmpty()) {
                userShiftModel.setShiftId(userShiftObj.optInt("shift_id"));
                userShiftModel.setShiftName(userShiftObj.optString("shift_name"));
                userShiftModel.setFromTime(userShiftObj.optString("start_time"));
                userShiftModel.setToTime(userShiftObj.optString("end_time"));
                userShiftModel.setWeekOff(userShiftObj.optString("week_off"));
                userShiftTable.saveShift(userShiftModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEmployee(final Context context, final JSONObject empObj) {
        try {

            if (empObj != null && !empObj.toString().isEmpty()) {


                final UsersTable usersTable = new UsersTable(context);
                final UsersModel usersModel = new UsersModel();


                usersModel.setName(empObj.optString("name"));

                usersModel.setDisplayName(empObj.optString("name"));

                usersModel.setUserId(empObj.optInt("user_id"));

                usersModel.setEmail(empObj.optString("email"));

                usersModel.setMobile(empObj.optString("mobile"));


                usersModel.setEmpId(empObj.optString("emp_id"));

                usersModel.setFingerPrint(empObj.optString("finger_print"));

                usersModel.setRole(empObj.optString("role"));

                usersModel.setShiftId(empObj.optString("shift_id"));

                usersModel.setStatus(empObj.optString("status"));


                usersModel.setReportingBossId(empObj.optString("reporting_boss_id"));

                usersModel.setReportingHrId(empObj.optString("reporting_hr_id"));

                usersModel.setReportingPermissionId(empObj.optString("reporting_permission_id"));

                String url = empObj.optString("user_avatar");
                if (url != null && !url.trim().isEmpty()) {
                    if (url.contains("data") || url.trim().startsWith("user_avatar/")) {
                        url = MyApplication.AWS_BASE_URL + url;
                    }

                    String imgName = url.substring(url.lastIndexOf("/") + 1, url.length());
                    FileSaver saver = new FileSaver(context, url, imgName);
                    saver.execute();
                    saver.setListener(new FileSaver.FileSaverListener() {
                        @Override
                        public void onAfterFileSaved(File file, int Status) {
                            try {
                                usersModel.setUserAvatar(file.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            usersTable.saveUser(usersModel);
                        }

                        @Override
                        public void onFailedToCompleteTask(Exception e) {
                            usersModel.setUserAvatar("");
                            usersTable.saveUser(usersModel);
                        }
                    });
                } else {
                    usersModel.setUserAvatar("");
                    usersTable.saveUser(usersModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    ;

    public static void closeKeyboard(Context context, Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void openProgress(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (customProgressBar == null) {
                        customProgressBar = new CustomProgressBar(activity);
                    }
                    customProgressBar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void closeProgress(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (customProgressBar != null && customProgressBar.isShowing()) {
                        customProgressBar.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
