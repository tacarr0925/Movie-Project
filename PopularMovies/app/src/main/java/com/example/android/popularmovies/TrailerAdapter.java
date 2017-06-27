package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Travis on 6/18/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static String TAG = TrailerAdapter.class.getSimpleName();

    private List<MovieTrailer> mTrailerList;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String key);
    }

    public TrailerAdapter(List<MovieTrailer> trailerList, TrailerAdapterOnClickHandler clickHandler) {
        mTrailerList = trailerList;
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        holder.mTrailerNameTextView.setText(mTrailerList.get(position).trailerName);
    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null) return 0;
        return mTrailerList.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerNameTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerNameTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
            //TODO update onClick to the Image button and not the View
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mTrailerList.get(adapterPosition).youTubeKey);
        }
    }

    public void setTrailerData(List<MovieTrailer> list) {
        mTrailerList = list;
        notifyDataSetChanged();
    }
}
