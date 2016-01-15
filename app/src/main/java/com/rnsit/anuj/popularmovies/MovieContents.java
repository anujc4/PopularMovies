package com.rnsit.anuj.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anuj on 1/10/2016.
 */
public class MovieContents implements Parcelable {
    String LOG_TAG = MovieContents.class.getSimpleName();
    String POSTER_PATH;
    String OVERVIEW;
    String RELEASE_DATE;
    String ORIGINAL_TITLE;
    String AVERAGE_VOTE;


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

    public String getORIGINAL_TITLE() {
        return ORIGINAL_TITLE;
    }

    public void setORIGINAL_TITLE(String ORIGINAL_TITLE) {
        this.ORIGINAL_TITLE = ORIGINAL_TITLE;
    }

    public String getAVERAGE_VOTE() {
        return AVERAGE_VOTE;
    }

    public void setAVERAGE_VOTE(String AVERAGE_VOTE) {
        this.AVERAGE_VOTE = AVERAGE_VOTE;
    }

    protected MovieContents(Parcel in) {
        this.POSTER_PATH = in.readString();
        this.OVERVIEW = in.readString();
        this.RELEASE_DATE = in.readString();
        this.ORIGINAL_TITLE = in.readString();
        this.AVERAGE_VOTE = in.readString();
    }

    protected MovieContents(JSONObject jsonObject) throws JSONException {
        this.ORIGINAL_TITLE = jsonObject.getString("original_title");
        if (!jsonObject.getString("poster_path").equals("null")) {
            this.POSTER_PATH = "http://image.tmdb.org/t/p/w342" +
                    jsonObject.getString("poster_path");
        }
        this.OVERVIEW = jsonObject.getString("overview");
        this.AVERAGE_VOTE = jsonObject.getString("vote_average");
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


    public MovieContents() {
        //Default Constructor
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(POSTER_PATH);
        dest.writeString(OVERVIEW);
        dest.writeString(RELEASE_DATE);
        dest.writeString(ORIGINAL_TITLE);
        dest.writeString(AVERAGE_VOTE);
    }
}
