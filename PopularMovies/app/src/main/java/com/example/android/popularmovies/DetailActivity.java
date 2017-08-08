package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieInfo;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;
import com.example.android.popularmovies.sync.ReviewAsyncTaskLoader;
import com.example.android.popularmovies.sync.TrailerAsyncTaskLoader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String KEY_TRAILER_STATE = "trailer_state";
    private static final String KEY_REVIEW_STATE = "review_state";

    private static final int ID_TRAILER_LOADER = 101;
    private static final int ID_REVIEW_LOADER = 102;
    private static final int ID_CHECKBOX_LOADER = 103;

    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private TextView mTitleTextView;
    private ImageView mImagePosterImageView;
    private TextView mPlotTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;

    private LinearLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;

    private Parcelable mTrailerListState;
    private Parcelable mReviewListState;

    private CheckBox mFavoriteCheckBox;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<MovieTrailer> mTrailerList;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<MovieReview> mReviewList;

    private MovieInfo mMovieInfo;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_title);

        mImagePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);

        mPlotTextView = (TextView) findViewById(R.id.tv_detail_plot);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_date);

        mUserRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        mFavoriteCheckBox = (CheckBox) findViewById(R.id.cb_favorites);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);

        mTrailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);

        mReviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Bundle bundle = intentThatStartedThisActivity.getExtras();

            if (bundle != null) {
                MovieInfo movieInfo = bundle.getParcelable("parseMovieInfo");
                SimpleDateFormat unformatted = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatted = new SimpleDateFormat("yyyy");

                if (movieInfo != null) {
                    mMovieInfo = movieInfo;
                    mTitleTextView.setText(movieInfo.originalTitle);
                    mPlotTextView.setText(movieInfo.plotSynopsis);

                    try {
                        Date date = unformatted.parse(movieInfo.releaseDate);
                        mReleaseDateTextView.setText(formatted.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mUserRatingTextView.setText(movieInfo.userRating + "/10");

                    Picasso.with(this)
                            .load(movieInfo.moviePosterImage)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(mImagePosterImageView);
                }
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_TRAILER_STATE)) {
                mTrailerListState = savedInstanceState.getParcelable(KEY_TRAILER_STATE);
            }

            if (savedInstanceState.containsKey(KEY_REVIEW_STATE)) {
                mTrailerListState = savedInstanceState.getParcelable(KEY_REVIEW_STATE);
            }
        }

        mUri = MovieContract.MovieEntry.builderMovieUriWithMovieId(mMovieInfo.movieId);
        getSupportLoaderManager().initLoader(ID_CHECKBOX_LOADER, null, this);

        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, null, this);
        mTrailerAdapter = new TrailerAdapter(mTrailerList, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, null, this);
        mReviewAdapter = new ReviewAdapter(mReviewList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mTrailerListState = mTrailerLayoutManager.onSaveInstanceState();
        mReviewListState = mReviewLayoutManager.onSaveInstanceState();

        outState.putParcelable(KEY_TRAILER_STATE, mTrailerListState);
        outState.putParcelable(KEY_REVIEW_STATE, mReviewListState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTrailerListState != null) {
            mTrailerLayoutManager.onRestoreInstanceState(mTrailerListState);
        }

        if (mReviewListState != null) {

            mReviewLayoutManager.onRestoreInstanceState(mReviewListState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        if (mTrailerList == null || mTrailerList.isEmpty()) {
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Intent shareIntent = createShareIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(YOUTUBE_URL + mTrailerList.get(0).youTubeKey)
                .getIntent();

        return shareIntent;
    }

    @Override
    public void onClick(String key) {
        Uri uri = Uri.parse(YOUTUBE_URL + key);
        Log.d(TAG, uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onCheckboxClicked(View view) {
        Uri uri;
        ContentValues contentValues = new ContentValues();
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.cb_favorites:
                if (checked) {
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                            mMovieInfo.movieId);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RATING,
                            mMovieInfo.userRating);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                            mMovieInfo.releaseDate);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS,
                            mMovieInfo.plotSynopsis);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,
                            mMovieInfo.originalTitle);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER,
                            mMovieInfo.moviePosterImage);

                    uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                }
                else {
                    uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(mMovieInfo.movieId).build();

                    getContentResolver().delete(uri, null, null);
                }
                break;
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_TRAILER_LOADER:
                return new TrailerAsyncTaskLoader(this, mMovieInfo.movieId);
            case ID_REVIEW_LOADER:
                return new ReviewAsyncTaskLoader(this, mMovieInfo.movieId);
            case ID_CHECKBOX_LOADER:
                return new CursorLoader(this,
                        mUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not Implemented " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data != null) {
            int id = loader.getId();
            switch (id) {
                case ID_TRAILER_LOADER:
                    mTrailerList = (ArrayList<MovieTrailer>) data;
                    mTrailerAdapter.setTrailerData(mTrailerList);
                    invalidateOptionsMenu();
                    break;
                case ID_REVIEW_LOADER:
                    mReviewList = (ArrayList<MovieReview>) data;
                    mReviewAdapter.setReviewData(mReviewList);
                    ;
                    break;
                case ID_CHECKBOX_LOADER:
                    Cursor cursor = (Cursor) data;
                    if (cursor != null && cursor.moveToFirst()) {
                        mFavoriteCheckBox.setChecked(true);
                    }
                    break;
                default:
                    throw new RuntimeException("Loader not Implemented " + id);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();
        switch (id) {
            case ID_TRAILER_LOADER:
                mTrailerAdapter.setTrailerData(null);
                break;
            case ID_REVIEW_LOADER:
                mReviewAdapter.setReviewData(null);
                break;
            case ID_CHECKBOX_LOADER:
                break;
            default:
                throw new RuntimeException("Loader not Implemented " + id);
        }
    }
}
