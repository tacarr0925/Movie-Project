package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    private MovieInfoAdapter mMovieInfoAdapter;
    private List<MovieInfo> mMovieInfoList;

    private static String[] testString = new String[] {
            "Movie1",
            "Movie2",
            "Movie3",
            "Movie4",
            "Movie5",
            "Movie6",
            "Movie7",
            "Movie8",
            "Movie9",
            "Movie10",
            "Movie11"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieInfoList = new ArrayList<>();

        for (String test : testString) {
            mMovieInfoList.add(new MovieInfo(test));
            Log.d(TAG, test);
        }

        mMovieInfoAdapter = new MovieInfoAdapter(mMovieInfoList);
        mRecyclerView.setAdapter(mMovieInfoAdapter);
    }
}
