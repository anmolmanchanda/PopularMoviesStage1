package com.anmolmanchanda.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final Creator <Movie> CREATOR = new Creator <Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie( source );
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String mTitle;
    String mReleaseDate;
    String mPosterPath;
    String mVoteAverage;
    String mOverView;

    public Movie(String title,
                 String releaseDate,
                 String posterPath,
                 String voteAverage,
                 String overView) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mOverView = overView;
    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mReleaseDate = in.readString();
        this.mPosterPath = in.readString();
        this.mVoteAverage = in.readString();
        this.mOverView = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverView() {
        return mOverView;
    }

    public void setOverView(String overView) {
        mOverView = overView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( this.mTitle );
        dest.writeString( this.mReleaseDate );
        dest.writeString( this.mPosterPath );
        dest.writeString( this.mVoteAverage );
        dest.writeString( this.mOverView );
    }
}
