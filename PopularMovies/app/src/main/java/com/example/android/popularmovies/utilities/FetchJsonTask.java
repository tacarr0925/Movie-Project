package com.example.android.popularmovies.utilities;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by Travis on 6/21/2017.
 */

public class FetchJsonTask extends AsyncTask<Object, Void, String> {
    private AsyncTaskCallBack listener;

    public FetchJsonTask(AsyncTaskCallBack listener) {
        this.listener = listener;
    }

    public interface AsyncTaskCallBack {
        public void onAsyncTaskComplete(String jsonString);
    }

    @Override
    protected String doInBackground(Object... params) {
        int taskId = (int)params[0];
        URL requestUrl = (URL)params[1];

        try {
            String jsonResponse = NetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            switch (taskId) {
                case 100:

                    break;
                default:
                    break;
            }
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String jsonString) {
        //TODO update to set visibility of loadingIndicator and display error if no videos
        if (jsonString != null) {
            listener.onAsyncTaskComplete(jsonString);
        }
    }
}
