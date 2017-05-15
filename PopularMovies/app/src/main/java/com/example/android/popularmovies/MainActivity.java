package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    private List<MovieInfo> mMovieInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieInfoList = new ArrayList<>();

        mMovieInfoAdapter = new MovieInfoAdapter(mMovieInfoList, this);
        mRecyclerView.setAdapter(mMovieInfoAdapter);

        loadMovieData();
    }

    public void loadMovieData() {
        String test = "test";
        new FetchMovieTask().execute(test);
    }

    @Override
    public void onClick(MovieInfo movieInfo) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieInfo.moviePosterImage);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            if (movieData != null) {
                mMovieInfoAdapter.setMovieData(movieData);
            }
            else {
                // TODO create error message
            }
        }

        @Override
        protected String[] doInBackground(String... params) {

            URL movieDBRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonMovieDBResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieDBRequestUrl);

                String[] jsonMovieData = MovieJsonUtils
                        .getMovieDBStringsFromJson(MainActivity.this, jsonMovieDBResponse);

                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }
        }
    }
}
