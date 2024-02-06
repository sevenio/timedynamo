package com.tvisha.trooptime.activity.activity.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.Dialog.DialogCallConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by koti on 31/5/17.
 */

public class Helper {
    private static Helper ourInstance = new Helper();
    CustomProgressBar customProgressBar;

    public static Helper getInstance() {
        if (ourInstance == null) {
            ourInstance = new Helper();
        }
        return ourInstance;
    }

    public static JSONObject stringToJsonObject(String data) {

        if (data != null) {
            try {
                JSONObject data_object = new JSONObject(data);
                return data_object;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static List<String> getDates(String dateString1, String dateString2) {
        List<String> dates = new ArrayList<String>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {

            dates.add(df1.format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
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

    public void openKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }

    public boolean stringMatchWithFirstWordofString(String search, String name) {
        String regex = "(?i)\\b" + search + ".*?\\b";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return m.find();

    }

    public String getFirstLetters(String text) {
        String firstLetters = "";
        text = text.replaceAll("[.,]", ""); // Replace dots, etc (optional)
        for (String s : text.split(" ")) {
            firstLetters += s.charAt(0);
        }
        return firstLetters;
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

    public void detachProgress(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (customProgressBar != null && customProgressBar.isShowing()) {
                        customProgressBar.dismiss();
                    }
                    customProgressBar = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void LogDetails(String title, String content) {
        try {

            System.out.println("Mounika Reddy  " + title + " data === " + content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String capitalize(String capString) {

        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public boolean call(Context context, String phone_num) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                context.startActivity(callIntent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void callConfirmation(Context context, String mobileNumber) {
        try {
            DialogCallConfirmation dialogCallConfirmation = new DialogCallConfirmation(context, mobileNumber);
            dialogCallConfirmation.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkContactPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> getTheDateStringList(String s_date, String e_date) {
        try {
            if (s_date != null && !s_date.trim().isEmpty() && !s_date.trim().equals("null") && e_date != null && !e_date.trim().isEmpty() && !e_date.trim().equals("null")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeKeyBoard(Context context, View view) {
        try {

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String replaceSpecialCharacters(String message) {
        //.replace("\u0022",""")
        String convertMessage = message;
        convertMessage = convertMessage
                .replace("\u0021", "!")
                .replace("\u0023", "#")
                .replace("\u0024", "$")
                .replace("\u0025", "%")
                .replace("\u0026", "&")
                .replace("\u0026", "&")
                .replace("\u0027", "'")
                .replace("\u0028", "(")
                .replace("\u0029", ")")
                .replace("\u002A", "*")
                .replace("\u002B", "+")
                .replace("\u002C", ",")
                .replace("\u002D", "-")
                .replace("\u002E", ".")
                .replace("\u002F", "/")
                .replace("\u003A", ":")
                .replace("\u003B", ";")
                .replace("\u003C", "<")
                .replace("\u003D", "=")
                .replace("\u003E", ">")
                .replace("\u003F", "?")
                .replace("\u0040", "@")
                .replace("\u005B", "[")
                // .replace("\u005C","/")
                .replace("\u005D", "]")
                .replace("\u005E", "^")
                .replace("\u005F", "_")
                .replace("\u0060", "`")
                .replace("\u007B", "{")
                .replace("\u007C", "|")
                .replace("\u007D", "}")
                .replace("\u007E", "~")
        ;
        return convertMessage.trim();
    }

    public String replaceString(String message) {
        String convertMessage = message;
        convertMessage = convertMessage.replace(" &lsquo; ", "‘").replace(" :\u000f ", "☺️").replace(" \u0017 ", "\uD83D\uDE17")
                .replace(" &bull; ", "•").replace(" &pi; ", "π").replace(" &radic; ", "√").replace(" &div; ", "÷").replace(" &times; ", "×").replace(" &para; ", "¶").replace(" ∆&pound; ", "∆")
                .replace(" &ldquo; ", "").replace(" &nbsp; ", " ")
                .replace(" &cent; ", "£").replace(" &euro; ", "¢").replace(" &yen; ", "¥").replace(" &deg; ", "°")
                .replace(" &copy; ", "©").replace(" &circledR; ", "®").replace(" &trade; ", "™").replace(" &check; ", "✓")
                .replace(" &gt; ", ">").replace(" &lt; ", "<").replace(" &apos; ", "'").replace(" &quot; ", "\"")
                .replace(" &amp; ", "&").replace(" &apos; ", "'").replace(" &quot; ", "\"").replace(" &lt; ", "<").replace(" &gt; ", ">").replace(" &#10; ", "\n").replace(" &nbsp; ", " ")
                .replace(" \u0003 ", " \uD83D\uDE03").replace(" \u0004 ", "\uD83D\uDE04").replace(" \u0001 ", "\uD83D\uDE01")
                .replace(" \u0006 ", "\uD83D\uDE06").replace(" \u0000 ", "\uD83D\uDE00").replace(" 9\u000f ", "☹️")
                .replace(" \u0015 ", "\uD83D\uDE15")
                .replace(" \u0019 ", " \uD83D\uDE19").replace(" \u0016 ", " \uD83D\uDE16").replace("\u0011 ", "\uD83E\uDD11")
                .replace(" :smile: ", "\uD83D\uDE04").replace(" :-P ", "\uD83D\uDE1B").replace(" =P ", "\uD83D\uDE1B")
                .replace(" :blush: ", "\uD83D\uDE0A").replace(" :smiley: ", "\uD83D\uDE03")
                .replace(" :) ", "\uD83D\uDE42").replace(" :-) ", "\uD83D\uDE42").replace(" :smirk: ", "\uD83D\uDE0F").replace(" :slight_smile: ", "\uD83D\uDE42").replace(" =] ", "\uD83D\uDE42")
                .replace(" =) ", "\uD83D\uDE42").replace(" :] ", "\uD83D\uDE42")
                .replace(" ':) ", "\uD83D\uDE05").replace(" :sweat_smile: ", "\uD83D\uDE05").replace(" ':-) ", "\uD83D\uDE05").replace(" '=) ", "\uD83D\uDE05").replace(" ':D ", "\uD83D\uDE05")
                .replace(" ':-D ", "\uD83D\uDE05").replace(" '=D ", "\uD83D\uDE05")
                .replace(" :/ ", "\uD83D\uDE15").replace(" :disappointed: ", "\uD83D\uDE1E").replace(" :( ", "\uD83D\uDE1E")
                .replace(" :heart_eyes: ", "\uD83D\uDE0D").replace(" :kissing_heart: ", "\uD83D\uDE18")
                .replace(" :kissing_closed_eyes: ", "\uD83D\uDE1A").replace(" :flushed: ", "\uD83D\uDE33")
                .replace(" :laughing: ", "\uD83D\uDE06").replace(" :D ", "\uD83D\uDE03").replace(" :-D ", "\uD83D\uDE03").replace(" =D ", "\uD83D\uDE03").replace(" >:) ", "\uD83D\uDE06").replace(" >;) ", "\uD83D\uDE06")
                .replace(" >:-) ", "\uD83D\uDE06").replace(" >=) ", "\uD83D\uDE06").replace(" :wink: ", "\uD83D\uDE09").replace(" ;-) ", "\uD83D\uDE09").replace(" *-) ", "\uD83D\uDE09").replace(" *) ", "\uD83D\uDE09")
                .replace(" ;-] ", "\uD83D\uDE09").replace(" ;] ", "\uD83D\uDE09").replace(" ;D ", "\uD83D\uDE09").replace(" ;^ ) ", "\uD83D\uDE09")
                .replace(" :sweat: ", "\uD83D\uDE13").replace(" ':( ", "\uD83D\uDE13").replace("' :-( ", "\uD83D\uDE13").replace(" '=( ", "\uD83D\uDE13")
                .replace(" :kissing_heart: ", "\uD83D\uDE18").replace(" :* ", "\uD83D\uDE18").replace(" :-* ", "\uD83D\uDE18").replace(" =* ", "\uD83D\uDE18").replace(" :^* ", "\uD83D\uDE18")
                .replace(" :heart: ", "❤️").replace(" <3 ", "❤").replace(" :') ", "\uD83D\uDE02")
                .replace(" :joy: ", "\uD83D\uDE02").replace(" :'-) ", "\uD83D\uDE02")
                .replace(" :stuck_out_tongue_winking_eye: ", "\uD83D\uDE1C").replace(" >:P ", "\uD83D\uDE1C").replace(" X-P ", "\uD83D\uDE1C").replace(" x-p ", "\uD83D\uDE1C")
                .replace(" :disappointed: ", "\uD83D\uDE1E").replace(" >:[ ", "\uD83D\uDE1E").replace(" :-( ", "\uD83D\uDE1E").replace(" :( ", "\uD83D\uDE1E").replace(" :-[ ", "\uD83D\uDE1E").replace(" :[ ", "\uD83D\uDE1E").replace(" =( ", "\uD83D\uDE1E")
                .replace(" :angry: ", "\uD83D\uDE20").replace(" >:( ", "\uD83D\uDE20").replace(" >:-( ", "\uD83D\uDE20").replace(" :@ ", "\uD83D\uDE20")
                .replace(" :cry: ", "\uD83D\uDE22").replace(" :'( ", "\uD83D\uDE22").replace(" :'-( ", "\uD83D\uDE22").replace(" ;( ", "\uD83D\uDE22").replace(" ;-( ", "\uD83D\uDE22")
                .replace(" :persevere: ", "\uD83D\uDE23").replace(" >.< ", "\uD83D\uDE23")
                .replace(" :fearful: ", "\uD83D\uDE28").replace(" :flushed: ", "\uD83D\uDE33").replace(" :$ ", "\uD83D\uDE33").replace(" =$ ", "\uD83D\uDE33")
                .replace(" :dizzy_face: ", "\uD83D\uDE35").replace(" #-) ", "\uD83D\uDE35").replace("  #) ", "\uD83D\uDE35").replace(" %-) ", "\uD83D\uDE35").replace(" %) ", "\uD83D\uDE35").replace(" X) ", "\uD83D\uDE35").replace(" X-) ", "\uD83D\uDE35")
                .replace(" :ok_woman: ", "\uD83D\uDE46").replace(" *\\0 ", "\uD83D\uDE46").replace(" \\0/ ", "\uD83D\uDE46").replace(" *\\O ", "\uD83D\uDE46").replace(" \\O/ ", "\uD83D\uDE46")
                .replace(" :innocent: ", "\uD83D\uDE07").replace(" O:-) ", "\uD83D\uDE07").replace(" 0:-3 ", "\uD83D\uDE07").replace(" 0:3 ", "\uD83D\uDE07").replace(" 0:-) ", "\uD83D\uDE07").replace(" 0:) ", "\uD83D\uDE07").replace(" 0;^) ", "\uD83D\uDE07").replace(" O:-) ", "\uD83D\uDE07").replace(" O:) ", "\uD83D\uDE07").replace(" O;-) ", "\uD83D\uDE07").replace(" O=) ", "\uD83D\uDE07").replace(" 0;-) ", "\uD83D\uDE07").replace(" O:-3 ", "\uD83D\uDE07").replace(" O:3 ", "\uD83D\uDE07")
                .replace(" :sunglasses: ", "\uD83D\uDE0E").replace(" B-) ", "\uD83D\uDE0E").replace(" B) ", "\uD83D\uDE0E").replace(" 8) ", "\uD83D\uDE0E").replace(" 8-) ", "\uD83D\uDE0E").replace(" B-D ", "\uD83D\uDE0E").replace(" 8-D ", "\uD83D\uDE0E")
                .replace(" :expressionless: ", "\uD83D\uDE11").replace(" -_- ", "\uD83D\uDE11").replace(" -__- ", "\uD83D\uDE11").replace(" -___- ", "\uD83D\uDE11")
                .replace(" :confused: ", "\uD83D\uDE15").replace(" >:\\ ", "\uD83D\uDE15").replace(" >:/ ", "\uD83D\uDE15").replace(" :-/ ", "\uD83D\uDE15").replace(" :-. ", "\uD83D\uDE15").replace(" :/ ", "\uD83D\uDE15").replace(" :\\ ", "\uD83D\uDE15").replace(" =/ ", "\uD83D\uDE15").replace(" =\\ ", "\uD83D\uDE15").replace(" :L ", "\uD83D\uDE15").replace(" =L ", "\uD83D\uDE15")
                .replace(" :stuck_out_tongue: ", "\uD83D\uDE1B").replace(" :P ", "\uD83D\uDE1B").replace(" :-P ", "\uD83D\uDE1B").replace(" =P ", "\uD83D\uDE1B").replace(" :-p ", "\uD83D\uDE1B").replace(" :p ", "\uD83D\uDE1B").replace(" =p ", "\uD83D\uDE1B").replace(" :-Þ ", "\uD83D\uDE1B").replace(" :Þ ", "\uD83D\uDE1B").replace(" :þ ", "\uD83D\uDE1B").replace(" :-þ ", "\uD83D\uDE1B").replace(" :-b ", "\uD83D\uDE1B").replace(" :b ", "\uD83D\uDE1B").replace(" D: ", "\uD83D\uDE1B")
                .replace(" :open_mouth: ", "\uD83D\uDE2E").replace(" :-O ", "\uD83D\uDE2E").replace(" :O ", "\uD83D\uDE2E").replace(" :-o ", "\uD83D\uDE2E").replace(" :o ", "\uD83D\uDE2E").replace(" O_O ", "\uD83D\uDE2E").replace(" >:O ", "\uD83D\uDE2E")
                .replace(" :no_mouth: ", "\uD83D\uDE36").replace(" :-X ", "\uD83D\uDE36").replace(" :X ", "\uD83D\uDE36").replace(" :-# ", "\uD83D\uDE36").replace(" :# ", "\uD83D\uDE36").replace(" =X ", "\uD83D\uDE36").replace(" =x ", "\uD83D\uDE36").replace(" :x ", "\uD83D\uDE36").replace(" :-x ", "\uD83D\uDE36").replace(" =# ", "\uD83D\uDE36")
                .replace(" </3 ", "\uD83D\uDC94").replace(" :broken_heart: ", "\uD83D\uDC94")
                .replace(" \t ", "\uD83D\uDE09").replace(" \u001f ", "\uD83D\uDE1F")
                .replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :kiss_mm: ", "\uD83D\uDC68\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC68")
                .replace(" :kiss_woman_man: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC68")
                .replace(" :woman_zombie: ", "\uD83E\uDDDF\u200D♀️").replace(" :man_zombie: ", "\uD83E\uDDDF\u200D♂️")
                .replace(" :kiss_ww: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC69")
                .replace(" :family_mmbb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_mmgb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_mmgg: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_mwbb: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_mwgb: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_mwgg: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_wwbb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_wwgb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_wwgg: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :couple_mm: ", "\uD83D\uDC68\u200D❤️\u200D\uD83D\uDC68").replace(" :couple_with_heart_woman_man: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68")
                .replace(" :couple_ww: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC69").replace(" :family_man_boy_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_man_girl_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_man_girl_girl: ", "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67").replace(" :family_man_woman_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66")
                .replace(" :family_mmb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66").replace(" :family_mmg: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67")
                .replace(" :family_mwg: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67").replace(" :family_woman_boy_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_woman_girl_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66").replace(" :family_woman_girl_girl: ", "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_wwb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66").replace(" :family_wwg: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67")
                .replace(" :blond-haired_man_tone1: ", "\uD83D\uDC71\uD83C\uDFFB\u200D♂️").replace(" :blond-haired_woman_tone1: ", "\uD83D\uDC71\uD83C\uDFFB\u200D♀️")
                .replace(" :man_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB\u200D♂️").replace(" :man_construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB\u200D♂️")
                .replace(" :man_detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB\u200D♂️").replace(" :man_elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB\u200D♂️")
                .replace(" :man_facepalming_tone1: ", "\uD83E\uDD26\uD83C\uDFFB\u200D♂️").replace(" :man_fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB\u200D♂️")
                .replace(" :man_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB\u200D♂️").replace(" :man_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB\u200D♂️")
                .replace(" :man_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB\u200D♂️").replace(" :man_getting_face_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB\u200D♂️")
                .replace(" :man_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB\u200D♂️").replace(" :man_guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB\u200D♂️")
                .replace(" :man_health_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D⚕️").replace(" :man_judge_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D⚖️")
                .replace(" :man_mage_tone1: ", "\uD83E\uDDD9\uD83C\uDFFB\u200D♂️").replace(" :man_pilot_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D✈️")
                .replace(" :man_police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB\u200D♂️").replace(" :man_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB\u200D♂️")
                .replace(" :man_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB\u200D♂️").replace(" :man_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB\u200D♂️")
                .replace(" :man_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB\u200D♂️")
                .replace(" :person_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB").replace(" :bath_tone1: ", "\uD83D\uDEC0\uD83C\uDFFB")
                .replace(" :person_in_bed_tone1: ", "\uD83D\uDECC\uD83C\uDFFB").replace(" :snowboarder_tone1: ", "\uD83C\uDFC2\uD83C\uDFFB")
                .replace(" :person_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB").replace(" :man_surfing: ", "\uD83C\uDFC4\u200D♂️")
                .replace(" :man_swimming: ", "\uD83C\uDFCA\u200D♂️").replace(" :men_wrestling: ", "\uD83E\uDD3C\u200D♂️").replace(" :woman_biking: ", "\uD83D\uDEB4\u200D♀️")
                .replace(" :woman_cartwheeling: ", "\uD83E\uDD38\u200D♀️").replace(" :woman_climbing: ", "\uD83E\uDDD7\u200D♀️")
                .replace(" :woman_in_lotus_position: ", "\uD83E\uDDD8\u200D♀️").replace(" :woman_in_steamy_room: ", "\uD83E\uDDD6\u200D♀️")
                .replace(" :woman_juggling: ", "\uD83E\uDD39\u200D♀️").replace(" :woman_mountain_biking: ", "\uD83D\uDEB5\u200D♀️")
                .replace(" :woman_playing_handball: ", "\uD83E\uDD3E\u200D♀️").replace(" :woman_playing_water_polo: ", "\uD83E\uDD3D\u200D♀️")
                .replace(" :woman_rowing_boat: ", "\uD83D\uDEA3\u200D♀️").replace(" :woman_surfing: ", "\uD83C\uDFC4\u200D♀️").replace(" :woman_swimming: ", "\uD83C\uDFCA\u200D♀️")
                .replace(" :women_wrestling: ", "\uD83E\uDD3C\u200D♀️").replace(" :breast_feeding_tone1: ", "\uD83E\uDD31\uD83C\uDFFB").replace(" :horse_racing_tone1: ", "\uD83C\uDFC7\uD83C\uDFFB")
                .replace(" :person_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB").replace(" :person_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB").replace(" :person_doing_cartwheel_tone1: ", "\uD83E\uDD38\uD83C\uDFFB")
                .replace(" :person_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB").replace(" :person_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB")
                .replace(" :person_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB").replace(" :person_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB")
                .replace(" :person_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB").replace(" :person_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB")
                .replace(" :person_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB").replace(" :person_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB")
                .replace(" :person_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB").replace(" :person_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB")
                .replace(" :person_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB").replace(" :man_rowing_boat: ", "\uD83D\uDEA3\u200D♂️").replace(" :man_playing_water_polo: ", "\uD83E\uDD3D\u200D♂️").replace(" :man_playing_handball: ", "\uD83E\uDD3E\u200D♂️")
                .replace(" :man_mountain_biking: ", "\uD83D\uDEB5\u200D♂️").replace(" :man_juggling: ", "\uD83E\uDD39\u200D♂️").replace(" :man_in_steamy_room: ", "\uD83E\uDDD6\u200D♂️")
                .replace(" :man_in_lotus_position: ", "\uD83E\uDDD8\u200D♂️").replace(" :man_climbing: ", "\uD83E\uDDD7\u200D♂️").replace(" :man_cartwheeling: ", "\uD83E\uDD38\u200D♂️")
                .replace(" :man_biking: ", "\uD83D\uDEB4\u200D♂️").replace(" :woman_bouncing_ball: ", "⛹️\u200D♀️").replace(" :man_bouncing_ball: ", "⛹️\u200D♂️")
                .replace(" :woman_lifting_weights: ", "\uD83C\uDFCB️\u200D♀️").replace(" :woman_golfing: ", "\uD83C\uDFCC️\u200D♀️").replace(" :woman_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB\u200D♀️")
                .replace(" :man_lifting_weights: ", "\uD83C\uDFCB️\u200D♂️").replace(" :man_golfing: ", "\uD83C\uDFCC️\u200D♂️").replace(" :man_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB\u200D♂️")
                .replace(" :woman_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB\u200D♀️").replace(" :woman_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB\u200D♀️").replace(" :woman_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB\u200D♀️").replace(" :woman_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB\u200D♀️").replace(" :woman_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB\u200D♀️").replace(" :woman_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB\u200D♀️").replace(" :woman_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB\u200D♀️").replace(" :woman_cartwheeling_tone1: ", "\uD83E\uDD38\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB\u200D♀️").replace(" :man_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB\u200D♂️").replace(" :man_cartwheeling_tone1: ", "\uD83E\uDD38\uD83C\uDFFB\u200D♂️")
                .replace(" :man_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB\u200D♂️").replace(" :man_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB\u200D♂️").replace(" :man_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB\u200D♂️")
                .replace(" :man_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB\u200D♂️").replace(" :man_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB\u200D♂️").replace(" :man_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB\u200D♂️")
                .replace(" :man_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB\u200D♂️").replace(" :man_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB\u200D♂️").replace(" :man_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB\u200D♂️")
                .replace(" :man_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB\u200D♂️").replace(" :man_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB\u200D♂️").replace(" :man_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB\u200D♂️")
                .replace(" &circledR; ", " ®️ ").replace(" &spades; ", " ♠️ ").replace(" &trade; ", " ™️ ").replace(" &EmptySmallSquare; ", " ◻️ ")
                .replace(" &EmptyVerySmallSquare; ", "️ ▫ ").replace(" &hookleftarrow; ", " ↩️️ ").replace(" &harr; ", " ↔ ").replace(" &hearts; ", " ♥️ ")
                .replace(" &diamondsuit; ️", " ♦️ ").replace(" &copy; ", " ©️ ").replace(" &clubs; ", " ♣️ ").replace(" :two: ", "2️⃣")
                .replace(" :zero: ", "0️⃣").replace(" &cudarrr; ", " ⤵️ ").replace(" &LowerLeftArrow; ", " ↙️️ ").replace(" &LowerRightArrow;️ ", " ↘ ")
                .replace(" &hookrightarrow; ", " ↪️ ").replace(" &updownarrow; ", " ↕️ ️").replace(" &nearr; ", " ↗ ️").replace(" &nwarr; ", " ↖ ").replace(" &FilledSmallSquare; ", " ◼️️ ").replace(" &blacksquare; ", " ▪️ ").replace(" &blacksquare; ", " ▪ ")
                .replace(" :three: ", "3️⃣").replace(" :six: ", "6️⃣").replace(" :seven: ", "7️⃣").replace(" :one: ", "1️⃣").replace(" :nine: ", "9️⃣").replace(" :hash: ", "#️⃣").replace(" :four: ", "4️⃣").replace(" :five: ", "5️⃣").replace(" :eight: ", "8️⃣").replace(" :asterisk: ", "*️⃣")
                .replace(" :eye_in_speech_bubble: ", "\uD83D\uDC41️\u200D\uD83D\uDDE8️").replace(" :united_nations: ", "\uD83C\uDDFA\uD83C\uDDF3")
                .replace(" :flag_zw: ", "\uD83C\uDDFF\uD83C\uDDFC").replace(" :flag_za: ", "\uD83C\uDDFF\uD83C\uDDF2").replace(" :flag_yt: ", "\uD83C\uDDFF\uD83C\uDDE6")
                .replace(" :flag_ye: ", "\uD83C\uDDFE\uD83C\uDDF9").replace(" :flag_xk: ", "\uD83C\uDDFE\uD83C\uDDEA").replace(" :flag_ws: ", "\uD83C\uDDFD\uD83C\uDDF0")
                .replace(" :flag_wf: ", "\uD83C\uDDFC\uD83C\uDDEB").replace(" :flag_vu: ", "\uD83C\uDDFB\uD83C\uDDFA").replace(" :flag_vn: ", "\uD83C\uDDFB\uD83C\uDDF3")
                .replace(" :flag_vi: ", "\uD83C\uDDFB\uD83C\uDDEE").replace(" :flag_vg: ", "\uD83C\uDDFB\uD83C\uDDEC").replace(" :flag_ve: ", "\uD83C\uDDFB\uD83C\uDDEA")
                .replace(" :flag_vc: ", "\uD83C\uDDFB\uD83C\uDDE8").replace(" :flag_va: ", "\uD83C\uDDFB\uD83C\uDDE6").replace(" :flag_sn: ", "\uD83C\uDDF8\uD83C\uDDF3")
                .replace(" :flag_so: ", "\uD83C\uDDF8\uD83C\uDDF4").replace(" :flag_sr: ", "\uD83C\uDDF8\uD83C\uDDF7").replace(" :flag_ss: ", "\uD83C\uDDF8\uD83C\uDDF8")
                .replace(" :flag_st: ", "\uD83C\uDDF8\uD83C\uDDF9").replace(" :flag_sv: ", "\uD83C\uDDF8\uD83C\uDDFB").replace(" :flag_sx: ", "\uD83C\uDDF8\uD83C\uDDFD")
                .replace(" :flag_sy: ", "\uD83C\uDDF8\uD83C\uDDFE").replace(" :flag_sz: ", "\uD83C\uDDF8\uD83C\uDDFF").replace(" :flag_ta: ", "\uD83C\uDDF9\uD83C\uDDE6")
                .replace(" :flag_tc: ", "\uD83C\uDDF9\uD83C\uDDE8").replace(" :flag_td: ", "\uD83C\uDDF9\uD83C\uDDE9").replace(" :flag_tf: ", "\uD83C\uDDF9\uD83C\uDDEB")
                .replace(" :flag_tg: ", "\uD83C\uDDF9\uD83C\uDDEC").replace(" :flag_th: ", "\uD83C\uDDF9\uD83C\uDDED").replace(" :flag_tj: ", "\uD83C\uDDF9\uD83C\uDDEF")
                .replace(" :flag_tk: ", "\uD83C\uDDF9\uD83C\uDDF0").replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :flag_tm: ", "\uD83C\uDDF9\uD83C\uDDF2")
                .replace(" :flag_tn: ", "\uD83C\uDDF9\uD83C\uDDF3").replace(" :flag_to: ", "\uD83C\uDDF9\uD83C\uDDF4").replace(" :flag_tr: ", "\uD83C\uDDF9\uD83C\uDDF7")
                .replace(" :flag_tt: ", "\uD83C\uDDF9\uD83C\uDDF9").replace(" :flag_tv: ", "\uD83C\uDDF9\uD83C\uDDFB").replace(" :flag_tw: ", "\uD83C\uDDF9\uD83C\uDDFC")
                .replace(" :flag_tz: ", "\uD83C\uDDF9\uD83C\uDDFF").replace(" :flag_ua: ", "\uD83C\uDDFA\uD83C\uDDE6").replace(" :flag_ug: ", "\uD83C\uDDFA\uD83C\uDDEC")
                .replace(" :flag_um: ", "\uD83C\uDDFA\uD83C\uDDF2").replace(" :flag_us: ", "\uD83C\uDDFA\uD83C\uDDF8").replace(" :flag_uy: ", "\uD83C\uDDFA\uD83C\uDDFE")
                .replace(" :flag_uz: ", "\uD83C\uDDFA\uD83C\uDDFF").replace(" :flag_tg: ", "\uD83C\uDDF9\uD83C\uDDEC").replace(" :flag_th: ", "\uD83C\uDDF9\uD83C\uDDED").replace(" :flag_tj: ", "\uD83C\uDDF9\uD83C\uDDEF").replace(" :flag_tk: ", "\uD83C\uDDF9\uD83C\uDDF0").replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :flag_tm: ", "\uD83C\uDDF9\uD83C\uDDF2").replace(" :flag_tn: ", "\uD83C\uDDF9\uD83C\uDDF3")
                .replace(" :flag_sm: ", "\uD83C\uDDF8\uD83C\uDDF2").replace(" :flag_sl: ", "\uD83C\uDDF8\uD83C\uDDF1").replace(" :flag_sk: ", "\uD83C\uDDF8\uD83C\uDDF0").replace(" :flag_sj: ", "\uD83C\uDDF8\uD83C\uDDEF").replace(" :flag_si: ", "\uD83C\uDDF8\uD83C\uDDEE").replace(" :flag_sh: ", "\uD83C\uDDF8\uD83C\uDDED").replace(" :flag_sg: ", "\uD83C\uDDF8\uD83C\uDDEC").replace(" :flag_se: ", "\uD83C\uDDF8\uD83C\uDDEA").replace(" :flag_sd: ", "\uD83C\uDDF8\uD83C\uDDE9").replace(" :flag_sc: ", "\uD83C\uDDF8\uD83C\uDDE8").replace(" :flag_sb: ", "\uD83C\uDDF8\uD83C\uDDE7").replace(" :flag_sa: ", "\uD83C\uDDF8\uD83C\uDDE6").replace(" :flag_sm: ", "\uD83C\uDDF8\uD83C\uDDE6").replace(" :flag_rw: ", "\uD83C\uDDF7\uD83C\uDDFC").replace(" :flag_ru: ", "\uD83C\uDDF7\uD83C\uDDFA").replace(" :flag_rs: ", "\uD83C\uDDF7\uD83C\uDDF8").replace(" :flag_ro: ", "\uD83C\uDDF7\uD83C\uDDF4").replace(" :flag_re: ", "\uD83C\uDDF7\uD83C\uDDEA").replace(" :flag_qa: ", "\uD83C\uDDF6\uD83C\uDDE6").replace(" :flag_py: ", "\uD83C\uDDF5\uD83C\uDDFE")
                .replace(" :flag_pw: ", "\uD83C\uDDF5\uD83C\uDDFC").replace(" :flag_pt: ", "\uD83C\uDDF5\uD83C\uDDF9").replace(" :flag_ps: ", "\uD83C\uDDF5\uD83C\uDDF8").replace(" :flag_pr: ", "\uD83C\uDDF5\uD83C\uDDF7").replace(" :flag_pn: ", "\uD83C\uDDF5\uD83C\uDDF3").replace(" :flag_pm: ", "\uD83C\uDDF5\uD83C\uDDF2").replace(" :flag_pl: ", "\uD83C\uDDF5\uD83C\uDDF1").replace(" :flag_pk: ", "\uD83C\uDDF5\uD83C\uDDF0").replace(" :flag_ph: ", "\uD83C\uDDF5\uD83C\uDDED").replace(" :flag_pg: ", "\uD83C\uDDF5\uD83C\uDDEC").replace(" :flag_pf: ", "\uD83C\uDDF5\uD83C\uDDEB").replace(" :flag_pe: ", "\uD83C\uDDF5\uD83C\uDDEA").replace(" :flag_pa: ", "\uD83C\uDDF5\uD83C\uDDE6")
                .replace(" :flag_mt: ", "\uD83C\uDDF2\uD83C\uDDF9").replace(" :flag_om: ", "\uD83C\uDDF4\uD83C\uDDF2").replace(" :flag_nz: ", "\uD83C\uDDF3\uD83C\uDDFF").replace(" :flag_nu: ", "\uD83C\uDDF3\uD83C\uDDFA").replace(" :flag_nr: ", "\uD83C\uDDF3\uD83C\uDDF7").replace(" :flag_np: ", "\uD83C\uDDF3\uD83C\uDDF5").replace(" :flag_no: ", "\uD83C\uDDF3\uD83C\uDDF4").replace(" :flag_nl: ", "\uD83C\uDDF3\uD83C\uDDF1").replace(" :flag_ni: ", "\uD83C\uDDF3\uD83C\uDDEE").replace(" :flag_ng: ", "\uD83C\uDDF3\uD83C\uDDEC").replace(" :flag_ne: ", "\uD83C\uDDF3\uD83C\uDDEA").replace(" :flag_nc: ", "\uD83C\uDDF3\uD83C\uDDE8").replace(" :flag_na: ", "\uD83C\uDDF3\uD83C\uDDE6").replace(" :flag_mz: ", "\uD83C\uDDF2\uD83C\uDDFF").replace(" :flag_my: ", "\uD83C\uDDF2\uD83C\uDDFE").replace(" :flag_mx: ", "\uD83C\uDDF2\uD83C\uDDFD").replace(" :flag_mw: ", "\uD83C\uDDF2\uD83C\uDDFC").replace(" :flag_mv: ", "\uD83C\uDDF2\uD83C\uDDFB").replace(" :flag_mu: ", "\uD83C\uDDF2\uD83C\uDDFA")
                .replace(" :flag_ms: ", "\uD83C\uDDF2\uD83C\uDDF8").replace(" :flag_mr: ", "\uD83C\uDDF2\uD83C\uDDF7").replace(" :flag_mq: ", "\uD83C\uDDF2\uD83C\uDDF6").replace(" :flag_mp: ", "\uD83C\uDDF2\uD83C\uDDF5").replace(" :flag_mo: ", "\uD83C\uDDF2\uD83C\uDDF4").replace(" :flag_mn: ", "\uD83C\uDDF2\uD83C\uDDF3").replace(" :flag_mm: ", "\uD83C\uDDF2\uD83C\uDDF2").replace(" :flag_ml: ", "\uD83C\uDDF2\uD83C\uDDF1").replace(" :flag_mk: ", "\uD83C\uDDF2\uD83C\uDDF0").replace(" :flag_mh: ", "\uD83C\uDDF2\uD83C\uDDED").replace(" :flag_mg: ", "\uD83C\uDDF2\uD83C\uDDEC").replace(" :flag_mf: ", "\uD83C\uDDF2\uD83C\uDDEB")
                .replace(" :flag_me: ", "\uD83C\uDDF2\uD83C\uDDEA").replace(" :flag_md: ", "\uD83C\uDDF2\uD83C\uDDE9").replace(" :flag_mc: ", "\uD83C\uDDF2\uD83C\uDDE8").replace(" :flag_ma: ", "\uD83C\uDDF2\uD83C\uDDE6").replace(" :flag_ly: ", "\uD83C\uDDF1\uD83C\uDDFE").replace(" :flag_lv: ", "\uD83C\uDDF1\uD83C\uDDFB").replace(" :flag_lu: ", "\uD83C\uDDF1\uD83C\uDDFA").replace(" :flag_lt: ", "\uD83C\uDDF1\uD83C\uDDF9").replace(" :flag_ls: ", "\uD83C\uDDF1\uD83C\uDDF8").replace(" :flag_lr: ", "\uD83C\uDDF1\uD83C\uDDF7").replace(" :flag_lk: ", "\uD83C\uDDF1\uD83C\uDDF0").replace(" :flag_li: ", "\uD83C\uDDF1\uD83C\uDDEE").replace(" :flag_lc: ", "\uD83C\uDDF1\uD83C\uDDE8").replace(" :flag_lb: ", "\uD83C\uDDF1\uD83C\uDDE7").replace(" :flag_la: ", "\uD83C\uDDF1\uD83C\uDDE6")
                .replace(" :flag_kz:  ", "\uD83C\uDDF0\uD83C\uDDFF").replace(" :flag_ky: ", "\uD83C\uDDF0\uD83C\uDDFE").replace(" :flag_kw: ", "\uD83C\uDDF0\uD83C\uDDFC").replace(" :flag_kr: ", "\uD83C\uDDF0\uD83C\uDDF7").replace(" :flag_kp: ", "\uD83C\uDDF0\uD83C\uDDF5").replace(" :flag_kn: ", "\uD83C\uDDF0\uD83C\uDDF3").replace(" :flag_km: ", "\uD83C\uDDF0\uD83C\uDDF2").replace(" :flag_ki: ", "\uD83C\uDDF0\uD83C\uDDEE").replace(" :flag_kh: ", "\uD83C\uDDF0\uD83C\uDDED").replace(" :flag_kg: ", "\uD83C\uDDF0\uD83C\uDDEC").replace(" :flag_ke: ", "\uD83C\uDDF0\uD83C\uDDEA").replace(" :flag_jp: ", "\uD83C\uDDEF\uD83C\uDDF5").replace(" :flag_jo: ", "\uD83C\uDDEF\uD83C\uDDF4").replace(" :flag_jm: ", "\uD83C\uDDEF\uD83C\uDDF2").replace(" :flag_je: ", "\uD83C\uDDEF\uD83C\uDDEA")
                .replace(" :flag_it: ", "\uD83C\uDDEE\uD83C\uDDF9").replace(" :flag_is: ", "\uD83C\uDDEE\uD83C\uDDF8")
                .replace(" :flag_gd: ", "\uD83C\uDDEC\uD83C\uDDE9").replace(" :flag_ge: ", "\uD83C\uDDEC\uD83C\uDDEA").replace(" :flag_gf: ", "\uD83C\uDDEC\uD83C\uDDEB").replace(" :flag_gg: ", "\uD83C\uDDEC\uD83C\uDDEC").replace(" :flag_gh: ", "\uD83C\uDDEC\uD83C\uDDED").replace(" :flag_gi: ", "\uD83C\uDDEC\uD83C\uDDEE").replace(" :flag_gl: ", "\uD83C\uDDEC\uD83C\uDDF1").replace(" :flag_gm: ", "\uD83C\uDDEC\uD83C\uDDF2").replace(" :flag_gn: ", "\uD83C\uDDEC\uD83C\uDDF3").replace(" :flag_gp: ", "\uD83C\uDDEC\uD83C\uDDF5").replace(" :flag_gq: ", "\uD83C\uDDEC\uD83C\uDDF6").replace(" :flag_gr: ", "\uD83C\uDDEC\uD83C\uDDF7").replace(" :flag_gs: ", "\uD83C\uDDEC\uD83C\uDDF8").replace(" :flag_gt: ", "\uD83C\uDDEC\uD83C\uDDF9").replace(" :flag_gu: ", "\uD83C\uDDEC\uD83C\uDDFA").replace(" :flag_gw: ", "\uD83C\uDDEC\uD83C\uDDFC").replace(" :flag_gy: ", "\uD83C\uDDEC\uD83C\uDDFE").replace(" :flag_hk: ", "\uD83C\uDDED\uD83C\uDDF0").replace(" :flag_hm: ", "\uD83C\uDDED\uD83C\uDDF2")
                .replace(" :flag_hn: ", "\uD83C\uDDED\uD83C\uDDF3").replace(" :flag_hr: ", "\uD83C\uDDED\uD83C\uDDF7").replace(" :flag_ht: ", "\uD83C\uDDED\uD83C\uDDF9").replace(" :flag_hu: ", "\uD83C\uDDED\uD83C\uDDFA").replace(" :flag_ic: ", "\uD83C\uDDEE\uD83C\uDDE8").replace(" :flag_id: ", "\uD83C\uDDEE\uD83C\uDDE9").replace(" :flag_ie: ", "\uD83C\uDDEE\uD83C\uDDEA").replace(" :flag_il: ", "\uD83C\uDDEE\uD83C\uDDF1").replace(" :flag_im: ", "\uD83C\uDDEE\uD83C\uDDF2").replace(" :flag_in: ", "\uD83C\uDDEE\uD83C\uDDF3").replace(" :flag_io: ", "\uD83C\uDDEE\uD83C\uDDF4").replace(" :flag_iq: ", "\uD83C\uDDEE\uD83C\uDDF6").replace(" :flag_ir: ", "\uD83C\uDDEE\uD83C\uDDF7").replace(" :flag_gb: ", "\uD83C\uDDEC\uD83C\uDDE7").replace(" :flag_ga: ", "\uD83C\uDDEC\uD83C\uDDE6").replace(" :flag_fr: ", "\uD83C\uDDEB\uD83C\uDDF7").replace(" :flag_fo: ", "\uD83C\uDDEB\uD83C\uDDF4").replace(" :flag_fm: ", "\uD83C\uDDEB\uD83C\uDDF2").replace(" :flag_fk: ", "\uD83C\uDDEB\uD83C\uDDF0")
                .replace(" :flag_fj: ", "\uD83C\uDDEB\uD83C\uDDEF").replace(" :flag_fi: ", "\uD83C\uDDEB\uD83C\uDDEE").replace(" :flag_eu: ", "\uD83C\uDDEA\uD83C\uDDFA").replace(" :flag_et: ", "\uD83C\uDDEA\uD83C\uDDF9").replace(" :flag_es: ", "\uD83C\uDDEA\uD83C\uDDF8").replace(" :flag_er:", "\uD83C\uDDEA\uD83C\uDDF7").replace(" :flag_eh: ", "\uD83C\uDDEA\uD83C\uDDED").replace(" :flag_eg: ", "\uD83C\uDDEA\uD83C\uDDEC").replace(" :flag_ee: ", "\uD83C\uDDEA\uD83C\uDDEA").replace(" :flag_ec: ", "\uD83C\uDDEA\uD83C\uDDE8").replace(" :flag_ea: ", "\uD83C\uDDEA\uD83C\uDDE6").replace(" :flag_dz: ", "\uD83C\uDDE9\uD83C\uDDFF").replace(" :flag_do: ", "\uD83C\uDDE9\uD83C\uDDF4").replace(" :flag_dm: ", "\uD83C\uDDE9\uD83C\uDDF2").replace(" :flag_dk: ", "\uD83C\uDDE9\uD83C\uDDF0")
                .replace(" :flag_dj: ", "\uD83C\uDDE9\uD83C\uDDEF").replace(" :flag_dg: ", "\uD83C\uDDE9\uD83C\uDDEC").replace(" :flag_de: ", "\uD83C\uDDE9\uD83C\uDDEA").replace(" :flag_cz: ", "\uD83C\uDDE8\uD83C\uDDFF").replace(" :flag_cy: ", "\uD83C\uDDE8\uD83C\uDDFE").replace(" :flag_cx: ", "\uD83C\uDDE8\uD83C\uDDFD").replace(" :flag_cw: ", "\uD83C\uDDE8\uD83C\uDDFC").replace(" :flag_cv: ", "\uD83C\uDDE8\uD83C\uDDFB").replace(" :flag_cu: ", "\uD83C\uDDE8\uD83C\uDDFA").replace(" :flag_cr: ", "\uD83C\uDDE8\uD83C\uDDF7").replace(" :flag_cp: ", "\uD83C\uDDE8\uD83C\uDDF5").replace(" :flag_bb: ", "\uD83C\uDDE7\uD83C\uDDE7").replace(" :flag_bd: ", "\uD83C\uDDE7\uD83C\uDDE9").replace(" :flag_be: ", "\uD83C\uDDE7\uD83C\uDDEA").replace(" :flag_bf: ", "\uD83C\uDDE7\uD83C\uDDEB").replace(" :flag_bg: ", "\uD83C\uDDE7\uD83C\uDDEC").replace(" :flag_bh: ", "\uD83C\uDDE7\uD83C\uDDED").replace(" :flag_bi: ", "\uD83C\uDDE7\uD83C\uDDEE").replace(" :flag_bj: ", "\uD83C\uDDE7\uD83C\uDDEF").replace(" :flag_bl: ", "\uD83C\uDDE7\uD83C\uDDF1").replace(" :flag_bm: ", "\uD83C\uDDE7\uD83C\uDDF2")
                .replace(" :flag_bn: ", "\uD83C\uDDE7\uD83C\uDDF3").replace(" :flag_bo: ", "\uD83C\uDDE7\uD83C\uDDF4").replace(" :flag_bq: ", "\uD83C\uDDE7\uD83C\uDDF6").replace(" :flag_br: ", "\uD83C\uDDE7\uD83C\uDDF7").replace(" :flag_bs: ", "\uD83C\uDDE7\uD83C\uDDF8").replace(" :flag_bt: ", "\uD83C\uDDE7\uD83C\uDDF9").replace(" :flag_bv: ", "\uD83C\uDDE7\uD83C\uDDFB").replace(" :flag_bw: ", "\uD83C\uDDE7\uD83C\uDDFC").replace(" :flag_by: ", "\uD83C\uDDE7\uD83C\uDDFE").replace(" :flag_bz: ", "\uD83C\uDDE7\uD83C\uDDFF").replace(" :flag_ca: ", "\uD83C\uDDE8\uD83C\uDDE6").replace(" :flag_cc: ", "\uD83C\uDDE8\uD83C\uDDE8").replace(" :flag_cd: ", "\uD83C\uDDE8\uD83C\uDDE9").replace(" :flag_cf: ", "\uD83C\uDDE8\uD83C\uDDEB").replace(" :flag_cg: ", "\uD83C\uDDE8\uD83C\uDDEC").replace(" :flag_ch: ", "\uD83C\uDDE8\uD83C\uDDED").replace(" :flag_ci: ", "\uD83C\uDDE8\uD83C\uDDEE").replace(" :flag_ck: ", "\uD83C\uDDE8\uD83C\uDDF0").replace(" :flag_cl: ", "\uD83C\uDDE8\uD83C\uDDF1").replace(" :flag_cm: ", "\uD83C\uDDE8\uD83C\uDDF2")
                .replace(" :flag_cn: ", "\uD83C\uDDE8\uD83C\uDDF3").replace(" :flag_co: ", "\uD83C\uDDE8\uD83C\uDDF4").replace(" :england: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F").replace(" :scotland: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F").replace(" :wales: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F").replace(" :rainbow_flag: ", "\uD83C\uDFF3️\u200D\uD83C\uDF08").replace(" :flag_ac: ", "\uD83C\uDDE6\uD83C\uDDE8").replace(" :flag_ad: ", "\uD83C\uDDE6\uD83C\uDDE9").replace(" :flag_ae: ", "\uD83C\uDDE6\uD83C\uDDEA").replace(" :flag_af: ", "\uD83C\uDDE6\uD83C\uDDEB").replace(" :flag_ag: ", "\uD83C\uDDE6\uD83C\uDDEC").replace(" :flag_ai: ", "\uD83C\uDDE6\uD83C\uDDEE").replace(" :flag_al: ", "\uD83C\uDDE6\uD83C\uDDF1").replace(" :flag_am: ", "\uD83C\uDDE6\uD83C\uDDF2").replace(" :flag_ao: ", "\uD83C\uDDE6\uD83C\uDDF4").replace(" :flag_aq: ", "\uD83C\uDDE6\uD83C\uDDF6").replace(" :flag_ar:", "\uD83C\uDDE6\uD83C\uDDF7").replace(" :flag_as: ", "\uD83C\uDDE6\uD83C\uDDF8").replace(" :flag_at: ", "\uD83C\uDDE6\uD83C\uDDF9").replace(" :flag_au: ", "\uD83C\uDDE6\uD83C\uDDFA")
                .replace(" :flag_aw: ", "\uD83C\uDDE6\uD83C\uDDFC").replace(" :flag_ax: ", "\uD83C\uDDE6\uD83C\uDDFD").replace(" :flag_az: ", "\uD83C\uDDE6\uD83C\uDDFF").replace(" :flag_ba: ", "\uD83C\uDDE7\uD83C\uDDE6")
                .replace(" &female;  ", " ♀️ ").replace(" :woman_with_headscarf_tone1: ", "\uD83E\uDDD5\uD83C\uDFFB").replace(" :woman_tone1: ", "\uD83D\uDC69\uD83C\uDFFB").replace(" :vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB").replace(" :selfie_tone1: ", "\uD83E\uDD33\uD83C\uDFFB").replace(" :santa_tone1: ", "\uD83C\uDF85\uD83C\uDFFB").replace(" :princess_tone1: ", "\uD83D\uDC78\uD83C\uDFFB").replace(" :prince_tone1: ", "\uD83E\uDD34\uD83C\uDFFB").replace(" :pregnant_woman_tone1: ", "\uD83E\uDD30\uD83C\uDFFB").replace(" :pray_tone1: ", "\uD83D\uDE4F\uD83C\uDFFB").replace(" :fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB").replace(" :girl_tone1: ", "\uD83D\uDC67\uD83C\uDFFB").replace(" :guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB").replace(" :mage_tone1: ", "\uD83E\uDDD9\uD83C\uDFFB").replace(" :man_dancing_tone1: ", "\uD83D\uDD7A\uD83C\uDFFB").replace(" :man_in_business_suit_levitating_tone1: ", "\uD83D\uDD74\uD83C\uDFFB").replace(" :man_in_tuxedo_tone1: ", "\uD83E\uDD35\uD83C\uDFFB").replace(" :man_tone1: ", "\uD83D\uDC68\uD83C\uDFFB").replace(" :man_with_chinese_cap_tone1: ", "\uD83D\uDC72\uD83C\uDFFB").replace(" :merperson_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB").replace(" :mrs_claus_tone1: ", "\uD83E\uDD36\uD83C\uDFFB").replace(" :muscle_tone1: ", "\uD83D\uDCAA\uD83C\uDFFB").replace(" :nail_care_tone1: ", "\uD83D\uDC85\uD83C\uDFFB").replace(" :nose_tone1: ", "\uD83D\uDC43\uD83C\uDFFB")
                .replace(" :older_adult_tone1: ", "\uD83E\uDDD3\uD83C\uDFFB").replace(" :older_man_tone1: ", "\uD83D\uDC74\uD83C\uDFFB").replace(" :older_woman_tone1: ", "\uD83D\uDC75\uD83C\uDFFB").replace(" :person_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB").replace(" :person_facepalming_tone1: ", "\uD83D\uDC69\uD83C\uDFFB").replace(" :person_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB").replace(" :person_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB").replace(" :person_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB").replace(" :person_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB").replace(" :person_getting_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB").replace(" :person_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB").replace(" :person_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB").replace(" :person_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB").replace(" :person_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB").replace(" :person_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB").replace(" :person_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB").replace(" :person_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB").replace(" :police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB").replace(" :elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB").replace(" :ear_tone1: ", "\uD83D\uDC42\uD83C\uDFFB")
                .replace(" :detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB").replace(" :dancer_tone1: ", "\uD83D\uDC83\uD83C\uDFFB").replace(" :construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB").replace(" :child_tone1: ", "\uD83E\uDDD2\uD83C\uDFFB").replace(" :bride_with_veil_tone1: ", "\uD83D\uDC70\uD83C\uDFFB").replace(" :boy_tone1: ", "\uD83D\uDC66\uD83C\uDFFB").replace(" :blond_haired_person_tone1: ", "\uD83D\uDC71\uD83C\uDFFB").replace(" :bearded_person_tone1: ", "\uD83E\uDDD4\uD83C\uDFFB").replace(" :baby_tone1: ", "\uD83D\uDC76\uD83C\uDFFB").replace(" :angel_tone1: ", "\uD83D\uDC7C\uD83C\uDFFB").replace(" :adult_tone1: ", "\uD83E\uDDD1\uD83C\uDFFB").replace(" :woman_technologist: ", "\uD83D\uDC69\u200D\uD83D\uDCBB").replace(" :woman_teacher: ", "\uD83D\uDC69\u200D\uD83C\uDFEB").replace(" :woman_student: ", "\uD83D\uDC69\u200D\uD83C\uDF93").replace(" :woman_singer: ", "\uD83D\uDC69\u200D\uD83C\uDFA4").replace(" :woman_scientist: ", "\uD83D\uDC69\u200D\uD83D\uDD2C").replace(" :woman_office_worker: ", "\uD83D\uDC69\u200D\uD83D\uDCBC").replace(" :woman_mechanic: ", "\uD83D\uDC69\u200D\uD83D\uDD27").replace(" :woman_firefighter: ", "\uD83D\uDC69\u200D\uD83D\uDE92")
                .replace(" :woman_farmer: ", "\uD83D\uDC69\u200D\uD83C\uDF3E").replace(" :woman_factory_worker: ", "\uD83D\uDC69\u200D\uD83C\uDFED").replace(" :woman_cook: ", "\uD83D\uDC69\u200D\uD83C\uDF73").replace(" :woman_astronaut: ", "\uD83D\uDC69\u200D\uD83D\uDE80").replace(" :woman_artist: ", "\uD83D\uDC69\u200D\uD83C\uDFA8")
                .replace(" :man_technologist: ", "\uD83D\uDC68\u200D\uD83D\uDCBB").replace(" :man_teacher: ", "\uD83D\uDC68\u200D\uD83C\uDFEB").replace(" :man_student: ", "\uD83D\uDC68\u200D\uD83C\uDF93").replace(" :man_singer: ", "\uD83D\uDC68\u200D\uD83C\uDFA4").replace(" :man_scientist: ", "\uD83D\uDC68\u200D\uD83D\uDD2C").replace(" :man_office_worker: ", "\uD83D\uDC68\u200D\uD83D\uDCBC").replace(" :woman_genie: ", "\uD83E\uDDDE\u200D♀️").replace(" :woman_gesturing_no: ", "\uD83D\uDE45\u200D♀️").replace(" :woman_gesturing_ok: ", "\uD83D\uDE46\u200D♀️").replace(" :woman_getting_face_massage: ", "\uD83D\uDC86\u200D♀️").replace(" :woman_getting_haircut: ", "\uD83D\uDC87\u200D♀️").replace(" :woman_guard: ", "\uD83D\uDC82\u200D♀️").replace(" :woman_health_worker: ", "\uD83D\uDC69\u200D⚕️").replace(" :woman_judge: ", "\uD83D\uDC69\u200D⚖️").replace(" :woman_mage: ", "\uD83E\uDDD9\u200D♀️").replace(" :woman_pilot: ", "\uD83D\uDC69\u200D✈️").replace(" :woman_police_officer: ", "\uD83D\uDC6E\u200D♀️️").replace(" :woman_pouting: ", "\uD83D\uDE4E\u200D♀️")
                .replace(" :woman_raising_hand: ", "\uD83D\uDE4B\u200D♀️").replace(" :woman_running: ", "\uD83C\uDFC3\u200D♀️").replace(" :woman_shrugging: ", "\uD83E\uDD37\u200D♀️")
                .replace(" :woman_tipping_hand: ", "\uD83D\uDC81\u200D♀️").replace(" :woman_vampire: ", "\uD83E\uDDDB\u200D♀️").replace(" :woman_walking: ", "\uD83D\uDEB6\u200D♀️").replace(" :woman_wearing_turban: ", "\uD83D\uDC73\u200D♀️")
                .replace(" :women_with_bunny_ears_partying: ", "\uD83D\uDC6F\u200D♀️").replace(" :family_man_girl: ", "\uD83D\uDC68\u200D\uD83D\uDC67").replace(" :family_man_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC66").replace(" :family_woman_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC66").replace(" :family_woman_girl: ", "\uD83D\uDC69\u200D\uD83D\uDC67").replace(" :man_artist: ", "\uD83D\uDC68\u200D\uD83C\uDFA8").replace(" :man_astronaut: ", "\uD83D\uDC68\u200D\uD83D\uDE80")
                .replace(" :man_cook: ", "\uD83D\uDC68\u200D\uD83C\uDF73️").replace(" :man_factory_worker: ", "\uD83D\uDC68\u200D\uD83C\uDFED").replace(" :man_farmer: ", "\uD83D\uDC68\u200D\uD83C\uDF3E️").replace(" :man_firefighter: ", "\uD83D\uDC68\u200D\uD83D\uDE92️").replace(" :man_mechanic: ", "\uD83D\uDC68\u200D\uD83D\uDD27").replace(" :woman_frowning: ", "\uD83D\uDE4D\u200D♀️️").replace(" :woman_fairy: ", "\uD83E\uDDDA\u200D♀️️").replace(" :woman_facepalming: ", "\uD83E\uDD26\u200D♀️️").replace(" :woman_elf: ", "\uD83E\uDDDD\u200D♀️️").replace(" :woman_construction_worker: ", "\uD83D\uDC77\u200D♀️️")
                .replace(" :woman_bowing: ", "\uD83D\uDE47\u200D♀️️").replace(" :men_with_bunny_ears_partying: ", "\uD83D\uDC6F\u200D♂️").replace(" :man_wearing_turban: ", "\uD83D\uDC73\u200D♂️️").replace(" :men_with_bunny_ears_partying: ", "\uD83D\uDC68\u200D\uD83D\uDE92️").replace(" :man_walking: ", "\uD83D\uDEB6\u200D♂️").replace(" :man_vampire: ", "\uD83E\uDDDB\u200D♂️").replace(" :man_tipping_hand: ", "\uD83D\uDC81\u200D♂️️️").replace(" :man_shrugging: ", "\uD83E\uDD37\u200D♂️️️").replace(" :man_running: ", "\uD83C\uDFC3\u200D♂️️️").replace(" :man_raising_hand: ", "\uD83D\uDE4B\u200D♂️️️")
                .replace(" :man_pouting: ", "\uD83D\uDE4E\u200D♂️").replace(" :merman: ", "\uD83E\uDDDC\u200D♂️").replace(" :mermaid: ", "\uD83E\uDDDC\u200D♀️").replace(" :man_police_officer: ", "\uD83D\uDC6E\u200D♂️️").replace(" :man_pilot: ", "\uD83D\uDC68\u200D✈️").replace(" :man_mage: ", "\uD83E\uDDD9\u200D♂️️️").replace(" :man_judge: ", "\uD83D\uDC68\u200D⚖️").replace(" :man_health_worker: ", "\uD83D\uDC68\u200D⚕️️️").replace(" :man_guard: ", "\uD83D\uDC82\u200D♂️").replace(" :man_getting_haircut: ", "\uD83D\uDC87\u200D♂️️️")
                .replace(" :man_getting_face_massage: ", "\uD83D\uDC86\u200D♂️").replace(" :man_gesturing_ok: ", "\uD83D\uDE46\u200D♂️").replace(" :man_gesturing_no: ", "\uD83D\uDE45\u200D♂️️").replace(" :man_genie: ", "\uD83E\uDDDE\u200D♂️").replace(" :man_frowning: ", "\uD83D\uDE4D\u200D♂️").replace(" :man_fairy: ", "\uD83E\uDDDA\u200D♂️").replace(" :man_facepalming: ", "\uD83E\uDD26\u200D♂️").replace(" :woman_detective: ", "\uD83D\uDD75️\u200D♀️").replace(" :man_artist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8").replace(" :man_astronaut_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDE80")
                .replace(" :man_cook_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF73️").replace(" :man_factory_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFED").replace(" :man_farmer_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF3E️").replace(" :man_firefighter_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDE92").replace(" :man_mechanic_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDD27").replace(" :man_office_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBC️️").replace(" :man_scientist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDD2C️️").replace(" :man_singer_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA4️️").replace(" :man_student_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF93️️").replace(" :man_teacher_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFEB️️")
                .replace(" :man_technologist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB").replace(" :woman_artist_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFA8").replace(" :woman_astronaut_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDE80️").replace(" :woman_cook_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF73").replace(" :woman_factory_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFED").replace(" :woman_farmer_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF3E").replace(" :woman_firefighter_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDE92").replace(" :woman_mechanic_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDD27️️").replace(" :woman_office_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBC️️").replace(" :woman_scientist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFEB️️")
                .replace(" :woman_singer_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFA4").replace(" :woman_student_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93").replace(" :woman_teacher_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFEB️").replace(" :woman_technologist_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBB").replace(" :blond-haired_man: ", "\uD83D\uDC71\u200D♂️").replace(" :blond-haired_woman: ", "\uD83D\uDC71\u200D♀️").replace(" :man_bowing: ", "\uD83D\uDE47\u200D♂️").replace(" :man_construction_worker: ", "\uD83D\uDC77\u200D♂️").replace(" :man_elf: ", "\uD83E\uDDDD\u200D♂️")
                .replace(" :man_detective: ", "\uD83D\uDD75️\u200D♂️").replace(" :woman_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB\u200D♀️").replace(" :woman_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB\u200D♀️").replace(" :woman_vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB\u200D♀️").replace(" :woman_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB\u200D♀️").replace(" :woman_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB\u200D♀️").replace(" :woman_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB\u200D♀️").replace(" :woman_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️").replace(" :woman_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB\u200D♀️").replace(" :woman_police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB\u200D♀️").replace(" :woman_pilot_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D✈️").replace(" :woman_judge_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D⚖️").replace(" :woman_health_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D⚕️").replace(" :woman_guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB\u200D♀️").replace(" :woman_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_getting_face_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB\u200D♀️").replace(" :woman_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB\u200D♀️").replace(" :woman_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB\u200D♀️").replace(" :woman_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB\u200D♀️").replace(" :woman_fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB\u200D♀️").replace(" :woman_facepalming_tone1: ", "\uD83E\uDD26\uD83C\uDFFB\u200D♀️").replace(" :woman_elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB\u200D♀️").replace(" :woman_detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB\u200D♀️").replace(" :woman_construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB\u200D♀️").replace(" :woman_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB\u200D♀️").replace(" :merman_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB\u200D♂️").replace(" :mermaid_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB\u200D♀️").replace(" :man_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB\u200D♂️").replace(" :man_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB\u200D♂️").replace(" :man_vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB\u200D♂️").replace(" :man_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB\u200D♂️")
                .replace(" &phone; ", " ☎️ ").replace(" &rsquo; ", "'").replace(" &rdquo; ", "'");
        return convertMessage.trim();
    }

    public String replaceSpecialChar(String s) {
        try {
            if (s != null && s.length() > 0) {
                s = replaceString1(" " + s);

                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String replaceString1(String message) {
        String convertMessage = message;
        convertMessage = convertMessage.replace(" ;)", "\uD83D\uDE09").replace("&lsquo;", "‘").replace(":\u000f", "☺️").replace("\u0017", "\uD83D\uDE17")
                .replace("&bull;", "•").replace("&pi;", "π").replace("&radic;", "√").replace("&div;", "÷").replace("&times;", "×").replace("&para;", "¶").replace("∆&pound;", "∆")
                .replace("&ldquo;", "").replace("&nbsp;", " ")
                .replace("&cent;", "£").replace("&euro;", "¢").replace("&yen;", "¥").replace("&deg;", "°")
                .replace("&copy;", "©").replace("&circledR;", "®").replace("&trade;", "™").replace("&check;", "✓")
                .replace("&gt;", ">").replace("&lt;", "<").replace("&apos;", "'").replace("&quot;", "\"")
                .replace("&amp;", "&").replace("&apos;", "'").replace("&quot;", "\"").replace(" &amp;", "&")
                .replace(" &#10; ", "\n").replace(" :)", "\uD83D\uDE42").replace("&#10;", "\n")
                .replace(" :D", "\uD83D\uDE03").replace(" ;D", "\uD83D\uDE09")
                .replace("&rsquo;", "'").replace("&rdquo;", "'");
                /*.replace(" \u0003 ", " \uD83D\uDE03").replace(" \u0004 ", "\uD83D\uDE04").replace(" \u0001 ", "\uD83D\uDE01")
                .replace(" \u0006 ", "\uD83D\uDE06").replace(" \u0000 ", "\uD83D\uDE00").replace(" 9\u000f ", "☹️")
                .replace(" \u0015 ", "\uD83D\uDE15")
                .replace(" \u0019 ", " \uD83D\uDE19").replace(" \u0016 ", " \uD83D\uDE16").replace("\u0011 ", "\uD83E\uDD11")
                .replace(" :smile: ", "\uD83D\uDE04").replace(" :-P ", "\uD83D\uDE1B").replace(" =P ", "\uD83D\uDE1B")
                .replace(" :blush: ", "\uD83D\uDE0A").replace(" :smiley: ", "\uD83D\uDE03")
                .replace(" :) ", "\uD83D\uDE42").replace(" :-) ", "\uD83D\uDE42").replace(" :smirk: ", "\uD83D\uDE0F").replace(" :slight_smile: ", "\uD83D\uDE42").replace(" =] ", "\uD83D\uDE42")
                .replace(" =) ", "\uD83D\uDE42").replace(" :] ", "\uD83D\uDE42")
                .replace(" ':) ", "\uD83D\uDE05").replace(" :sweat_smile: ", "\uD83D\uDE05").replace(" ':-) ", "\uD83D\uDE05").replace(" '=) ", "\uD83D\uDE05").replace(" ':D ", "\uD83D\uDE05")
                .replace(" ':-D ", "\uD83D\uDE05").replace(" '=D ", "\uD83D\uDE05")
                .replace(" :/ ", "\uD83D\uDE15").replace(" :disappointed: ", "\uD83D\uDE1E").replace(" :( ", "\uD83D\uDE1E")
                .replace(" :heart_eyes: ", "\uD83D\uDE0D").replace(" :kissing_heart: ", "\uD83D\uDE18")
                .replace(" :kissing_closed_eyes: ", "\uD83D\uDE1A").replace(" :flushed: ", "\uD83D\uDE33")
                .replace(" :laughing: ", "\uD83D\uDE06").replace(" :D ", "\uD83D\uDE03").replace(" :-D ", "\uD83D\uDE03").replace(" =D ", "\uD83D\uDE03").replace(" >:) ", "\uD83D\uDE06").replace(" >;) ", "\uD83D\uDE06")
                .replace(" >:-) ", "\uD83D\uDE06").replace(" >=) ", "\uD83D\uDE06").replace(" :wink: ", "\uD83D\uDE09").replace(" ;-) ", "\uD83D\uDE09").replace(" *-) ", "\uD83D\uDE09").replace(" *) ", "\uD83D\uDE09")
                .replace(" ;-] ", "\uD83D\uDE09").replace(" ;] ", "\uD83D\uDE09").replace(" ;D ", "\uD83D\uDE09").replace(" ;^ ) ", "\uD83D\uDE09")
                .replace(" :sweat: ", "\uD83D\uDE13").replace(" ':( ", "\uD83D\uDE13").replace("' :-( ", "\uD83D\uDE13").replace(" '=( ", "\uD83D\uDE13")
                .replace(" :kissing_heart: ", "\uD83D\uDE18").replace(" :* ", "\uD83D\uDE18").replace(" :-* ", "\uD83D\uDE18").replace(" =* ", "\uD83D\uDE18").replace(" :^* ", "\uD83D\uDE18")
                .replace(" :heart: ", "❤️").replace(" <3 ", "❤").replace(" :') ", "\uD83D\uDE02")
                .replace(" :joy: ", "\uD83D\uDE02").replace(" :'-) ", "\uD83D\uDE02")
                .replace(" :stuck_out_tongue_winking_eye: ", "\uD83D\uDE1C").replace(" >:P ", "\uD83D\uDE1C").replace(" X-P ", "\uD83D\uDE1C").replace(" x-p ", "\uD83D\uDE1C")
                .replace(" :disappointed: ", "\uD83D\uDE1E").replace(" >:[ ", "\uD83D\uDE1E").replace(" :-( ", "\uD83D\uDE1E").replace(" :( ", "\uD83D\uDE1E").replace(" :-[ ", "\uD83D\uDE1E").replace(" :[ ", "\uD83D\uDE1E").replace(" =( ", "\uD83D\uDE1E")
                .replace(" :angry: ", "\uD83D\uDE20").replace(" >:( ", "\uD83D\uDE20").replace(" >:-( ", "\uD83D\uDE20").replace(" :@ ", "\uD83D\uDE20")
                .replace(" :cry: ", "\uD83D\uDE22").replace(" :'( ", "\uD83D\uDE22").replace(" :'-( ", "\uD83D\uDE22").replace(" ;( ", "\uD83D\uDE22").replace(" ;-( ", "\uD83D\uDE22")
                .replace(" :persevere: ", "\uD83D\uDE23").replace(" >.< ", "\uD83D\uDE23")
                .replace(" :fearful: ", "\uD83D\uDE28").replace(" :flushed: ", "\uD83D\uDE33").replace(" :$ ", "\uD83D\uDE33").replace(" =$ ", "\uD83D\uDE33")
                .replace(" :dizzy_face: ", "\uD83D\uDE35").replace(" #-) ", "\uD83D\uDE35").replace("  #) ", "\uD83D\uDE35").replace(" %-) ", "\uD83D\uDE35").replace(" %) ", "\uD83D\uDE35").replace(" X) ", "\uD83D\uDE35").replace(" X-) ", "\uD83D\uDE35")
                .replace(" :ok_woman: ", "\uD83D\uDE46").replace(" *\\0 ", "\uD83D\uDE46").replace(" \\0/ ", "\uD83D\uDE46").replace(" *\\O ", "\uD83D\uDE46").replace(" \\O/ ", "\uD83D\uDE46")
                .replace(" :innocent: ", "\uD83D\uDE07").replace(" O:-) ", "\uD83D\uDE07").replace(" 0:-3 ", "\uD83D\uDE07").replace(" 0:3 ", "\uD83D\uDE07").replace(" 0:-) ", "\uD83D\uDE07").replace(" 0:) ", "\uD83D\uDE07").replace(" 0;^) ", "\uD83D\uDE07").replace(" O:-) ", "\uD83D\uDE07").replace(" O:) ", "\uD83D\uDE07").replace(" O;-) ", "\uD83D\uDE07").replace(" O=) ", "\uD83D\uDE07").replace(" 0;-) ", "\uD83D\uDE07").replace(" O:-3 ", "\uD83D\uDE07").replace(" O:3 ", "\uD83D\uDE07")
                .replace(" :sunglasses: ", "\uD83D\uDE0E").replace(" B-) ", "\uD83D\uDE0E").replace(" B) ", "\uD83D\uDE0E").replace(" 8) ", "\uD83D\uDE0E").replace(" 8-) ", "\uD83D\uDE0E").replace(" B-D ", "\uD83D\uDE0E").replace(" 8-D ", "\uD83D\uDE0E")
                .replace(" :expressionless: ", "\uD83D\uDE11").replace(" -_- ", "\uD83D\uDE11").replace(" -__- ", "\uD83D\uDE11").replace(" -___- ", "\uD83D\uDE11")
                .replace(" :confused: ", "\uD83D\uDE15").replace(" >:\\ ", "\uD83D\uDE15").replace(" >:/ ", "\uD83D\uDE15").replace(" :-/ ", "\uD83D\uDE15").replace(" :-. ", "\uD83D\uDE15").replace(" :/ ", "\uD83D\uDE15").replace(" :\\ ", "\uD83D\uDE15").replace(" =/ ", "\uD83D\uDE15").replace(" =\\ ", "\uD83D\uDE15").replace(" :L ", "\uD83D\uDE15").replace(" =L ", "\uD83D\uDE15")
                .replace(" :stuck_out_tongue: ", "\uD83D\uDE1B").replace(" :P ", "\uD83D\uDE1B").replace(" :-P ", "\uD83D\uDE1B").replace(" =P ", "\uD83D\uDE1B").replace(" :-p ", "\uD83D\uDE1B").replace(" :p ", "\uD83D\uDE1B").replace(" =p ", "\uD83D\uDE1B").replace(" :-Þ ", "\uD83D\uDE1B").replace(" :Þ ", "\uD83D\uDE1B").replace(" :þ ", "\uD83D\uDE1B").replace(" :-þ ", "\uD83D\uDE1B").replace(" :-b ", "\uD83D\uDE1B").replace(" :b ", "\uD83D\uDE1B").replace(" D: ", "\uD83D\uDE1B")
                .replace(" :open_mouth: ", "\uD83D\uDE2E").replace(" :-O ", "\uD83D\uDE2E").replace(" :O ", "\uD83D\uDE2E").replace(" :-o ", "\uD83D\uDE2E").replace(" :o ", "\uD83D\uDE2E").replace(" O_O ", "\uD83D\uDE2E").replace(" >:O ", "\uD83D\uDE2E")
                .replace(" :no_mouth: ", "\uD83D\uDE36").replace(" :-X ", "\uD83D\uDE36").replace(" :X ", "\uD83D\uDE36").replace(" :-# ", "\uD83D\uDE36").replace(" :# ", "\uD83D\uDE36").replace(" =X ", "\uD83D\uDE36").replace(" =x ", "\uD83D\uDE36").replace(" :x ", "\uD83D\uDE36").replace(" :-x ", "\uD83D\uDE36").replace(" =# ", "\uD83D\uDE36")
                .replace(" </3 ", "\uD83D\uDC94").replace(" :broken_heart: ", "\uD83D\uDC94")
                .replace(" \t ", "\uD83D\uDE09").replace(" \u001f ", "\uD83D\uDE1F")
                .replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :kiss_mm: ", "\uD83D\uDC68\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC68")
                .replace(" :kiss_woman_man: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC68")
                .replace(" :woman_zombie: ", "\uD83E\uDDDF\u200D♀️").replace(" :man_zombie: ", "\uD83E\uDDDF\u200D♂️")
                .replace(" :kiss_ww: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC8B\u200D\uD83D\uDC69")
                .replace(" :family_mmbb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_mmgb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_mmgg: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_mwbb: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_mwgb: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_mwgg: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_wwbb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_wwgb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_wwgg: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :couple_mm: ", "\uD83D\uDC68\u200D❤️\u200D\uD83D\uDC68").replace(" :couple_with_heart_woman_man: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68")
                .replace(" :couple_ww: ", "\uD83D\uDC69\u200D❤️\u200D\uD83D\uDC69").replace(" :family_man_boy_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_man_girl_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66")
                .replace(" :family_man_girl_girl: ", "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67").replace(" :family_man_woman_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66")
                .replace(" :family_mmb: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66").replace(" :family_mmg: ", "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67")
                .replace(" :family_mwg: ", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67").replace(" :family_woman_boy_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66")
                .replace(" :family_woman_girl_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66").replace(" :family_woman_girl_girl: ", "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67")
                .replace(" :family_wwb: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66").replace(" :family_wwg: ", "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67")
                .replace(" :blond-haired_man_tone1: ", "\uD83D\uDC71\uD83C\uDFFB\u200D♂️").replace(" :blond-haired_woman_tone1: ", "\uD83D\uDC71\uD83C\uDFFB\u200D♀️")
                .replace(" :man_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB\u200D♂️").replace(" :man_construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB\u200D♂️")
                .replace(" :man_detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB\u200D♂️").replace(" :man_elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB\u200D♂️")
                .replace(" :man_facepalming_tone1: ", "\uD83E\uDD26\uD83C\uDFFB\u200D♂️").replace(" :man_fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB\u200D♂️")
                .replace(" :man_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB\u200D♂️").replace(" :man_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB\u200D♂️")
                .replace(" :man_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB\u200D♂️").replace(" :man_getting_face_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB\u200D♂️")
                .replace(" :man_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB\u200D♂️").replace(" :man_guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB\u200D♂️")
                .replace(" :man_health_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D⚕️").replace(" :man_judge_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D⚖️")
                .replace(" :man_mage_tone1: ", "\uD83E\uDDD9\uD83C\uDFFB\u200D♂️").replace(" :man_pilot_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D✈️")
                .replace(" :man_police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB\u200D♂️").replace(" :man_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB\u200D♂️")
                .replace(" :man_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB\u200D♂️").replace(" :man_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB\u200D♂️")
                .replace(" :man_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB\u200D♂️")
                .replace(" :person_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB").replace(" :bath_tone1: ", "\uD83D\uDEC0\uD83C\uDFFB")
                .replace(" :person_in_bed_tone1: ", "\uD83D\uDECC\uD83C\uDFFB").replace(" :snowboarder_tone1: ", "\uD83C\uDFC2\uD83C\uDFFB")
                .replace(" :person_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB").replace(" :man_surfing: ", "\uD83C\uDFC4\u200D♂️")
                .replace(" :man_swimming: ", "\uD83C\uDFCA\u200D♂️").replace(" :men_wrestling: ", "\uD83E\uDD3C\u200D♂️").replace(" :woman_biking: ", "\uD83D\uDEB4\u200D♀️")
                .replace(" :woman_cartwheeling: ", "\uD83E\uDD38\u200D♀️").replace(" :woman_climbing: ", "\uD83E\uDDD7\u200D♀️")
                .replace(" :woman_in_lotus_position: ", "\uD83E\uDDD8\u200D♀️").replace(" :woman_in_steamy_room: ", "\uD83E\uDDD6\u200D♀️")
                .replace(" :woman_juggling: ", "\uD83E\uDD39\u200D♀️").replace(" :woman_mountain_biking: ", "\uD83D\uDEB5\u200D♀️")
                .replace(" :woman_playing_handball: ", "\uD83E\uDD3E\u200D♀️").replace(" :woman_playing_water_polo: ", "\uD83E\uDD3D\u200D♀️")
                .replace(" :woman_rowing_boat: ", "\uD83D\uDEA3\u200D♀️").replace(" :woman_surfing: ", "\uD83C\uDFC4\u200D♀️").replace(" :woman_swimming: ", "\uD83C\uDFCA\u200D♀️")
                .replace(" :women_wrestling: ", "\uD83E\uDD3C\u200D♀️").replace(" :breast_feeding_tone1: ", "\uD83E\uDD31\uD83C\uDFFB").replace(" :horse_racing_tone1: ", "\uD83C\uDFC7\uD83C\uDFFB")
                .replace(" :person_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB").replace(" :person_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB").replace(" :person_doing_cartwheel_tone1: ", "\uD83E\uDD38\uD83C\uDFFB")
                .replace(" :person_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB").replace(" :person_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB")
                .replace(" :person_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB").replace(" :person_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB")
                .replace(" :person_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB").replace(" :person_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB")
                .replace(" :person_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB").replace(" :person_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB")
                .replace(" :person_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB").replace(" :person_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB")
                .replace(" :person_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB").replace(" :man_rowing_boat: ", "\uD83D\uDEA3\u200D♂️").replace(" :man_playing_water_polo: ", "\uD83E\uDD3D\u200D♂️").replace(" :man_playing_handball: ", "\uD83E\uDD3E\u200D♂️")
                .replace(" :man_mountain_biking: ", "\uD83D\uDEB5\u200D♂️").replace(" :man_juggling: ", "\uD83E\uDD39\u200D♂️").replace(" :man_in_steamy_room: ", "\uD83E\uDDD6\u200D♂️")
                .replace(" :man_in_lotus_position: ", "\uD83E\uDDD8\u200D♂️").replace(" :man_climbing: ", "\uD83E\uDDD7\u200D♂️").replace(" :man_cartwheeling: ", "\uD83E\uDD38\u200D♂️")
                .replace(" :man_biking: ", "\uD83D\uDEB4\u200D♂️").replace(" :woman_bouncing_ball: ", "⛹️\u200D♀️").replace(" :man_bouncing_ball: ", "⛹️\u200D♂️")
                .replace(" :woman_lifting_weights: ", "\uD83C\uDFCB️\u200D♀️").replace(" :woman_golfing: ", "\uD83C\uDFCC️\u200D♀️").replace(" :woman_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB\u200D♀️")
                .replace(" :man_lifting_weights: ", "\uD83C\uDFCB️\u200D♂️").replace(" :man_golfing: ", "\uD83C\uDFCC️\u200D♂️").replace(" :man_bouncing_ball_tone1: ", "⛹\uD83C\uDFFB\u200D♂️")
                .replace(" :woman_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB\u200D♀️").replace(" :woman_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB\u200D♀️").replace(" :woman_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB\u200D♀️").replace(" :woman_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB\u200D♀️").replace(" :woman_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB\u200D♀️").replace(" :woman_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB\u200D♀️").replace(" :woman_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB\u200D♀️").replace(" :woman_cartwheeling_tone1: ", "\uD83E\uDD38\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB\u200D♀️").replace(" :man_biking_tone1: ", "\uD83D\uDEB4\uD83C\uDFFB\u200D♂️").replace(" :man_cartwheeling_tone1: ", "\uD83E\uDD38\uD83C\uDFFB\u200D♂️")
                .replace(" :man_climbing_tone1: ", "\uD83E\uDDD7\uD83C\uDFFB\u200D♂️").replace(" :man_golfing_tone1: ", "\uD83C\uDFCC\uD83C\uDFFB\u200D♂️").replace(" :man_in_lotus_position_tone1: ", "\uD83E\uDDD8\uD83C\uDFFB\u200D♂️")
                .replace(" :man_in_steamy_room_tone1: ", "\uD83E\uDDD6\uD83C\uDFFB\u200D♂️").replace(" :man_juggling_tone1: ", "\uD83E\uDD39\uD83C\uDFFB\u200D♂️").replace(" :man_lifting_weights_tone1: ", "\uD83C\uDFCB\uD83C\uDFFB\u200D♂️")
                .replace(" :man_mountain_biking_tone1: ", "\uD83D\uDEB5\uD83C\uDFFB\u200D♂️").replace(" :man_playing_handball_tone1: ", "\uD83E\uDD3E\uD83C\uDFFB\u200D♂️").replace(" :man_playing_water_polo_tone1: ", "\uD83E\uDD3D\uD83C\uDFFB\u200D♂️")
                .replace(" :man_rowing_boat_tone1: ", "\uD83D\uDEA3\uD83C\uDFFB\u200D♂️").replace(" :man_surfing_tone1: ", "\uD83C\uDFC4\uD83C\uDFFB\u200D♂️").replace(" :man_swimming_tone1: ", "\uD83C\uDFCA\uD83C\uDFFB\u200D♂️")
                .replace(" &circledR; ", " ®️ ").replace(" &spades; ", " ♠️ ").replace(" &trade; ", " ™️ ").replace(" &EmptySmallSquare; ", " ◻️ ")
                .replace(" &EmptyVerySmallSquare; ", "️ ▫ ").replace(" &hookleftarrow; ", " ↩️️ ").replace(" &harr; ", " ↔ ").replace(" &hearts; ", " ♥️ ")
                .replace(" &diamondsuit; ️", " ♦️ ").replace(" &copy; ", " ©️ ").replace(" &clubs; ", " ♣️ ").replace(" :two: ", "2️⃣")
                .replace(" :zero: ", "0️⃣").replace(" &cudarrr; ", " ⤵️ ").replace(" &LowerLeftArrow; ", " ↙️️ ").replace(" &LowerRightArrow;️ ", " ↘ ")
                .replace(" &hookrightarrow; ", " ↪️ ").replace(" &updownarrow; ", " ↕️ ️").replace(" &nearr; ", " ↗ ️").replace(" &nwarr; ", " ↖ ").replace(" &FilledSmallSquare; ", " ◼️️ ").replace(" &blacksquare; ", " ▪️ ").replace(" &blacksquare; ", " ▪ ")
                .replace(" :three: ", "3️⃣").replace(" :six: ", "6️⃣").replace(" :seven: ", "7️⃣").replace(" :one: ", "1️⃣").replace(" :nine: ", "9️⃣").replace(" :hash: ", "#️⃣").replace(" :four: ", "4️⃣").replace(" :five: ", "5️⃣").replace(" :eight: ", "8️⃣").replace(" :asterisk: ", "*️⃣")
                .replace(" :eye_in_speech_bubble: ", "\uD83D\uDC41️\u200D\uD83D\uDDE8️").replace(" :united_nations: ", "\uD83C\uDDFA\uD83C\uDDF3")
                .replace(" :flag_zw: ", "\uD83C\uDDFF\uD83C\uDDFC").replace(" :flag_za: ", "\uD83C\uDDFF\uD83C\uDDF2").replace(" :flag_yt: ", "\uD83C\uDDFF\uD83C\uDDE6")
                .replace(" :flag_ye: ", "\uD83C\uDDFE\uD83C\uDDF9").replace(" :flag_xk: ", "\uD83C\uDDFE\uD83C\uDDEA").replace(" :flag_ws: ", "\uD83C\uDDFD\uD83C\uDDF0")
                .replace(" :flag_wf: ", "\uD83C\uDDFC\uD83C\uDDEB").replace(" :flag_vu: ", "\uD83C\uDDFB\uD83C\uDDFA").replace(" :flag_vn: ", "\uD83C\uDDFB\uD83C\uDDF3")
                .replace(" :flag_vi: ", "\uD83C\uDDFB\uD83C\uDDEE").replace(" :flag_vg: ", "\uD83C\uDDFB\uD83C\uDDEC").replace(" :flag_ve: ", "\uD83C\uDDFB\uD83C\uDDEA")
                .replace(" :flag_vc: ", "\uD83C\uDDFB\uD83C\uDDE8").replace(" :flag_va: ", "\uD83C\uDDFB\uD83C\uDDE6").replace(" :flag_sn: ", "\uD83C\uDDF8\uD83C\uDDF3")
                .replace(" :flag_so: ", "\uD83C\uDDF8\uD83C\uDDF4").replace(" :flag_sr: ", "\uD83C\uDDF8\uD83C\uDDF7").replace(" :flag_ss: ", "\uD83C\uDDF8\uD83C\uDDF8")
                .replace(" :flag_st: ", "\uD83C\uDDF8\uD83C\uDDF9").replace(" :flag_sv: ", "\uD83C\uDDF8\uD83C\uDDFB").replace(" :flag_sx: ", "\uD83C\uDDF8\uD83C\uDDFD")
                .replace(" :flag_sy: ", "\uD83C\uDDF8\uD83C\uDDFE").replace(" :flag_sz: ", "\uD83C\uDDF8\uD83C\uDDFF").replace(" :flag_ta: ", "\uD83C\uDDF9\uD83C\uDDE6")
                .replace(" :flag_tc: ", "\uD83C\uDDF9\uD83C\uDDE8").replace(" :flag_td: ", "\uD83C\uDDF9\uD83C\uDDE9").replace(" :flag_tf: ", "\uD83C\uDDF9\uD83C\uDDEB")
                .replace(" :flag_tg: ", "\uD83C\uDDF9\uD83C\uDDEC").replace(" :flag_th: ", "\uD83C\uDDF9\uD83C\uDDED").replace(" :flag_tj: ", "\uD83C\uDDF9\uD83C\uDDEF")
                .replace(" :flag_tk: ", "\uD83C\uDDF9\uD83C\uDDF0").replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :flag_tm: ", "\uD83C\uDDF9\uD83C\uDDF2")
                .replace(" :flag_tn: ", "\uD83C\uDDF9\uD83C\uDDF3").replace(" :flag_to: ", "\uD83C\uDDF9\uD83C\uDDF4").replace(" :flag_tr: ", "\uD83C\uDDF9\uD83C\uDDF7")
                .replace(" :flag_tt: ", "\uD83C\uDDF9\uD83C\uDDF9").replace(" :flag_tv: ", "\uD83C\uDDF9\uD83C\uDDFB").replace(" :flag_tw: ", "\uD83C\uDDF9\uD83C\uDDFC")
                .replace(" :flag_tz: ", "\uD83C\uDDF9\uD83C\uDDFF").replace(" :flag_ua: ", "\uD83C\uDDFA\uD83C\uDDE6").replace(" :flag_ug: ", "\uD83C\uDDFA\uD83C\uDDEC")
                .replace(" :flag_um: ", "\uD83C\uDDFA\uD83C\uDDF2").replace(" :flag_us: ", "\uD83C\uDDFA\uD83C\uDDF8").replace(" :flag_uy: ", "\uD83C\uDDFA\uD83C\uDDFE")
                .replace(" :flag_uz: ", "\uD83C\uDDFA\uD83C\uDDFF").replace(" :flag_tg: ", "\uD83C\uDDF9\uD83C\uDDEC").replace(" :flag_th: ", "\uD83C\uDDF9\uD83C\uDDED").replace(" :flag_tj: ", "\uD83C\uDDF9\uD83C\uDDEF").replace(" :flag_tk: ", "\uD83C\uDDF9\uD83C\uDDF0").replace(" :flag_tl: ", "\uD83C\uDDF9\uD83C\uDDF1").replace(" :flag_tm: ", "\uD83C\uDDF9\uD83C\uDDF2").replace(" :flag_tn: ", "\uD83C\uDDF9\uD83C\uDDF3")
                .replace(" :flag_sm: ", "\uD83C\uDDF8\uD83C\uDDF2").replace(" :flag_sl: ", "\uD83C\uDDF8\uD83C\uDDF1").replace(" :flag_sk: ", "\uD83C\uDDF8\uD83C\uDDF0").replace(" :flag_sj: ", "\uD83C\uDDF8\uD83C\uDDEF").replace(" :flag_si: ", "\uD83C\uDDF8\uD83C\uDDEE").replace(" :flag_sh: ", "\uD83C\uDDF8\uD83C\uDDED").replace(" :flag_sg: ", "\uD83C\uDDF8\uD83C\uDDEC").replace(" :flag_se: ", "\uD83C\uDDF8\uD83C\uDDEA").replace(" :flag_sd: ", "\uD83C\uDDF8\uD83C\uDDE9").replace(" :flag_sc: ", "\uD83C\uDDF8\uD83C\uDDE8").replace(" :flag_sb: ", "\uD83C\uDDF8\uD83C\uDDE7").replace(" :flag_sa: ", "\uD83C\uDDF8\uD83C\uDDE6").replace(" :flag_sm: ", "\uD83C\uDDF8\uD83C\uDDE6").replace(" :flag_rw: ", "\uD83C\uDDF7\uD83C\uDDFC").replace(" :flag_ru: ", "\uD83C\uDDF7\uD83C\uDDFA").replace(" :flag_rs: ", "\uD83C\uDDF7\uD83C\uDDF8").replace(" :flag_ro: ", "\uD83C\uDDF7\uD83C\uDDF4").replace(" :flag_re: ", "\uD83C\uDDF7\uD83C\uDDEA").replace(" :flag_qa: ", "\uD83C\uDDF6\uD83C\uDDE6").replace(" :flag_py: ", "\uD83C\uDDF5\uD83C\uDDFE")
                .replace(" :flag_pw: ", "\uD83C\uDDF5\uD83C\uDDFC").replace(" :flag_pt: ", "\uD83C\uDDF5\uD83C\uDDF9").replace(" :flag_ps: ", "\uD83C\uDDF5\uD83C\uDDF8").replace(" :flag_pr: ", "\uD83C\uDDF5\uD83C\uDDF7").replace(" :flag_pn: ", "\uD83C\uDDF5\uD83C\uDDF3").replace(" :flag_pm: ", "\uD83C\uDDF5\uD83C\uDDF2").replace(" :flag_pl: ", "\uD83C\uDDF5\uD83C\uDDF1").replace(" :flag_pk: ", "\uD83C\uDDF5\uD83C\uDDF0").replace(" :flag_ph: ", "\uD83C\uDDF5\uD83C\uDDED").replace(" :flag_pg: ", "\uD83C\uDDF5\uD83C\uDDEC").replace(" :flag_pf: ", "\uD83C\uDDF5\uD83C\uDDEB").replace(" :flag_pe: ", "\uD83C\uDDF5\uD83C\uDDEA").replace(" :flag_pa: ", "\uD83C\uDDF5\uD83C\uDDE6")
                .replace(" :flag_mt: ", "\uD83C\uDDF2\uD83C\uDDF9").replace(" :flag_om: ", "\uD83C\uDDF4\uD83C\uDDF2").replace(" :flag_nz: ", "\uD83C\uDDF3\uD83C\uDDFF").replace(" :flag_nu: ", "\uD83C\uDDF3\uD83C\uDDFA").replace(" :flag_nr: ", "\uD83C\uDDF3\uD83C\uDDF7").replace(" :flag_np: ", "\uD83C\uDDF3\uD83C\uDDF5").replace(" :flag_no: ", "\uD83C\uDDF3\uD83C\uDDF4").replace(" :flag_nl: ", "\uD83C\uDDF3\uD83C\uDDF1").replace(" :flag_ni: ", "\uD83C\uDDF3\uD83C\uDDEE").replace(" :flag_ng: ", "\uD83C\uDDF3\uD83C\uDDEC").replace(" :flag_ne: ", "\uD83C\uDDF3\uD83C\uDDEA").replace(" :flag_nc: ", "\uD83C\uDDF3\uD83C\uDDE8").replace(" :flag_na: ", "\uD83C\uDDF3\uD83C\uDDE6").replace(" :flag_mz: ", "\uD83C\uDDF2\uD83C\uDDFF").replace(" :flag_my: ", "\uD83C\uDDF2\uD83C\uDDFE").replace(" :flag_mx: ", "\uD83C\uDDF2\uD83C\uDDFD").replace(" :flag_mw: ", "\uD83C\uDDF2\uD83C\uDDFC").replace(" :flag_mv: ", "\uD83C\uDDF2\uD83C\uDDFB").replace(" :flag_mu: ", "\uD83C\uDDF2\uD83C\uDDFA")
                .replace(" :flag_ms: ", "\uD83C\uDDF2\uD83C\uDDF8").replace(" :flag_mr: ", "\uD83C\uDDF2\uD83C\uDDF7").replace(" :flag_mq: ", "\uD83C\uDDF2\uD83C\uDDF6").replace(" :flag_mp: ", "\uD83C\uDDF2\uD83C\uDDF5").replace(" :flag_mo: ", "\uD83C\uDDF2\uD83C\uDDF4").replace(" :flag_mn: ", "\uD83C\uDDF2\uD83C\uDDF3").replace(" :flag_mm: ", "\uD83C\uDDF2\uD83C\uDDF2").replace(" :flag_ml: ", "\uD83C\uDDF2\uD83C\uDDF1").replace(" :flag_mk: ", "\uD83C\uDDF2\uD83C\uDDF0").replace(" :flag_mh: ", "\uD83C\uDDF2\uD83C\uDDED").replace(" :flag_mg: ", "\uD83C\uDDF2\uD83C\uDDEC").replace(" :flag_mf: ", "\uD83C\uDDF2\uD83C\uDDEB")
                .replace(" :flag_me: ", "\uD83C\uDDF2\uD83C\uDDEA").replace(" :flag_md: ", "\uD83C\uDDF2\uD83C\uDDE9").replace(" :flag_mc: ", "\uD83C\uDDF2\uD83C\uDDE8").replace(" :flag_ma: ", "\uD83C\uDDF2\uD83C\uDDE6").replace(" :flag_ly: ", "\uD83C\uDDF1\uD83C\uDDFE").replace(" :flag_lv: ", "\uD83C\uDDF1\uD83C\uDDFB").replace(" :flag_lu: ", "\uD83C\uDDF1\uD83C\uDDFA").replace(" :flag_lt: ", "\uD83C\uDDF1\uD83C\uDDF9").replace(" :flag_ls: ", "\uD83C\uDDF1\uD83C\uDDF8").replace(" :flag_lr: ", "\uD83C\uDDF1\uD83C\uDDF7").replace(" :flag_lk: ", "\uD83C\uDDF1\uD83C\uDDF0").replace(" :flag_li: ", "\uD83C\uDDF1\uD83C\uDDEE").replace(" :flag_lc: ", "\uD83C\uDDF1\uD83C\uDDE8").replace(" :flag_lb: ", "\uD83C\uDDF1\uD83C\uDDE7").replace(" :flag_la: ", "\uD83C\uDDF1\uD83C\uDDE6")
                .replace(" :flag_kz:  ", "\uD83C\uDDF0\uD83C\uDDFF").replace(" :flag_ky: ", "\uD83C\uDDF0\uD83C\uDDFE").replace(" :flag_kw: ", "\uD83C\uDDF0\uD83C\uDDFC").replace(" :flag_kr: ", "\uD83C\uDDF0\uD83C\uDDF7").replace(" :flag_kp: ", "\uD83C\uDDF0\uD83C\uDDF5").replace(" :flag_kn: ", "\uD83C\uDDF0\uD83C\uDDF3").replace(" :flag_km: ", "\uD83C\uDDF0\uD83C\uDDF2").replace(" :flag_ki: ", "\uD83C\uDDF0\uD83C\uDDEE").replace(" :flag_kh: ", "\uD83C\uDDF0\uD83C\uDDED").replace(" :flag_kg: ", "\uD83C\uDDF0\uD83C\uDDEC").replace(" :flag_ke: ", "\uD83C\uDDF0\uD83C\uDDEA").replace(" :flag_jp: ", "\uD83C\uDDEF\uD83C\uDDF5").replace(" :flag_jo: ", "\uD83C\uDDEF\uD83C\uDDF4").replace(" :flag_jm: ", "\uD83C\uDDEF\uD83C\uDDF2").replace(" :flag_je: ", "\uD83C\uDDEF\uD83C\uDDEA")
                .replace(" :flag_it: ", "\uD83C\uDDEE\uD83C\uDDF9").replace(" :flag_is: ", "\uD83C\uDDEE\uD83C\uDDF8")
                .replace(" :flag_gd: ", "\uD83C\uDDEC\uD83C\uDDE9").replace(" :flag_ge: ", "\uD83C\uDDEC\uD83C\uDDEA").replace(" :flag_gf: ", "\uD83C\uDDEC\uD83C\uDDEB").replace(" :flag_gg: ", "\uD83C\uDDEC\uD83C\uDDEC").replace(" :flag_gh: ", "\uD83C\uDDEC\uD83C\uDDED").replace(" :flag_gi: ", "\uD83C\uDDEC\uD83C\uDDEE").replace(" :flag_gl: ", "\uD83C\uDDEC\uD83C\uDDF1").replace(" :flag_gm: ", "\uD83C\uDDEC\uD83C\uDDF2").replace(" :flag_gn: ", "\uD83C\uDDEC\uD83C\uDDF3").replace(" :flag_gp: ", "\uD83C\uDDEC\uD83C\uDDF5").replace(" :flag_gq: ", "\uD83C\uDDEC\uD83C\uDDF6").replace(" :flag_gr: ", "\uD83C\uDDEC\uD83C\uDDF7").replace(" :flag_gs: ", "\uD83C\uDDEC\uD83C\uDDF8").replace(" :flag_gt: ", "\uD83C\uDDEC\uD83C\uDDF9").replace(" :flag_gu: ", "\uD83C\uDDEC\uD83C\uDDFA").replace(" :flag_gw: ", "\uD83C\uDDEC\uD83C\uDDFC").replace(" :flag_gy: ", "\uD83C\uDDEC\uD83C\uDDFE").replace(" :flag_hk: ", "\uD83C\uDDED\uD83C\uDDF0").replace(" :flag_hm: ", "\uD83C\uDDED\uD83C\uDDF2")
                .replace(" :flag_hn: ", "\uD83C\uDDED\uD83C\uDDF3").replace(" :flag_hr: ", "\uD83C\uDDED\uD83C\uDDF7").replace(" :flag_ht: ", "\uD83C\uDDED\uD83C\uDDF9").replace(" :flag_hu: ", "\uD83C\uDDED\uD83C\uDDFA").replace(" :flag_ic: ", "\uD83C\uDDEE\uD83C\uDDE8").replace(" :flag_id: ", "\uD83C\uDDEE\uD83C\uDDE9").replace(" :flag_ie: ", "\uD83C\uDDEE\uD83C\uDDEA").replace(" :flag_il: ", "\uD83C\uDDEE\uD83C\uDDF1").replace(" :flag_im: ", "\uD83C\uDDEE\uD83C\uDDF2").replace(" :flag_in: ", "\uD83C\uDDEE\uD83C\uDDF3").replace(" :flag_io: ", "\uD83C\uDDEE\uD83C\uDDF4").replace(" :flag_iq: ", "\uD83C\uDDEE\uD83C\uDDF6").replace(" :flag_ir: ", "\uD83C\uDDEE\uD83C\uDDF7").replace(" :flag_gb: ", "\uD83C\uDDEC\uD83C\uDDE7").replace(" :flag_ga: ", "\uD83C\uDDEC\uD83C\uDDE6").replace(" :flag_fr: ", "\uD83C\uDDEB\uD83C\uDDF7").replace(" :flag_fo: ", "\uD83C\uDDEB\uD83C\uDDF4").replace(" :flag_fm: ", "\uD83C\uDDEB\uD83C\uDDF2").replace(" :flag_fk: ", "\uD83C\uDDEB\uD83C\uDDF0")
                .replace(" :flag_fj: ", "\uD83C\uDDEB\uD83C\uDDEF").replace(" :flag_fi: ", "\uD83C\uDDEB\uD83C\uDDEE").replace(" :flag_eu: ", "\uD83C\uDDEA\uD83C\uDDFA").replace(" :flag_et: ", "\uD83C\uDDEA\uD83C\uDDF9").replace(" :flag_es: ", "\uD83C\uDDEA\uD83C\uDDF8").replace(" :flag_er:", "\uD83C\uDDEA\uD83C\uDDF7").replace(" :flag_eh: ", "\uD83C\uDDEA\uD83C\uDDED").replace(" :flag_eg: ", "\uD83C\uDDEA\uD83C\uDDEC").replace(" :flag_ee: ", "\uD83C\uDDEA\uD83C\uDDEA").replace(" :flag_ec: ", "\uD83C\uDDEA\uD83C\uDDE8").replace(" :flag_ea: ", "\uD83C\uDDEA\uD83C\uDDE6").replace(" :flag_dz: ", "\uD83C\uDDE9\uD83C\uDDFF").replace(" :flag_do: ", "\uD83C\uDDE9\uD83C\uDDF4").replace(" :flag_dm: ", "\uD83C\uDDE9\uD83C\uDDF2").replace(" :flag_dk: ", "\uD83C\uDDE9\uD83C\uDDF0")
                .replace(" :flag_dj: ", "\uD83C\uDDE9\uD83C\uDDEF").replace(" :flag_dg: ", "\uD83C\uDDE9\uD83C\uDDEC").replace(" :flag_de: ", "\uD83C\uDDE9\uD83C\uDDEA").replace(" :flag_cz: ", "\uD83C\uDDE8\uD83C\uDDFF").replace(" :flag_cy: ", "\uD83C\uDDE8\uD83C\uDDFE").replace(" :flag_cx: ", "\uD83C\uDDE8\uD83C\uDDFD").replace(" :flag_cw: ", "\uD83C\uDDE8\uD83C\uDDFC").replace(" :flag_cv: ", "\uD83C\uDDE8\uD83C\uDDFB").replace(" :flag_cu: ", "\uD83C\uDDE8\uD83C\uDDFA").replace(" :flag_cr: ", "\uD83C\uDDE8\uD83C\uDDF7").replace(" :flag_cp: ", "\uD83C\uDDE8\uD83C\uDDF5").replace(" :flag_bb: ", "\uD83C\uDDE7\uD83C\uDDE7").replace(" :flag_bd: ", "\uD83C\uDDE7\uD83C\uDDE9").replace(" :flag_be: ", "\uD83C\uDDE7\uD83C\uDDEA").replace(" :flag_bf: ", "\uD83C\uDDE7\uD83C\uDDEB").replace(" :flag_bg: ", "\uD83C\uDDE7\uD83C\uDDEC").replace(" :flag_bh: ", "\uD83C\uDDE7\uD83C\uDDED").replace(" :flag_bi: ", "\uD83C\uDDE7\uD83C\uDDEE").replace(" :flag_bj: ", "\uD83C\uDDE7\uD83C\uDDEF").replace(" :flag_bl: ", "\uD83C\uDDE7\uD83C\uDDF1").replace(" :flag_bm: ", "\uD83C\uDDE7\uD83C\uDDF2")
                .replace(" :flag_bn: ", "\uD83C\uDDE7\uD83C\uDDF3").replace(" :flag_bo: ", "\uD83C\uDDE7\uD83C\uDDF4").replace(" :flag_bq: ", "\uD83C\uDDE7\uD83C\uDDF6").replace(" :flag_br: ", "\uD83C\uDDE7\uD83C\uDDF7").replace(" :flag_bs: ", "\uD83C\uDDE7\uD83C\uDDF8").replace(" :flag_bt: ", "\uD83C\uDDE7\uD83C\uDDF9").replace(" :flag_bv: ", "\uD83C\uDDE7\uD83C\uDDFB").replace(" :flag_bw: ", "\uD83C\uDDE7\uD83C\uDDFC").replace(" :flag_by: ", "\uD83C\uDDE7\uD83C\uDDFE").replace(" :flag_bz: ", "\uD83C\uDDE7\uD83C\uDDFF").replace(" :flag_ca: ", "\uD83C\uDDE8\uD83C\uDDE6").replace(" :flag_cc: ", "\uD83C\uDDE8\uD83C\uDDE8").replace(" :flag_cd: ", "\uD83C\uDDE8\uD83C\uDDE9").replace(" :flag_cf: ", "\uD83C\uDDE8\uD83C\uDDEB").replace(" :flag_cg: ", "\uD83C\uDDE8\uD83C\uDDEC").replace(" :flag_ch: ", "\uD83C\uDDE8\uD83C\uDDED").replace(" :flag_ci: ", "\uD83C\uDDE8\uD83C\uDDEE").replace(" :flag_ck: ", "\uD83C\uDDE8\uD83C\uDDF0").replace(" :flag_cl: ", "\uD83C\uDDE8\uD83C\uDDF1").replace(" :flag_cm: ", "\uD83C\uDDE8\uD83C\uDDF2")
                .replace(" :flag_cn: ", "\uD83C\uDDE8\uD83C\uDDF3").replace(" :flag_co: ", "\uD83C\uDDE8\uD83C\uDDF4").replace(" :england: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F").replace(" :scotland: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F").replace(" :wales: ", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F").replace(" :rainbow_flag: ", "\uD83C\uDFF3️\u200D\uD83C\uDF08").replace(" :flag_ac: ", "\uD83C\uDDE6\uD83C\uDDE8").replace(" :flag_ad: ", "\uD83C\uDDE6\uD83C\uDDE9").replace(" :flag_ae: ", "\uD83C\uDDE6\uD83C\uDDEA").replace(" :flag_af: ", "\uD83C\uDDE6\uD83C\uDDEB").replace(" :flag_ag: ", "\uD83C\uDDE6\uD83C\uDDEC").replace(" :flag_ai: ", "\uD83C\uDDE6\uD83C\uDDEE").replace(" :flag_al: ", "\uD83C\uDDE6\uD83C\uDDF1").replace(" :flag_am: ", "\uD83C\uDDE6\uD83C\uDDF2").replace(" :flag_ao: ", "\uD83C\uDDE6\uD83C\uDDF4").replace(" :flag_aq: ", "\uD83C\uDDE6\uD83C\uDDF6").replace(" :flag_ar:", "\uD83C\uDDE6\uD83C\uDDF7").replace(" :flag_as: ", "\uD83C\uDDE6\uD83C\uDDF8").replace(" :flag_at: ", "\uD83C\uDDE6\uD83C\uDDF9").replace(" :flag_au: ", "\uD83C\uDDE6\uD83C\uDDFA")
                .replace(" :flag_aw: ", "\uD83C\uDDE6\uD83C\uDDFC").replace(" :flag_ax: ", "\uD83C\uDDE6\uD83C\uDDFD").replace(" :flag_az: ", "\uD83C\uDDE6\uD83C\uDDFF").replace(" :flag_ba: ", "\uD83C\uDDE7\uD83C\uDDE6")
                .replace(" &female;  ", " ♀️ ").replace(" :woman_with_headscarf_tone1: ", "\uD83E\uDDD5\uD83C\uDFFB").replace(" :woman_tone1: ", "\uD83D\uDC69\uD83C\uDFFB").replace(" :vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB").replace(" :selfie_tone1: ", "\uD83E\uDD33\uD83C\uDFFB").replace(" :santa_tone1: ", "\uD83C\uDF85\uD83C\uDFFB").replace(" :princess_tone1: ", "\uD83D\uDC78\uD83C\uDFFB").replace(" :prince_tone1: ", "\uD83E\uDD34\uD83C\uDFFB").replace(" :pregnant_woman_tone1: ", "\uD83E\uDD30\uD83C\uDFFB").replace(" :pray_tone1: ", "\uD83D\uDE4F\uD83C\uDFFB").replace(" :fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB").replace(" :girl_tone1: ", "\uD83D\uDC67\uD83C\uDFFB").replace(" :guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB").replace(" :mage_tone1: ", "\uD83E\uDDD9\uD83C\uDFFB").replace(" :man_dancing_tone1: ", "\uD83D\uDD7A\uD83C\uDFFB").replace(" :man_in_business_suit_levitating_tone1: ", "\uD83D\uDD74\uD83C\uDFFB").replace(" :man_in_tuxedo_tone1: ", "\uD83E\uDD35\uD83C\uDFFB").replace(" :man_tone1: ", "\uD83D\uDC68\uD83C\uDFFB").replace(" :man_with_chinese_cap_tone1: ", "\uD83D\uDC72\uD83C\uDFFB").replace(" :merperson_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB").replace(" :mrs_claus_tone1: ", "\uD83E\uDD36\uD83C\uDFFB").replace(" :muscle_tone1: ", "\uD83D\uDCAA\uD83C\uDFFB").replace(" :nail_care_tone1: ", "\uD83D\uDC85\uD83C\uDFFB").replace(" :nose_tone1: ", "\uD83D\uDC43\uD83C\uDFFB")
                .replace(" :older_adult_tone1: ", "\uD83E\uDDD3\uD83C\uDFFB").replace(" :older_man_tone1: ", "\uD83D\uDC74\uD83C\uDFFB").replace(" :older_woman_tone1: ", "\uD83D\uDC75\uD83C\uDFFB").replace(" :person_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB").replace(" :person_facepalming_tone1: ", "\uD83D\uDC69\uD83C\uDFFB").replace(" :person_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB").replace(" :person_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB").replace(" :person_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB").replace(" :person_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB").replace(" :person_getting_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB").replace(" :person_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB").replace(" :person_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB").replace(" :person_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB").replace(" :person_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB").replace(" :person_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB").replace(" :person_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB").replace(" :person_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB").replace(" :police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB").replace(" :elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB").replace(" :ear_tone1: ", "\uD83D\uDC42\uD83C\uDFFB")
                .replace(" :detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB").replace(" :dancer_tone1: ", "\uD83D\uDC83\uD83C\uDFFB").replace(" :construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB").replace(" :child_tone1: ", "\uD83E\uDDD2\uD83C\uDFFB").replace(" :bride_with_veil_tone1: ", "\uD83D\uDC70\uD83C\uDFFB").replace(" :boy_tone1: ", "\uD83D\uDC66\uD83C\uDFFB").replace(" :blond_haired_person_tone1: ", "\uD83D\uDC71\uD83C\uDFFB").replace(" :bearded_person_tone1: ", "\uD83E\uDDD4\uD83C\uDFFB").replace(" :baby_tone1: ", "\uD83D\uDC76\uD83C\uDFFB").replace(" :angel_tone1: ", "\uD83D\uDC7C\uD83C\uDFFB").replace(" :adult_tone1: ", "\uD83E\uDDD1\uD83C\uDFFB").replace(" :woman_technologist: ", "\uD83D\uDC69\u200D\uD83D\uDCBB").replace(" :woman_teacher: ", "\uD83D\uDC69\u200D\uD83C\uDFEB").replace(" :woman_student: ", "\uD83D\uDC69\u200D\uD83C\uDF93").replace(" :woman_singer: ", "\uD83D\uDC69\u200D\uD83C\uDFA4").replace(" :woman_scientist: ", "\uD83D\uDC69\u200D\uD83D\uDD2C").replace(" :woman_office_worker: ", "\uD83D\uDC69\u200D\uD83D\uDCBC").replace(" :woman_mechanic: ", "\uD83D\uDC69\u200D\uD83D\uDD27").replace(" :woman_firefighter: ", "\uD83D\uDC69\u200D\uD83D\uDE92")
                .replace(" :woman_farmer: ", "\uD83D\uDC69\u200D\uD83C\uDF3E").replace(" :woman_factory_worker: ", "\uD83D\uDC69\u200D\uD83C\uDFED").replace(" :woman_cook: ", "\uD83D\uDC69\u200D\uD83C\uDF73").replace(" :woman_astronaut: ", "\uD83D\uDC69\u200D\uD83D\uDE80").replace(" :woman_artist: ", "\uD83D\uDC69\u200D\uD83C\uDFA8")
                .replace(" :man_technologist: ", "\uD83D\uDC68\u200D\uD83D\uDCBB").replace(" :man_teacher: ", "\uD83D\uDC68\u200D\uD83C\uDFEB").replace(" :man_student: ", "\uD83D\uDC68\u200D\uD83C\uDF93").replace(" :man_singer: ", "\uD83D\uDC68\u200D\uD83C\uDFA4").replace(" :man_scientist: ", "\uD83D\uDC68\u200D\uD83D\uDD2C").replace(" :man_office_worker: ", "\uD83D\uDC68\u200D\uD83D\uDCBC").replace(" :woman_genie: ", "\uD83E\uDDDE\u200D♀️").replace(" :woman_gesturing_no: ", "\uD83D\uDE45\u200D♀️").replace(" :woman_gesturing_ok: ", "\uD83D\uDE46\u200D♀️").replace(" :woman_getting_face_massage: ", "\uD83D\uDC86\u200D♀️").replace(" :woman_getting_haircut: ", "\uD83D\uDC87\u200D♀️").replace(" :woman_guard: ", "\uD83D\uDC82\u200D♀️").replace(" :woman_health_worker: ", "\uD83D\uDC69\u200D⚕️").replace(" :woman_judge: ", "\uD83D\uDC69\u200D⚖️").replace(" :woman_mage: ", "\uD83E\uDDD9\u200D♀️").replace(" :woman_pilot: ", "\uD83D\uDC69\u200D✈️").replace(" :woman_police_officer: ", "\uD83D\uDC6E\u200D♀️️").replace(" :woman_pouting: ", "\uD83D\uDE4E\u200D♀️")
                .replace(" :woman_raising_hand: ", "\uD83D\uDE4B\u200D♀️").replace(" :woman_running: ", "\uD83C\uDFC3\u200D♀️").replace(" :woman_shrugging: ", "\uD83E\uDD37\u200D♀️")
                .replace(" :woman_tipping_hand: ", "\uD83D\uDC81\u200D♀️").replace(" :woman_vampire: ", "\uD83E\uDDDB\u200D♀️").replace(" :woman_walking: ", "\uD83D\uDEB6\u200D♀️").replace(" :woman_wearing_turban: ", "\uD83D\uDC73\u200D♀️")
                .replace(" :women_with_bunny_ears_partying: ", "\uD83D\uDC6F\u200D♀️").replace(" :family_man_girl: ", "\uD83D\uDC68\u200D\uD83D\uDC67").replace(" :family_man_boy: ", "\uD83D\uDC68\u200D\uD83D\uDC66").replace(" :family_woman_boy: ", "\uD83D\uDC69\u200D\uD83D\uDC66").replace(" :family_woman_girl: ", "\uD83D\uDC69\u200D\uD83D\uDC67").replace(" :man_artist: ", "\uD83D\uDC68\u200D\uD83C\uDFA8").replace(" :man_astronaut: ", "\uD83D\uDC68\u200D\uD83D\uDE80")
                .replace(" :man_cook: ", "\uD83D\uDC68\u200D\uD83C\uDF73️").replace(" :man_factory_worker: ", "\uD83D\uDC68\u200D\uD83C\uDFED").replace(" :man_farmer: ", "\uD83D\uDC68\u200D\uD83C\uDF3E️").replace(" :man_firefighter: ", "\uD83D\uDC68\u200D\uD83D\uDE92️").replace(" :man_mechanic: ", "\uD83D\uDC68\u200D\uD83D\uDD27").replace(" :woman_frowning: ", "\uD83D\uDE4D\u200D♀️️").replace(" :woman_fairy: ", "\uD83E\uDDDA\u200D♀️️").replace(" :woman_facepalming: ", "\uD83E\uDD26\u200D♀️️").replace(" :woman_elf: ", "\uD83E\uDDDD\u200D♀️️").replace(" :woman_construction_worker: ", "\uD83D\uDC77\u200D♀️️")
                .replace(" :woman_bowing: ", "\uD83D\uDE47\u200D♀️️").replace(" :men_with_bunny_ears_partying: ", "\uD83D\uDC6F\u200D♂️").replace(" :man_wearing_turban: ", "\uD83D\uDC73\u200D♂️️").replace(" :men_with_bunny_ears_partying: ", "\uD83D\uDC68\u200D\uD83D\uDE92️").replace(" :man_walking: ", "\uD83D\uDEB6\u200D♂️").replace(" :man_vampire: ", "\uD83E\uDDDB\u200D♂️").replace(" :man_tipping_hand: ", "\uD83D\uDC81\u200D♂️️️").replace(" :man_shrugging: ", "\uD83E\uDD37\u200D♂️️️").replace(" :man_running: ", "\uD83C\uDFC3\u200D♂️️️").replace(" :man_raising_hand: ", "\uD83D\uDE4B\u200D♂️️️")
                .replace(" :man_pouting: ", "\uD83D\uDE4E\u200D♂️").replace(" :merman: ", "\uD83E\uDDDC\u200D♂️").replace(" :mermaid: ", "\uD83E\uDDDC\u200D♀️").replace(" :man_police_officer: ", "\uD83D\uDC6E\u200D♂️️").replace(" :man_pilot: ", "\uD83D\uDC68\u200D✈️").replace(" :man_mage: ", "\uD83E\uDDD9\u200D♂️️️").replace(" :man_judge: ", "\uD83D\uDC68\u200D⚖️").replace(" :man_health_worker: ", "\uD83D\uDC68\u200D⚕️️️").replace(" :man_guard: ", "\uD83D\uDC82\u200D♂️").replace(" :man_getting_haircut: ", "\uD83D\uDC87\u200D♂️️️")
                .replace(" :man_getting_face_massage: ", "\uD83D\uDC86\u200D♂️").replace(" :man_gesturing_ok: ", "\uD83D\uDE46\u200D♂️").replace(" :man_gesturing_no: ", "\uD83D\uDE45\u200D♂️️").replace(" :man_genie: ", "\uD83E\uDDDE\u200D♂️").replace(" :man_frowning: ", "\uD83D\uDE4D\u200D♂️").replace(" :man_fairy: ", "\uD83E\uDDDA\u200D♂️").replace(" :man_facepalming: ", "\uD83E\uDD26\u200D♂️").replace(" :woman_detective: ", "\uD83D\uDD75️\u200D♀️").replace(" :man_artist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8").replace(" :man_astronaut_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDE80")
                .replace(" :man_cook_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF73️").replace(" :man_factory_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFED").replace(" :man_farmer_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF3E️").replace(" :man_firefighter_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDE92").replace(" :man_mechanic_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDD27").replace(" :man_office_worker_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBC️️").replace(" :man_scientist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDD2C️️").replace(" :man_singer_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA4️️").replace(" :man_student_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF93️️").replace(" :man_teacher_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFEB️️")
                .replace(" :man_technologist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB").replace(" :woman_artist_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFA8").replace(" :woman_astronaut_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDE80️").replace(" :woman_cook_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF73").replace(" :woman_factory_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFED").replace(" :woman_farmer_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF3E").replace(" :woman_firefighter_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDE92").replace(" :woman_mechanic_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDD27️️").replace(" :woman_office_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBC️️").replace(" :woman_scientist_tone1: ", "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFEB️️")
                .replace(" :woman_singer_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFA4").replace(" :woman_student_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93").replace(" :woman_teacher_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDFEB️").replace(" :woman_technologist_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBB").replace(" :blond-haired_man: ", "\uD83D\uDC71\u200D♂️").replace(" :blond-haired_woman: ", "\uD83D\uDC71\u200D♀️").replace(" :man_bowing: ", "\uD83D\uDE47\u200D♂️").replace(" :man_construction_worker: ", "\uD83D\uDC77\u200D♂️").replace(" :man_elf: ", "\uD83E\uDDDD\u200D♂️")
                .replace(" :man_detective: ", "\uD83D\uDD75️\u200D♂️").replace(" :woman_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB\u200D♀️").replace(" :woman_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB\u200D♀️").replace(" :woman_vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB\u200D♀️").replace(" :woman_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB\u200D♀️").replace(" :woman_shrugging_tone1: ", "\uD83E\uDD37\uD83C\uDFFB\u200D♀️").replace(" :woman_running_tone1: ", "\uD83C\uDFC3\uD83C\uDFFB\u200D♀️").replace(" :woman_raising_hand_tone1: ", "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️").replace(" :woman_pouting_tone1: ", "\uD83D\uDE4E\uD83C\uDFFB\u200D♀️").replace(" :woman_police_officer_tone1: ", "\uD83D\uDC6E\uD83C\uDFFB\u200D♀️").replace(" :woman_pilot_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D✈️").replace(" :woman_judge_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D⚖️").replace(" :woman_health_worker_tone1: ", "\uD83D\uDC69\uD83C\uDFFB\u200D⚕️").replace(" :woman_guard_tone1: ", "\uD83D\uDC82\uD83C\uDFFB\u200D♀️").replace(" :woman_getting_haircut_tone1: ", "\uD83D\uDC87\uD83C\uDFFB\u200D♀️")
                .replace(" :woman_getting_face_massage_tone1: ", "\uD83D\uDC86\uD83C\uDFFB\u200D♀️").replace(" :woman_gesturing_ok_tone1: ", "\uD83D\uDE46\uD83C\uDFFB\u200D♀️").replace(" :woman_gesturing_no_tone1: ", "\uD83D\uDE45\uD83C\uDFFB\u200D♀️").replace(" :woman_frowning_tone1: ", "\uD83D\uDE4D\uD83C\uDFFB\u200D♀️").replace(" :woman_fairy_tone1: ", "\uD83E\uDDDA\uD83C\uDFFB\u200D♀️").replace(" :woman_facepalming_tone1: ", "\uD83E\uDD26\uD83C\uDFFB\u200D♀️").replace(" :woman_elf_tone1: ", "\uD83E\uDDDD\uD83C\uDFFB\u200D♀️").replace(" :woman_detective_tone1: ", "\uD83D\uDD75\uD83C\uDFFB\u200D♀️").replace(" :woman_construction_worker_tone1: ", "\uD83D\uDC77\uD83C\uDFFB\u200D♀️").replace(" :woman_bowing_tone1: ", "\uD83D\uDE47\uD83C\uDFFB\u200D♀️").replace(" :merman_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB\u200D♂️").replace(" :mermaid_tone1: ", "\uD83E\uDDDC\uD83C\uDFFB\u200D♀️").replace(" :man_wearing_turban_tone1: ", "\uD83D\uDC73\uD83C\uDFFB\u200D♂️").replace(" :man_walking_tone1: ", "\uD83D\uDEB6\uD83C\uDFFB\u200D♂️").replace(" :man_vampire_tone1: ", "\uD83E\uDDDB\uD83C\uDFFB\u200D♂️").replace(" :man_tipping_hand_tone1: ", "\uD83D\uDC81\uD83C\uDFFB\u200D♂️")
                .replace(" &phone; ", " ☎️ ");*/

        return convertMessage.trim();
    }

    public static boolean checkStoragePermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                )) {
            return false;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                )) {
            return false;
        } else {
            return true;
        }
    }

    public static String notificationType(int type) {
        switch (type) {
            case Constants.NotificationType.NOTIFICATION_CHECK_IN:
                return "Check_in";
            case Constants.NotificationType.NOTIFICATION_CHECK_OUT:
                return "Check_out";
            case Constants.NotificationType.NOTIFICATION_LEAVE_CASUAL:
                return "Casual Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_SICK:
                return "Sick Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_OPTIONAL:
                return "Optional Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_COVID:
                return "Covid Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_COVID_WFH:
                return "Covid WFH Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_PATERNITY:
                return "Paternity Leave";
            case Constants.NotificationType.NOTIFICATION_LEAVE_MATERNITY:
                return "Maternity Leave";
        }
        return "";
    }

}
