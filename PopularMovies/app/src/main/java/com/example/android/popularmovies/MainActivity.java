package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieInfo;
import com.example.android.popularmovies.data.MoviePreferences;
import com.example.android.popularmovies.sync.MovieAsyncTaskLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieInfoAdapter.MovieInfoAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ID_MOVIE_LOADER = 100;
    private static final int ID_FAVORITES_LOADER = 101;

    private RecyclerView mRecyclerView;
    private MovieInfoAdapter mMovieInfoAdapter;
    private ArrayList<MovieInfo> mMovieInfoList;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    public static final String[] MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_SYNOPSIS = 1;
    public static final int INDEX_RELEASE_DATE = 2;
    public static final int INDEX_RATING = 3;
    public static final int INDEX_TITLE = 4;
    public static final int INDEX_POSTER = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        final int spanCount = getResources().getInteger(R.integer.grid_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        setupSharedPreferences();

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieInfoList")) {
            mMovieInfoList = new ArrayList<>();
        }
        else {
            mMovieInfoList = savedInstanceState.getParcelableArrayList("movieInfoList");

            if (mMovieInfoList == null || mMovieInfoList.size() == 0) {
                loadMovieData();
            }
        }

        if (MoviePreferences.isSortByFavorites(this)) {
            getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, this);
        } else {
            getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        }
        mMovieInfoAdapter = new MovieInfoAdapter(mMovieInfoList, this);
        mRecyclerView.setAdapter(mMovieInfoAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieInfoList", mMovieInfoList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(MovieInfo movieInfo) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("parseMovieInfo", movieInfo);
        intentToStartDetailActivity.putExtras(bundle);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Using the sort by Popular movies this method will get movie data in the background.
     */
    public void loadMovieData() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader;
        int loaderId;

        if (MoviePreferences.isSortByFavorites(this)) {
            loaderId = ID_FAVORITES_LOADER;
        } else {
            loaderId = ID_MOVIE_LOADER;
        }
        loader = loaderManager.getLoader(loaderId);
        showMovieDataView();

        mLoadingIndicator.setVisibility(View.VISIBLE);

        if (loader == null) {
            loaderManager.initLoader(loaderId, null, this);
        } else {
            loaderManager.restartLoader(loaderId, null, this);
        }
    }

    /**
     * This method will display the recycle view holding the movie data.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will display an error message if can't get movie data.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            loadMovieData();
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch(id) {
            case ID_MOVIE_LOADER:
                return new MovieAsyncTaskLoader(this);
            case ID_FAVORITES_LOADER:
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                return new CursorLoader(this,
                        uri,
                        MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implement: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data != null) {
            if (loader.getId() == ID_FAVORITES_LOADER)
            {
                Cursor cursor = (Cursor) data;
                MovieInfo movieInfo;
                mMovieInfoList.clear();
                cursor.moveToPosition(-1);
                try {
                    while (cursor.moveToNext()) {
                        movieInfo = new MovieInfo(cursor.getString(INDEX_MOVIE_ID),
                                cursor.getString(INDEX_TITLE),
                                cursor.getString(INDEX_POSTER),
                                cursor.getString(INDEX_SYNOPSIS),
                                cursor.getString(INDEX_RATING),
                                cursor.getString(INDEX_RELEASE_DATE)
                                );

                        mMovieInfoList.add(movieInfo);
                    }
                } finally {
                    cursor.close();
                }

            } else {
                showMovieDataView();
                mMovieInfoList = (ArrayList<MovieInfo>) data;
            }

            mMovieInfoAdapter.setMovieData(mMovieInfoList);
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMovieInfoAdapter.setMovieData(null);
    }
}
