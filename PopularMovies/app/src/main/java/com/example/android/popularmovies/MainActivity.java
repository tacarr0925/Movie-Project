package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
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

import com.example.android.popularmovies.sync.MovieAsyncTaskLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieInfoAdapter.MovieInfoAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<MovieInfo>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ID_MOVIE_LOADER = 100;

    private RecyclerView mRecyclerView;
    private MovieInfoAdapter mMovieInfoAdapter;
    private ArrayList<MovieInfo> mMovieInfoList;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


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

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
        Loader<ArrayList<MovieInfo>> movieAsyncTaskLoader = loaderManager.getLoader(ID_MOVIE_LOADER);
        showMovieDataView();

        mLoadingIndicator.setVisibility(View.VISIBLE);

        if (movieAsyncTaskLoader == null) {
            loaderManager.initLoader(ID_MOVIE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(ID_MOVIE_LOADER, null, this);
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
    public Loader<ArrayList<MovieInfo>> onCreateLoader(int id, Bundle args) {

        switch(id) {
            case ID_MOVIE_LOADER:
                return new MovieAsyncTaskLoader(this);
            default:
                throw new RuntimeException("Loader Not Implement: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieInfo>> loader, ArrayList<MovieInfo> data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showMovieDataView();
            mMovieInfoList = data;
            mMovieInfoAdapter.setMovieData(mMovieInfoList);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieInfo>> loader) {
        mMovieInfoAdapter.setMovieData(null);
    }
}
