package net.d4rkfly3r.fireline.phone;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectorUtil extends AsyncTask<Void, String, String> {

    private HttpURLConnection mUrlConnection;
    private Runnable mFinishedCallback;
    private String mReturnData;

    public ConnectorUtil(String endpoint) {
        try {
            mUrlConnection = (HttpURLConnection) new URL(endpoint).openConnection();
            mUrlConnection.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            for (String line = ""; line != null; line = bufferedReader.readLine()) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//
//        String line;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return stringBuilder.toString();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            mUrlConnection.connect();
            System.err.println(mUrlConnection.getResponseCode());
            final String s = convertStreamToString(mUrlConnection.getInputStream());
            System.out.println(s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mUrlConnection.disconnect();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        setReturnData(s);
        if (mFinishedCallback != null) {
            mFinishedCallback.run();
        }
    }

    public Runnable getFinishedCallback() {
        return mFinishedCallback;
    }

    public void setFinishedCallback(Runnable finishedCallback) {
        this.mFinishedCallback = finishedCallback;
    }

    public String getReturnData() {
        return mReturnData;
    }

    public void setReturnData(String returnData) {
        this.mReturnData = returnData;
    }


}
