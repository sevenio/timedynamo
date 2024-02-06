package com.tvisha.trooptime.activity.activity.ApiPostModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Attendance implements Serializable {

    /* "is_exception_check_in": "1",

                  "is_exception_check_out": "2",
                  "exception_status_in": "0",
                  "exception_status_out": "0",*/
    private final static long serialVersionUID = 6178049529468061659L;
    @SerializedName("attendance_id")
    @Expose
    private String attendance_id;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("working_hours")
    @Expose
    private String workingHours;
    @SerializedName("break_time")
    @Expose
    private int breakTime;
    @SerializedName("attendance_list")
    @Expose
    private List<AttendanceList> attendanceList = null;
    @SerializedName("ot")
    @Expose
    private int ot;
    @SerializedName("is_holiday")
    @Expose
    private int isHoliday;
    @SerializedName("is_checkin_manual")
    @Expose
    private String isCheckinManual;
    @SerializedName("is_checkout_manual")
    @Expose
    private String isCheckoutManual;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("number_of_breaks")
    @Expose
    private int numberOfBreaks;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("user_avatar")
    @Expose
    private String userAvatar;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("shift_start_time")
    @Expose
    private String shiftStartTime;
    @SerializedName("shift_end_time")
    @Expose
    private String shiftEndTime;
    @SerializedName("shift_working_hours")
    @Expose
    private String shiftWorkingHours;
    /*@SerializedName("breaks_list")
    @Expose
    private List<Breaks_list> breaksList = null;*/
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("db_remarks")
    @Expose
    private String db_remarks;
    @SerializedName("is_pinned")
    @Expose
    private String isPinned;
    @SerializedName("pinned_date")
    @Expose
    private String pinnedDate;
    @SerializedName("pin_type")
    @Expose
    private String pinType;
    @SerializedName("check_in_branch")
    @Expose
    private String checkInBranch;
    @SerializedName("check_out_branch")
    @Expose
    private String checkOutBranch;
    @SerializedName("in_branch")
    @Expose
    private String inBranch;
    @SerializedName("out_branch")
    @Expose
    private String outBranch;
    @SerializedName("is_auto_check_out")
    @Expose
    private String isAutoCheckOut;
    @SerializedName("is_exception")
    @Expose
    private String isException;
    @SerializedName("exception_status")
    @Expose
    private String exceptionStatus;
    @SerializedName("is_auto_check_out_approved")
    @Expose
    private String isAutoCheckOutApproved;
    @SerializedName("is_exception_check_in")
    @Expose
    private String is_exception_check_in;
    @SerializedName("is_exception_check_out")
    @Expose
    private String is_exception_check_out;
    @SerializedName("exception_status_in")
    @Expose
    private String exception_status_in;
    @SerializedName("exception_status_out")
    @Expose
    private String exception_status_out;

    public String getIs_exception_check_in() {
        return is_exception_check_in;
    }

    public void setIs_exception_check_in(String is_exception_check_in) {
        this.is_exception_check_in = is_exception_check_in;
    }

    public String getDb_remarks() {
        return db_remarks;
    }

    public void setDb_remarks(String db_remarks) {
        this.db_remarks = db_remarks;
    }

    public String getIs_exception_check_out() {
        return is_exception_check_out;
    }

    public void setIs_exception_check_out(String is_exception_check_out) {
        this.is_exception_check_out = is_exception_check_out;
    }

    public String getException_status_in() {
        return exception_status_in;
    }

    public void setException_status_in(String exception_status_in) {
        this.exception_status_in = exception_status_in;
    }

    public String getException_status_out() {
        return exception_status_out;
    }

    public void setException_status_out(String exception_status_out) {
        this.exception_status_out = exception_status_out;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public List<AttendanceList> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceList> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public int getOt() {
        return ot;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }

    public int getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(int isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getIsCheckinManual() {
        return isCheckinManual;
    }

    public void setIsCheckinManual(String isCheckinManual) {
        this.isCheckinManual = isCheckinManual;
    }

    public String getIsCheckoutManual() {
        return isCheckoutManual;
    }

    public void setIsCheckoutManual(String isCheckoutManual) {
        this.isCheckoutManual = isCheckoutManual;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfBreaks() {
        return numberOfBreaks;
    }

    public void setNumberOfBreaks(int numberOfBreaks) {
        this.numberOfBreaks = numberOfBreaks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public String getShiftWorkingHours() {
        return shiftWorkingHours;
    }

    public void setShiftWorkingHours(String shiftWorkingHours) {
        this.shiftWorkingHours = shiftWorkingHours;
    }

  /*  public List<Breaks_list> getBreaksList() {
        return breaksList;
    }

    public void setBreaksList(List<Breaks_list> breaksList) {
        this.breaksList = breaksList;
    }*/

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(String isPinned) {
        this.isPinned = isPinned;
    }

    public String getPinnedDate() {
        return pinnedDate;
    }

    public void setPinnedDate(String pinnedDate) {
        this.pinnedDate = pinnedDate;
    }

    public String getPinType() {
        return pinType;
    }

    public void setPinType(String pinType) {
        this.pinType = pinType;
    }

    public String getCheckInBranch() {
        return checkInBranch;
    }

    public void setCheckInBranch(String checkInBranch) {
        this.checkInBranch = checkInBranch;
    }

    public String getCheckOutBranch() {
        return checkOutBranch;
    }

    public void setCheckOutBranch(String checkOutBranch) {
        this.checkOutBranch = checkOutBranch;
    }

    public String getInBranch() {
        return inBranch;
    }

    public void setInBranch(String inBranch) {
        this.inBranch = inBranch;
    }

    public String getOutBranch() {
        return outBranch;
    }

    public void setOutBranch(String outBranch) {
        this.outBranch = outBranch;
    }

    public String getIsAutoCheckOut() {
        return isAutoCheckOut;
    }

    public void setIsAutoCheckOut(String isAutoCheckOut) {
        this.isAutoCheckOut = isAutoCheckOut;
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(String exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    public String getIsAutoCheckOutApproved() {
        return isAutoCheckOutApproved;
    }

    public void setIsAutoCheckOutApproved(String isAutoCheckOutApproved) {
        this.isAutoCheckOutApproved = isAutoCheckOutApproved;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

}



/*
public class Attendance implements Serializable
{

    @SerializedName("check_in")
    @Expose
    private String check_in;
    @SerializedName("check_out")
    @Expose
    private String check_out;
    @SerializedName("working_hours")
    @Expose
    private String working_hours;
    @SerializedName("break_time")
    @Expose
    private int break_time;
    @SerializedName("attendance_list")
    @Expose
    private List<Attendance_list> attendance_list = new ArrayList<>();
    @SerializedName("ot")
    @Expose
    private int ot;
    @SerializedName("is_holiday")
    @Expose
    private int is_holiday;
    @SerializedName("is_checkin_manual")
    @Expose
    private String is_checkin_manual;
    @SerializedName("is_checkout_manual")
    @Expose
    private String is_checkout_manual;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("number_of_breaks")
    @Expose
    private int number_of_breaks;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String display_name;
    @SerializedName("user_avatar")
    @Expose
    private String user_avatar;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;
    @SerializedName("shift_start_time")
    @Expose
    private String shift_start_time;
    @SerializedName("shift_end_time")
    @Expose
    private String shift_end_time;
    @SerializedName("shift_working_hours")
    @Expose
    private String shift_working_hours;

    public String getPinned_type() {
        return pinned_type;
    }

    public void setPinned_type(String pinned_type) {
        this.pinned_type = pinned_type;
    }

    @SerializedName("breaks_list")
    @Expose
    private List<Breaks_list> breaks_list = new ArrayList<>();
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("is_pinned")
    @Expose
    private String is_pinned;
    @SerializedName("pinned_date")
    @Expose
    private String pinned_date;
    @SerializedName("pin_type")
    @Expose
    private String pinned_type;
    private final static long serialVersionUID = -6321845813609292744L;

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public int getBreak_time() {
        return break_time;
    }

    public void setBreak_time(int break_time) {
        this.break_time = break_time;
    }

    public List<Attendance_list> getAttendance_list() {
        return attendance_list;
    }

    public void setAttendance_list(List<Attendance_list> attendance_list) {
        this.attendance_list = attendance_list;
    }

    public int getOt() {
        return ot;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }

    public int getIs_holiday() {
        return is_holiday;
    }

    public void setIs_holiday(int is_holiday) {
        this.is_holiday = is_holiday;
    }

    public String getIs_checkin_manual() {
        return is_checkin_manual;
    }

    public void setIs_checkin_manual(String is_checkin_manual) {
        this.is_checkin_manual = is_checkin_manual;
    }

    public String getIs_checkout_manual() {
        return is_checkout_manual;
    }

    public void setIs_checkout_manual(String is_checkout_manual) {
        this.is_checkout_manual = is_checkout_manual;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber_of_breaks() {
        return number_of_breaks;
    }

    public void setNumber_of_breaks(int number_of_breaks) {
        this.number_of_breaks = number_of_breaks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getShift_start_time() {
        return shift_start_time;
    }

    public void setShift_start_time(String shift_start_time) {
        this.shift_start_time = shift_start_time;
    }

    public String getShift_end_time() {
        return shift_end_time;
    }

    public void setShift_end_time(String shift_end_time) {
        this.shift_end_time = shift_end_time;
    }

    public String getShift_working_hours() {
        return shift_working_hours;
    }

    public void setShift_working_hours(String shift_working_hours) {
        this.shift_working_hours = shift_working_hours;
    }

    public List<Breaks_list> getBreaks_list() {
        return breaks_list;
    }

    public void setBreaks_list(List<Breaks_list> breaks_list) {
        this.breaks_list = breaks_list;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "check_in='" + check_in + '\'' +
                ", check_out='" + check_out + '\'' +
                ", working_hours='" + working_hours + '\'' +
                ", break_time=" + break_time +
                ", attendance_list=" + attendance_list +
                ", ot=" + ot +
                ", is_holiday=" + is_holiday +
                ", is_checkin_manual='" + is_checkin_manual + '\'' +
                ", is_checkout_manual='" + is_checkout_manual + '\'' +
                ", date='" + date + '\'' +
                ", number_of_breaks=" + number_of_breaks +
                ", name='" + name + '\'' +
                ", display_name='" + display_name + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_id='" + user_id + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", shift_start_time='" + shift_start_time + '\'' +
                ", shift_end_time='" + shift_end_time + '\'' +
                ", shift_working_hours='" + shift_working_hours + '\'' +
                ", breaks_list=" + breaks_list +
                ", remarks=" + remarks +
                ", is_pinned='" + is_pinned + '\'' +
                ", pinned_date='" + pinned_date + '\'' +
                ", pinned_type='" + pinned_type + '\'' +
                '}';
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIs_pinned() {
        return is_pinned;
    }

    public void setIs_pinned(String is_pinned) {
        this.is_pinned = is_pinned;
    }

    public String getPinned_date() {
        return pinned_date;
    }

    public void setPinned_date(String pinned_date) {
        this.pinned_date = pinned_date;
    }

}
*/
