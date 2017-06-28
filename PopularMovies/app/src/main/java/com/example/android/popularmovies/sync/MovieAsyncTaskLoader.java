package com.example.android.popularmovies.sync;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.MovieInfo;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Travis on 6/25/2017.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieInfo>> {

    ArrayList<MovieInfo> mMovieInfoList;

    public MovieAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mMovieInfoList != null) {
            deliverResult(mMovieInfoList);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<MovieInfo> loadInBackground() {
        final Context context = getContext();
        URL movieRequestUrl = NetworkUtils.getMovieUrl(context);

        try {
            String jsonMovieDBResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);

            mMovieInfoList = MovieJsonUtils
                    .getMovieDBStringsFromJson(jsonMovieDBResponse);

            return mMovieInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieInfo> data) {
        mMovieInfoList = data;
        super.deliverResult(data);
    }
}
