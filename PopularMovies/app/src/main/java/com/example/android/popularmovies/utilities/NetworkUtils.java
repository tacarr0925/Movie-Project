package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Travis on 5/15/2017.
 * These utilities will be used do communicate with the MovieDB API.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final int FIVE_SECONDS = 5000;
    private static final int TEN_SECONDS = 10000;

    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";


    final static String PARM_API_KEY = "api_key";
    final static String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    final static String sortByPopMovies = "popular";
    final static String sortByTopRated = "top_rated";
    final static String VIDEOS = "videos";
    final static String REVIEWS = "reviews";

    public static URL getMovieUrl(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForMovieSort = context.getString(R.string.pref_sort_key);
        String defaultMovieSort = context.getString(R.string.pref_pop_movies_value);
        URL url;

        String sortBy = sharedPreferences.getString(keyForMovieSort, defaultMovieSort);

        if (sortBy.equals(context.getString(R.string.pref_pop_movies_value))) {
            url = buildUrlWithPopularMovies();
        } else if (sortBy.equals(context.getString(R.string.pref_top_rated_value))) {
            url =  buildUrlWithTopRatedMovies();
        } else {
            url = buildUrlWithPopularMovies();
        }

        return url;
    }

    public static URL buildUrlWithPopularMovies() {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(sortByPopMovies)
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO take out tag
        Log.d(TAG, url.toString());
        return url;
    }

    public static URL buildUrlWithTopRatedMovies() {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(sortByTopRated)
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO take out tag
        Log.d(TAG, url.toString());
        return url;
    }

    /**
     * Builds a URL used to communicate with MovieDB.
     * @param sortBy user specified sort movie data.
     * @return The URL used to query MovieDB.
     */
    public static URL buildUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO take the tag out.
        Log.d (TAG, url.toString());
        return url;
    }

    public static URL buildTrailerUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEOS)
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO take the tag out.
        Log.d(TAG, url.toString());
        return url;
    }

    public static URL buildReviewUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS)
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO take the tag out.
        Log.d(TAG, url.toString());
        return url;
    }

    /**
     * Returns the entire result from the HTTP response
     * @param url The URL to fetch the response from
     * @return The contents of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(FIVE_SECONDS);
            urlConnection.setReadTimeout(TEN_SECONDS);

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
