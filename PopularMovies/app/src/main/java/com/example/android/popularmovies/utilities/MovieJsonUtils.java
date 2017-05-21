package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.example.android.popularmovies.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Travis on 5/15/2017.
 */

public final class MovieJsonUtils {
    private static final String TAG = MovieJsonUtils.class.getSimpleName();

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

            movieInfoList.add(new MovieInfo(title, posterPathUrl + posterPath, plotSynopsis, userRating, releaseDate));
        }

        return movieInfoList;
    }
}
