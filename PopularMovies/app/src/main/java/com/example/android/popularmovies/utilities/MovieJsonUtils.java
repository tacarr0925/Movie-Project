package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.MovieInfo;
import com.example.android.popularmovies.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Travis on 5/15/2017.
 * Utility functions to handle MovieDB JSON Data.
 */

public final class MovieJsonUtils {
    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    /**
     * This method Parses JSON from a web response and returns a ArrayList of MovieInfo
     *
     * @param context context from MainActivity
     * @param movieJsonStr JSON response from the server
     * @return ArrayList of MovieInfo giving detail of each movie.
     * @throws JSONException if JSON data cannot be  properly parsed
     */
    public static ArrayList<MovieInfo> getMovieDBStringsFromJson(Context context, String movieJsonStr)
        throws JSONException {

        ArrayList<MovieInfo> movieInfoList = new ArrayList<>();

        /*Movie list*/
        final String RESULT = "results";

        /*Original Title*/
        final String ORIGINAL_TITLE = "original_title";

        /*poster path url*/
        final String POSTER_PATH = "poster_path";

        /*Base Poster Path URL*/
        final String posterPathUrl = "http://image.tmdb.org/t/p/w185/";

        /*Plot Synopsis*/
        final String PLOT_SYNOPSIS = "overview";

        /*User Rating*/
        final String USER_RATING = "vote_average";

        /*Release Date*/
        final String RELEASE_DATE = "release_date";

        JSONObject movieDBJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieDBJson.getJSONArray(RESULT);

        for (int i = 0; i <movieArray.length(); i++) {
            String title;
            String posterPath;
            String plotSynopsis;
            String userRating;
            String releaseDate;

            JSONObject movieData = movieArray.getJSONObject(i);

            title = movieData.getString(ORIGINAL_TITLE);
            posterPath = movieData.getString(POSTER_PATH);
            plotSynopsis = movieData.getString(PLOT_SYNOPSIS);
            userRating = movieData.getString(USER_RATING);
            releaseDate = movieData.getString(RELEASE_DATE);

            //TODO update JSON to get movie id
            movieInfoList.add(new MovieInfo("335988",title, posterPathUrl + posterPath, plotSynopsis, userRating, releaseDate));
        }

        return movieInfoList;
    }

    public  static ArrayList<MovieTrailer> getTrailerStringFromJson(Context context, String trailerJsonStr)
        throws JSONException {

        /*Trailer list*/
        final String RESULT = "results";
        /*YouTube Key*/
        final String KEY = "key";
        /*Name of Trailer*/
        final String NAME = "name";

        ArrayList<MovieTrailer> trailerList = new ArrayList<>();

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONArray trailerArray = trailerJson.getJSONArray(RESULT);

        for (int i = 0; i < trailerArray.length(); i++) {
            String youTubeKey;
            String name;

            JSONObject trailerData = trailerArray.getJSONObject(i);

            youTubeKey = trailerData.getString(KEY);
            name = trailerData.getString(NAME);

            trailerList.add(new MovieTrailer(name, youTubeKey));
        }
        return trailerList;
    }
}
