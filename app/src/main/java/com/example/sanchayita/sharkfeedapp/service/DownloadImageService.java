package com.example.sanchayita.sharkfeedapp.service;



import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadImageService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String DOWNLOAD = "install";
    public static final String DELETE = "delete";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.example.sanchayita.downloadImgservice.receiver";
    String TAG = "DownloadService";
    private static final String DIR_NAME = "SharkFeed";


    public DownloadImageService() {
        super("DownloadImgService");

    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {

        String urlPath = intent.getStringExtra(URL);

        File sharkFeedDir = new File(Environment.getExternalStorageDirectory() + "/" + DIR_NAME);
        if (!sharkFeedDir.exists()) {
            sharkFeedDir.mkdir();
        }
        File output = new File(sharkFeedDir.getPath() + "/" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");

        Log.v(TAG , "Download Destination : " + output.getPath());

        InputStream input = null;
        OutputStream fos = null;

        if (output.exists()) {

            Log.i(TAG, output + "exists . Deleting it");
            output.delete();

        }

        try {

            java.net.URL url = new URL(urlPath);
            URLConnection conn = url.openConnection();
            conn.connect();

            // input stream to read file
            input = new BufferedInputStream(url.openStream());

            output.createNewFile();

            fos = new FileOutputStream(output.getPath());

            byte data[] = new byte[1024];
            int count;
            long  total = 0;
            long lengthOfFile = conn.getContentLength();
            Log.i(TAG, "Length of file in Bytes " + lengthOfFile);

            while ((count = input.read(data, 0, 1024)) != -1  ) {

                fos.write(data, 0, count);
                total += count;
                // In Log file
                Log.i(TAG, "Downloading File " + output);
                Log.i(TAG,"Download Completed" + (int)((total*100)/lengthOfFile) + "%");
            }
            // successfully finished
            if (lengthOfFile == total) {

                result = Activity.RESULT_OK;
            }
        } catch (Exception e) {
            Log.e(TAG,  e.getMessage());
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG,e.getMessage());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }
        Log.v(TAG,"File Downloaded :" + output.getAbsolutePath());
        publishResults(output.getAbsolutePath(), result);

    }

    private void publishResults(String outputPath, int result) {

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

   @Override
    public void onDestroy()
    {

        super.onDestroy();

    }
}







































