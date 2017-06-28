package com.example.android.popularmovies.sync;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Travis on 6/27/2017.
 */

public class ReviewAsyncTaskLoader extends AsyncTaskLoader<ArrayList> {

    ArrayList<MovieReview> mMovieRevieList;

    String mMovieId;

    public ReviewAsyncTaskLoader(Context context, String movieId) {
        super(context);
        this.mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mMovieRevieList != null) {
            deliverResult(mMovieRevieList);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<MovieReview> loadInBackground() {

        URL reviewRequestUrl = NetworkUtils.buildReviewUrl(mMovieId);

        try {
            String jsonMovieDBResponse = NetworkUtils
                    .getResponseFromHttpUrl(reviewRequestUrl);

            mMovieRevieList = MovieJsonUtils
                    .getReviewStringFromJson(jsonMovieDBResponse);

            return mMovieRevieList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList data) {
        mMovieRevieList = data;
        super.deliverResult(data);
    }
}
