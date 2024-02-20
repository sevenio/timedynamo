package com.tvisha.trooptime.activity.activity.apiHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shiva prasad on 21/07/2016.
 */

public class ApiCallHelper extends AsyncTask<String, Void, String> {

    ApiCallBackListener listener;
    ApiCallHelper apiCallHelper;
    Map<String, String> apiParams;
    String urlStr;
    long request_timeout = 0;
    //private CustomProgressBar progress;
    private Context ctx;
    public ApiCallHelper(Context ctx, String url, Map mapParams) {
        apiParams = mapParams;
        urlStr = url;
        apiCallHelper = this;
        this.request_timeout = 30000;
        this.ctx = ctx;
    }

    public ApiCallHelper(Context ctx, String url, Map mapParams, long request_timeout) {
        apiParams = mapParams;
        urlStr = url;
        this.request_timeout = request_timeout;
        apiCallHelper = this;
        this.ctx = ctx;
    }

    public void setApiCallBackListener(ApiCallBackListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private void showProgressDialog() {
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            //startTimer(request_timeout);
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = urlParametersData(apiParams);


            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (Exception e) {
            listener.onFailedToCompleteTask(e);
        }
        return null;
    }

    private String urlParametersData(Map<String, String> apiParams) throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<>();
        keys.addAll(apiParams.keySet());
        String data = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = apiParams.get(key);

            if (null == value) {
                value = "";
            }

            if (i == 0) {
                data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            } else {
                data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        cancelProgressBar();
        try {
            listener.onCompleteTask(s);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                listener.onFailedToCompleteTask(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void cancelProgressBar() {
       /* try{
            if(progress.isShowing()){
                progress.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public interface ApiCallBackListener {
        public void onCompleteTask(String responce);

        public void onFailedToCompleteTask(Exception e);

        public void onRequestTimeout(String res);
    }

    public class RequestTimer extends CountDownTimer {

        public RequestTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            apiCallHelper.cancel(true);
            listener.onRequestTimeout("{\"success\":\"false\", \"message\":\"Request Timeout / Unknown host/Host not reachable.\"}");
        }
    }

}
