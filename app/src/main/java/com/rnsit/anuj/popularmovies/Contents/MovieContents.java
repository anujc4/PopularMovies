package com.rnsit.anuj.popularmovies.Contents;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anuj on 1/10/2016.
 */
public class MovieContents implements Parcelable {
    public static final Creator<MovieContents> CREATOR = new Creator<MovieContents>() {
        @Override
        public MovieContents createFromParcel(Parcel in) {
            return new MovieContents(in);
        }

        @Override
        public MovieContents[] newArray(int size) {
            return new MovieContents[size];
        }
    };
    public long ID;
    public String POSTER_PATH;
    public String OVERVIEW;
    public String RELEASE_DATE;
    public String TITLE;
    public double AVERAGE_VOTE;

    public MovieContents(int id, String overview, String release_date, String poster_path, String title, double vote_average) {
        this.ID = id;
        this.OVERVIEW = overview;
        this.RELEASE_DATE = release_date;
        this.POSTER_PATH = poster_path;
        this.TITLE = title;
        this.AVERAGE_VOTE = vote_average;
    }

    protected MovieContents(Parcel in) {
        this.ID = in.readLong();
        this.POSTER_PATH = in.readString();
        this.OVERVIEW = in.readString();
        this.RELEASE_DATE = in.readString();
        this.TITLE = in.readString();
        this.AVERAGE_VOTE = in.readDouble();
    }

    public MovieContents(JSONObject jsonObject) throws JSONException {
        this.TITLE = jsonObject.getString("original_title");
        if (!jsonObject.getString("poster_path").equals("null")) {
            this.POSTER_PATH = "http://image.tmdb.org/t/p/w342" +
                    jsonObject.getString("poster_path");
        }
        this.ID = jsonObject.getLong("id");
        this.OVERVIEW = jsonObject.getString("overview");
        this.AVERAGE_VOTE = jsonObject.getDouble("vote_average");
        this.RELEASE_DATE = jsonObject.getString("release_date");

        //DEBUG INFO
        /*
        Log.d(LOG_TAG,
                "Movie Info\n" +
                        ORIGINAL_TITLE + "\n" +
                        POSTER_PATH + "\n" +
                        OVERVIEW + "\n" +
                        AVERAGE_VOTE + "\n" +
                        RELEASE_DATE
        );
        */
    }

    public MovieContents() {
        //Default Constructor
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public void setPOSTER_PATH(String POSTER_PATH) {
        this.POSTER_PATH = POSTER_PATH;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(String OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(String RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String ORIGINAL_TITLE) {
        this.TITLE = ORIGINAL_TITLE;
    }

    public double getAVERAGE_VOTE() {
        return AVERAGE_VOTE;
    }

    public void setAVERAGE_VOTE(double AVERAGE_VOTE) {
        this.AVERAGE_VOTE = AVERAGE_VOTE;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(POSTER_PATH);
        dest.writeString(OVERVIEW);
        dest.writeString(RELEASE_DATE);
        dest.writeString(TITLE);
        dest.writeDouble(AVERAGE_VOTE);
    }
}
