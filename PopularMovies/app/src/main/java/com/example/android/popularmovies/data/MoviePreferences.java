package com.example.android.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.android.popularmovies.R;

/**
 * Created by Travis on 7/1/2017.
 */

public class MoviePreferences {

    public static boolean isSortByFavorites(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String preferredSortBy;

        preferredSortBy = sharedPreferences.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default_value));

        if (preferredSortBy.equals(context.getString(R.string.pref_favorite_value)))
            return true;
        else
            return false;
    }
}
