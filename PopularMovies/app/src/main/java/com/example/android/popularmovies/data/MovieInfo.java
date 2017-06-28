package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Travis on 5/13/2017.
 * This class is parcelable and holds the detail movie info.
 */

public class MovieInfo implements Parcelable {
    public String movieId;
    public String originalTitle;
    public String moviePosterImage;
    public String plotSynopsis;
    public String userRating;
    public String releaseDate;

    public MovieInfo (String movieId, String originalTitle, String moviePosterImage,
                      String plotSynopsis, String userRating, String releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.moviePosterImage = moviePosterImage;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private MovieInfo (Parcel in) {
        movieId = in.readString();
        originalTitle = in.readString();
        moviePosterImage = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(movieId);
        parcel.writeString(originalTitle);
        parcel.writeString(moviePosterImage);
        parcel.writeString(plotSynopsis);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
    }

    public final static Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int i) {
            return new MovieInfo[i];
        }
    };

}
