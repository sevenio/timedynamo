package com.tvisha.trooptime.activity.activity.Helper;

/**
 * Created by akhil on 28/5/18.
 */

public class SocketConstants {

    /*public final static String SOCKET_URL = "http://192.168.2.48:5000";*/

    /*public final static String SOCKET_URL = "http://192.168.2.48:8080";*/
    /*public final static String SOCKET_URL = "https://api.troopmessenger.com:8080";*/

    public final static String EVENT_SYNC_USER = "sync_user";
    public final static String EVENT_SYNC_DATA = "sync_data";
    public final static String EVENT_SYNC_ATTENDANCE = "sync_attendance";
    public final static String EVENT_SYNC_BREAK = "sync_break";

    public final static int SOCKET_CONNECT = 1;
    public final static int SOCKET_DISCONNECT = 2;
    public final static int SOCKET_SYNC_USER = 3;
    public final static int SOCKET_SYNC_DATA = 4;
    public final static int SOCKET_SYNC_ATTENDANCE = 5;
    public final static int SOCKET_SYNC_BREAK = 6;
}
