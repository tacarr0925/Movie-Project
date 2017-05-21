package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieInfoAdapter.MovieInfoAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    private MovieInfoAdapter mMovieInfoAdapter;
    private ArrayList<MovieInfo> mMovieInfoList;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private Spinner mSortSpinner;

    private boolean mHaveParceable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieInfoList")) {
            mMovieInfoList = new ArrayList<>();
            mHaveParceable = false;
        }
        else {
            mMovieInfoList = savedInstanceState.getParcelableArrayList("movieInfoList");
            mHaveParceable = true;
        }

        mMovieInfoAdapter = new MovieInfoAdapter(mMovieInfoList, this);
        mRecyclerView.setAdapter(mMovieInfoAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieInfoList", mMovieInfoList);
        super.onSaveInstanceState(outState);
    }

//TODO add rotation saveOnInstance; See udacity webcast
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

        MenuItem item = menu.findItem(R.id.action_spinner);
        mSortSpinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSortSpinner.setAdapter(adapter);

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*We already have the Movie Data from onSaveInstance so we don't want to call*/
                if (mHaveParceable) {
                    mHaveParceable = false;
                    return;
                }

                switch (position) {
                    case 0:
                        loadMostPopularMovieData();
                        break;
                    case 1:
                        loadTopRateMovieData();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    public void loadMostPopularMovieData() {
        showMovieDataView();
        new FetchMovieTask().execute(NetworkUtils.get_sortByPopMovies());
    }

    public void loadTopRateMovieData() {
        showMovieDataView();
        new FetchMovieTask().execute(NetworkUtils.get_sortByTopRatedMovies());
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieDataList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDataList != null) {
                showMovieDataView();
                mMovieInfoList = movieDataList;
                mMovieInfoAdapter.setMovieData(mMovieInfoList);
            }
            else {
                showErrorMessage();
            }
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {

            String sortBy;
            if (params.length == 0) {
                sortBy = NetworkUtils.get_sortByPopMovies();
            }
            else {
                sortBy = params[0];
            }

            URL movieDBRequestUrl = NetworkUtils.buildUrl(sortBy);

            try {
                String jsonMovieDBResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieDBRequestUrl);

                ArrayList<MovieInfo> jsonMovieInfoList = MovieJsonUtils
                        .getMovieDBStringsFromJson(MainActivity.this, jsonMovieDBResponse);

                return jsonMovieInfoList;

            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }
        }
    }
}
