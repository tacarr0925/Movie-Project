package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";


    final static String PARM_API_KEY = "api_key";
    final static String apiKey = "{ADD YOUR API KEY}";
    //final static String apiKey = "";

    final static String sortByPopMovies = "popular";
    final static String sortByTopRated = "top_rated";
    final static String VIDEOS = "videos";

    public static URL getMovieUrl(Context context) {
        //TODO build based on preferences
        return buildUrlWithPopularMovies();
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
    /**
     * Builds a URL used to communicate with MovieDB.
     * @param sortBy user specified sort movie data.
     * @return The URL used to query MovieDB.
     */
    public static URL buildUrl(String sortBy) {
        //TODO Update to use preferences.  Have new method to call buildUrl based on preferences.
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

    /**
     * Returns the entire result from the HTTP response
     * @param url The URL to fetch the response from
     * @return The contents of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
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

    /**
     *Getter for Sort by string for Popular Movies
     * @return string of sort by Popular Movies
     */
    public static String get_sortByPopMovies() {
        return sortByPopMovies;
    }

    /**
     *Getter for Sort by string for Top Rated Movies
     * @return string of sort by Top Rated Movies
     */
    public static String get_sortByTopRatedMovies() {
        return sortByTopRated;
    }
}
