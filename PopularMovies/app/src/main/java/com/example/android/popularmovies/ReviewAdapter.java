package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieReview;

import java.util.List;

/**
 * Created by Travis on 6/27/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ReviewAdapter.class.getSimpleName();

    private List<MovieReview> mReviewList;

    public ReviewAdapter(List<MovieReview> reviewList) {
        mReviewList = reviewList;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ReviewAdapterViewHolder reviewHolder = (ReviewAdapterViewHolder) viewHolder;
        reviewHolder.mAuthorTextView.setText(mReviewList.get(position).author);
        reviewHolder.mReviewTextView.setText(mReviewList.get(position).review);
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) return 0;
        return mReviewList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mAuthorTextView;
        public final TextView mReviewTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

    public void setReviewData(List<MovieReview> list) {
        mReviewList = list;
        notifyDataSetChanged();
    }
}
