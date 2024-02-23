package com.tvisha.trooptime.activity.activity.helper;

/**
 * Created by koti on 22/5/17.
 */

public class Constants {
    public static final String SERVER_KEY = "ba5d400506348b7d54088203324f5224";
    // public static final String TOKEN = "fdhTxknaBH9Nt8Hjtdy8k14bny9CXJxd";
    // public static final String TOKEN = "ExN60jFdtSH1IUABa80ODkDXsUg1J3Bo";
    public static final String TOKEN = "dHJvb3BNZXNzZW5nZXJUb2tlbg";

    public static final int RESPONCE_SUCCESSFUL = 200;
    public static final int HOLIDAY_TYPE_NONE = 0;
    public static final int HOLIDAY_TYPE_WEEKOFF = 1;
    public static final int HOLIDAY_TYPE_CONFIRM = 2;
    public static final int HOLIDAY_TYPE_OPTIONAL = 3;
    public static final int HOLIDAY_TYPE_LEAVE = 4;
    public static final int HOLIDAY_TYPE_COMPOFF = 5;

    public final static int NO_ACTIVITY = 0;
    public final static int ACTIVITY_CHECKIN = 1;
    public final static int ACTIVITY_CHECKOUT = 2;


    public static final int FULL_DAY = 1;
    public static final int FIRST_HALF_DAY = 2;
    public static final int SECOND_HALF_DAY = 3;

    public final static int UsbPermission = 1;
    public final static int EMPLOYESSD_ADDED = 3;



//    public final static int LOGIN_SUCCESSFUL = 1000;


    public static final String MONTHS[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    public static final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int WORKING_HOURS_ANY = 0;
    public static final int WORKING_HOURS_LESS_THAN_8 = 1;
    public static final int WORKING_HOURS_MORE_THAN_8 = 2;
    public static final int WORKING_HOURS_MORE_THAN_10 = 3;
    public static final int ATTENDENCE_FILTER_NONE = 0;
    public static final int ATTENDENCE_FILTER_LATE_LOGIN = 1;
    public static final int ATTENDENCE_FILTER_EARLY_LOGOUT = 2;
    public static final int PIN_STATUS_NONE = 0;
    public static final int PIN_STATUS_TOP_PINNED = 1;
    public static final int PIN_STATUS_BOTTOM_PINNED = 2;
    public static final int PIN_TYPE_TEAM = 1;
    public static final int PIN_TYPE_ALL = 2;
    public static final int EARLY_LOGOUT_REPORT = 1;
    public static final int LATE_LOGIN_REPORT = 2;
    public static final int PERMISSION_REPORT = 3;
    public static final int LESS_WORK_HRS_REPORT = 4;
    public static final int EXCEPTION_REPORT = 5;
    public static final int  LEAVE_SICK_REPORT = 9;
    public static final int  LEAVE_CASUAL_REPORT = 10;
    public static final int  LEAVE_OPTIONAL_REPORT = 11;
    public static final int  LOP_REPORT = 7;
    public static final int AUTOCHECK_OUT_REPORT = 6;
    public static final int LEAVE_REPORT = 8;
    public static final int PERMISSION_REQUEST_EARLY_LOGOUT = 1;
    public static final int PERMISSION_REQUEST_EXTENDED_LOGOUT = 2;
    public static final int PERMISSION_REQUEST_LATE_LOGIN = 3;
    public static final int PERMISSION_TYPE_OTHERS = 4;
    public static final int REQUEST_LEAVE = 1;
    public static final int REQUEST_PERMISSION = 2;
    public static final int REQUEST_COMPOFF = 3;
    public static final int REQUEST_SWAP = 4;
    public static final int REQUEST_OVERTIME = 5;
    public static final int REQUEST_STATUS_PENDING = 0;
    public static final int REQUEST_STATUS_APPROVED = 1;
    public static final int REQUEST_STATUS_REJECTED = 2;
    public static final int EXCEPTION_AUTOCHECKOUT_STATUS_PENDING = 0;
    public static final int EXCEPTION_AUTOCHECKOUT_STATUS_APPROVED = 1;

    //swap_status = 1 approved ,0 pending,2 rejected
    public static final int EXCEPTION_AUTOCHECKOUT_STATUS_REJECTED = 2;
    public static final int BREAK_TYPE_NORMAL = 0; // lunch break tea break
    public static final int BREAK_TYPE_TRANSIT = 1;
    public static final int BREAK_TYPE_CLIENT = 2; //sales

    public class Static {
        public static final int CHECK_IN = 1;
        public static final int CHECK_OUT = 2;
    }

    public class LeaveTypes {

        public static final int SickLeave = 1;
        public static final int CasualLeave = 2;
        public static final int OptionalHoliday = 3;
    }

    public class UserRole {
        public static final int ADMIN = 1;
        public static final int USER = 2;
    }

    public class ROLE {
        public static final int REPORING_MANAGER = 2;
        public static final int USER = 1;
    }

    public class Tabclick {
        public static final int SELF = 1;
        public static final int TEAM = 2;
        public static final int ALL = 3;
    }

    public class Breaks {
        public static final int breaks = 1;
        public static final int sales = 2;
        public static final int transit = 3;
        public static final int others = 4;
    }

    public class ExceptionAutoCheckoutComments {
        public static final int EXCEPTION_TYPE_CHECK_IN_EXCEPTION = 1;
        public static final int EXCEPTION_TYPE_CHECK_OUT_EXCEPTION = 2;
        public static final int EXCEPTION_TYPE_AUTOCHECK_OUT = 3;
    }
    public class NotificationType{
        public static final int NOTIFICATION_CHECK_IN = 0;
        public static final int NOTIFICATION_CHECK_OUT = 1;
        public static final int NOTIFICATION_LEAVE_SICK = 2;
        public static final int NOTIFICATION_LEAVE_CASUAL = 3;
        public static final int NOTIFICATION_LEAVE_OPTIONAL = 4;
        public static final int NOTIFICATION_LEAVE_COVID = 5;
        public static final int NOTIFICATION_LEAVE_COVID_WFH = 6;
        public static final int NOTIFICATION_LEAVE_PATERNITY = 7;
        public static final int NOTIFICATION_LEAVE_MATERNITY = 8;
    }
    public static final String STORAGE_PERMISSION_LIST[] ={"android.permission.READ_MEDIA_AUDIO","android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO","android.permission.CAMERA"};
    public static final String STORAGE_PERMISSION_LIST_BLOW_T[] = {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    public static final int STORAGE_PERMISSION = 12;
    public static final int CAMERA_ACTIVITY_RESULTS = 13;
    public static final int GALLERY_ACTIVITY_RESULTS = 14;
}

