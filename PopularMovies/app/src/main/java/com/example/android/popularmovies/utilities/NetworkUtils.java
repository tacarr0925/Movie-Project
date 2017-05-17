package com.example.android.popularmovies.utilities;

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
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    final static String PARM_API_KEY = "api_key";
    final static String apiKey = "{ADD YOUR API KEY}";
    //final static String apiKey = "";

    final static String PARM_SORT = "sort_by";
    final static String sortByPopMovies = "popularity.desc";

    final static String PARM_ADULT = "include_adult";
    final static String adult = "false";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter(PARM_API_KEY, apiKey)
                .appendQueryParameter(PARM_SORT, sortByPopMovies)
                .appendQueryParameter(PARM_ADULT, adult)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d (TAG, url.toString());
        return url;
    }

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
}
