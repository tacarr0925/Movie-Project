package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.MovieTrailer;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Travis on 6/19/2017.
 */
//TODO add onPreExecute to set visibility of a loading Indicator
public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<MovieTrailer>> {
    private Context mContext;
    private AsyncTaskCallBack listener;

    public FetchTrailerTask(Context context, AsyncTaskCallBack listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public interface AsyncTaskCallBack {
        public void onAsyncTaskComplete(ArrayList<MovieTrailer> list);
    }

    @Override
    protected ArrayList<MovieTrailer> doInBackground(String... params) {
        URL trailerRequestUrl = NetworkUtils.buildTrailerUrl(params[0]);

        try {
            String jsonTrailerResponse = NetworkUtils
                    .getResponseFromHttpUrl(trailerRequestUrl);

            ArrayList<MovieTrailer> jsonTrailerList = MovieJsonUtils
                    .getTrailerStringFromJson(mContext, jsonTrailerResponse);

            return jsonTrailerList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieTrailer> list) {
        //TODO update to set visibility of loadingIndicator and display error if no videos
        if (list != null) {
            listener.onAsyncTaskComplete(list);
        }
    }
}
