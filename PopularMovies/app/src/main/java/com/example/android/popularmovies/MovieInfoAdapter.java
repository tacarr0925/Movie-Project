package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Travis on 5/13/2017.
 */

public class MovieInfoAdapter extends RecyclerView.Adapter<MovieInfoAdapter.MovieInfoAdapterViewHolder> {

    private static final String TAG = MovieInfoAdapter.class.getSimpleName();

    private List<MovieInfo> mMovieInfoList;

    public MovieInfoAdapter(List<MovieInfo> movieInfoList) {
        mMovieInfoList = movieInfoList;
    }

    public class MovieInfoAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovierPosterImageView;

        public MovieInfoAdapterViewHolder(View view) {
            super(view);
            mMovierPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        }
    }

    @Override
    public MovieInfoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, shouldAttachToParentImmediately);
        return new MovieInfoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieInfoAdapterViewHolder movieInfoAdapterViewHolder, int position) {
        String moviePosterString = mMovieInfoList.get(position).moviePosterImage;
        Picasso.with(movieInfoAdapterViewHolder.mMovierPosterImageView.getContext())
                .load(moviePosterString)
                .into(movieInfoAdapterViewHolder.mMovierPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieInfoList == null) return 0;
        return mMovieInfoList.size();
    }

    public void setMovieData(String[] movieData) {
        mMovieInfoList.clear();

        for (String movie : movieData) {
            mMovieInfoList.add(new MovieInfo(movie));
        }
        notifyDataSetChanged();
    }
}
