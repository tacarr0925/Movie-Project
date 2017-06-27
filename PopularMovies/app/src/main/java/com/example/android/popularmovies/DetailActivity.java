package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.sync.TrailerAsyncTaskLoader;
import com.example.android.popularmovies.utilities.FetchJsonTask;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int ID_TRAILER_LOADER = 101;
    private static final int ID_REVIEW_LOADER = 102;

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

        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, null, this);
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
    public Loader<ArrayList<MovieTrailer>> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_TRAILER_LOADER:
                return new TrailerAsyncTaskLoader(this, mMovieId);
            default:
                throw new RuntimeException("Loader not Implemented" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieTrailer>> loader, ArrayList<MovieTrailer> data) {
        //TODO Implement functionality commented out below
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null || !data.isEmpty()) {
            //showTrailerDataView();
            mTrailerList = data;
            mTrailerAdapter.setTrailerData(mTrailerList);
        } /*else {
            showErrorMessage();
        }*/
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieTrailer>> loader) {
        mTrailerAdapter.setTrailerData(null);
    }
}
