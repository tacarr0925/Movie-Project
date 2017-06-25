package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.FetchJsonTask;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        FetchJsonTask.AsyncTaskCallBack {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private ImageView mImagePosterImageView;
    private TextView mPlotTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;

    private String mMovieId;

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<MovieTrailer> mTrailerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_title);

        mImagePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);

        mPlotTextView = (TextView) findViewById(R.id.tv_detail_plot);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_date);

        mUserRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            MovieInfo movieInfo = bundle.getParcelable("parseMovieInfo");

            if (movieInfo != null) {
                mTitleTextView.setText(movieInfo.originalTitle);
                mPlotTextView.setText(movieInfo.plotSynopsis);
                mReleaseDateTextView.setText(movieInfo.releaseDate);
                mUserRatingTextView.setText(movieInfo.userRating + "/10");
                mMovieId = movieInfo.movieId;

                Picasso.with(this)
                        .load(movieInfo.moviePosterImage)
                        .into(mImagePosterImageView);
            }
        }

        loadTrailerData();

        mTrailerAdapter = new TrailerAdapter(mTrailerList, this);
        mRecyclerView.setAdapter(mTrailerAdapter);
    }

    @Override
    public void onClick(String key) {
        Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + key);
        Log.d(TAG, uri.toString());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onAsyncTaskComplete(String jsonString) {
        //TODO set visibility of loading indicator
        //TODO Show error message if need be or show the view
        if (jsonString != null) {
            try {
                mTrailerList = MovieJsonUtils.getTrailerStringFromJson(this, jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mTrailerAdapter.setTrailerData(mTrailerList);
        }
    }

    public void loadTrailerData() {
        new FetchJsonTask(this).execute(NetworkUtils.buildTrailerUrl(mMovieId));
    }
}
