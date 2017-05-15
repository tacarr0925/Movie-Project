package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Travis on 5/15/2017.
 */

public final class MovieJsonUtils {
    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    public static String[] getMovieDBStringsFromJson(Context context, String movieJsonStr)
        throws JSONException {

        String[] parsedPosterPaths = null;

        final String RESULT = "results";

        final String POSTER_PATH = "poster_path";

        /*Poster Path URL*/
        final String posterPathUrl = "http://image.tmdb.org/t/p/w185/";

        JSONObject movieDBJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieDBJson.getJSONArray(RESULT);

        parsedPosterPaths = new String[movieArray.length()];

        for (int i = 0; i <movieArray.length(); i++) {
            String posterPath;

            JSONObject movieData = movieArray.getJSONObject(i);
            posterPath = movieData.getString(POSTER_PATH);

            parsedPosterPaths[i] = posterPathUrl + posterPath;
            Log.d(TAG, parsedPosterPaths[i]);
        }

        return parsedPosterPaths;
    }
}
