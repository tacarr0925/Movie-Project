package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
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

import com.example.android.popularmovies.data.MovieInfo;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;
import com.example.android.popularmovies.sync.ReviewAsyncTaskLoader;
import com.example.android.popularmovies.sync.TrailerAsyncTaskLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int ID_TRAILER_LOADER = 101;
    private static final int ID_REVIEW_LOADER = 102;

    private TextView mTitleTextView;
    private ImageView mImagePosterImageView;
    private TextView mPlotTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;

    private String mMovieId;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<MovieTrailer> mTrailerList;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<MovieReview> mReviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_title);

        mImagePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);

        mPlotTextView = (TextView) findViewById(R.id.tv_detail_plot);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_date);

        mUserRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);

        LinearLayoutManager ReviewLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mReviewRecyclerView.setLayoutManager(ReviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);

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
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, null, this);
        mReviewAdapter = new ReviewAdapter(mReviewList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }

    @Override
    public void onClick(String key) {
        Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + key);
        Log.d(TAG, uri.toString());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_TRAILER_LOADER:
                return new TrailerAsyncTaskLoader(this, mMovieId);
            case ID_REVIEW_LOADER:
                return new ReviewAsyncTaskLoader(this, mMovieId);
            default:
                throw new RuntimeException("Loader not Implemented " + id);
        }
    }


    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        //TODO Implement functionality commented out below
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            //showTrailerDataView();
            int id = loader.getId();
            switch (id) {
                case ID_TRAILER_LOADER:
                    mTrailerList = data;
                    mTrailerAdapter.setTrailerData(mTrailerList);
                    break;
                case ID_REVIEW_LOADER:
                    mReviewList = data;
                    mReviewAdapter.setReviewData(mReviewList);;
                    break;
                default:
                    throw new RuntimeException("Loader not Implemented " + id);
            }

        } /*else {
            showErrorMessage();
        }*/
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {
        int id = loader.getId();
        switch (id) {
            case ID_TRAILER_LOADER:
                mTrailerAdapter.setTrailerData(null);
                break;
            case ID_REVIEW_LOADER:
                mReviewAdapter.setReviewData(null);
                break;
            default:
                throw new RuntimeException("Loader not Implemented " + id);
        }
    }
}
