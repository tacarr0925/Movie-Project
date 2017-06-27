package com.example.android.popularmovies.sync;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.MovieTrailer;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Travis on 6/27/2017.
 */

public class TrailerAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieTrailer>> {

    ArrayList<MovieTrailer> mMovieTrailerList;

    String mMovieId;

    public TrailerAsyncTaskLoader(Context context, String movieId) {
        super(context);
        this.mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(mMovieTrailerList != null) {
            deliverResult(mMovieTrailerList);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<MovieTrailer> loadInBackground() {
        final Context context = getContext();
        URL trailerRequestUrl = NetworkUtils.buildTrailerUrl(mMovieId);

        try {
            String jsonMovieDBResponse = NetworkUtils
                    .getResponseFromHttpUrl(trailerRequestUrl);

            mMovieTrailerList = MovieJsonUtils
                    .getTrailerStringFromJson(context, jsonMovieDBResponse);

            return mMovieTrailerList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieTrailer> data) {
        mMovieTrailerList = data;
        super.deliverResult(data);
    }
}
