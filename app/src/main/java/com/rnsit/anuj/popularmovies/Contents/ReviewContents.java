package com.rnsit.anuj.popularmovies.Contents;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anuj on 2/18/2016.
 */
public class ReviewContents implements Parcelable {

    public static final Creator<ReviewContents> CREATOR = new Creator<ReviewContents>() {
        @Override
        public ReviewContents createFromParcel(Parcel in) {
            return new ReviewContents(in);
        }

        @Override
        public ReviewContents[] newArray(int size) {
            return new ReviewContents[size];
        }
    };
    String AUTHOR;
    String CONTENT;

    ReviewContents() {

    }

    public ReviewContents(JSONObject object) throws JSONException {
        this.AUTHOR = object.getString("author");
        this.CONTENT = object.getString("content");
    }

    public ReviewContents(String AUTHOR, String CONTENT) {
        this.AUTHOR = AUTHOR;
        this.CONTENT = CONTENT;
    }

    protected ReviewContents(Parcel in) {
        this.AUTHOR = in.readString();
        this.CONTENT = in.readString();
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AUTHOR);
        dest.writeString(CONTENT);
    }
}
