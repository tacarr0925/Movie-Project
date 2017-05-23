package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private ImageView mImagePosterImageView;
    private TextView mPlotTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_title);

        mImagePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);

        mPlotTextView = (TextView) findViewById(R.id.tv_detail_plot);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_date);

        mUserRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            MovieInfo movieInfo = bundle.getParcelable("parseMovieInfo");

            if (movieInfo != null) {
                mTitleTextView.setText(movieInfo.originalTitle);
                mPlotTextView.setText(movieInfo.plotSynopsis);
                mReleaseDateTextView.setText(movieInfo.releaseDate);
                mUserRatingTextView.setText(movieInfo.userRating + "/10");

               Picasso.with(this)
                        .load(movieInfo.moviePosterImage)
                        .into(mImagePosterImageView);
            }
        }


    }
}
