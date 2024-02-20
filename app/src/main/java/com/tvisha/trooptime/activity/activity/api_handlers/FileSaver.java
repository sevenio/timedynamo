package com.tvisha.trooptime.activity.activity.api_handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.tvisha.trooptime.activity.activity.helper.ServerUrls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class FileSaver extends AsyncTask<String, Void, File> {

    private static int FILE_SUCCESSFULLY_DOWNLOADED = 1;
    private static int FILE_ABORTED = 3;
    Context ctx;
    FileSaverListener listener;
    private String downloadUrl;
    private String fileName;
    private String fileSavingPath = null;

    public FileSaver(Context ctx, String downloadUrl, String fileName, String fileSavingPath) {
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.fileSavingPath = fileSavingPath;
        this.ctx = ctx;
    }

    public FileSaver(Context ctx, String downloadUrl, String fileName) {
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.ctx = ctx;
    }

    public void setListener(FileSaverListener listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(String... params) {
        try {
            if (isValidURL(downloadUrl)) {
                if (null != downloadUrl && !downloadUrl.equals("")) {
                    return downloadImagesToSdCard(downloadUrl, fileName);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailedToCompleteTask(e);
            return null;
        }
    }

    public boolean isValidURL(String url) {

        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFailedToCompleteTask(e);
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            listener.onFailedToCompleteTask(e);
            return false;
        }

        return true;
    }

    private File downloadImagesToSdCard(String downloadUrl, String fileName) {
        try {

            URL url = new URL(downloadUrl);


            File myDir = new File(Environment.getExternalStorageDirectory(), ServerUrls.imagesUri);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            File file = new File(myDir, fileName);
            if (file.exists()) {
                return file;
            } else if (!file.exists()) {

                /* Open a connection */
                URLConnection ucon = url.openConnection();
                InputStream inputStream = null;
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }

                FileOutputStream fos = new FileOutputStream(file);
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;

                }
                fos.close();
                return file;
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(File file) {
        if (null != file) {
            listener.onAfterFileSaved(file, FILE_SUCCESSFULLY_DOWNLOADED);
        } else {
            listener.onAfterFileSaved(file, FILE_ABORTED);
        }
        super.onPostExecute(file);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public interface FileSaverListener {
        void onAfterFileSaved(File file, int Status);

        void onFailedToCompleteTask(Exception e);
        //void onDownloadProgress(Void downloadPercent);
    }


}