package com.rnsit.anuj.popularmovies.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rnsit.anuj.popularmovies.Contents.ReviewContents;
import com.rnsit.anuj.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by anuj on 2/13/2016.
 */
public class ReviewAdapter extends ArrayAdapter<ReviewContents> {

    //    public static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    Context context;
    int resource;
    ArrayList<ReviewContents> list;

    public ReviewAdapter(Context context, int resource, ArrayList<ReviewContents> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reviews reviews;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            reviews = new Reviews();
            reviews.reviewAuthor = (TextView) convertView.findViewById(R.id.item_review_author);
            reviews.reviewText = (TextView) convertView.findViewById(R.id.item_review_content);
            convertView.setTag(reviews);
        } else {
            reviews = (Reviews) convertView.getTag();
        }

        ReviewContents contents = list.get(position);
        if (contents != null) {
            //Implement the getter and setter in Contents class
//            Log.e(LOG_TAG, "Values Author : " + contents.getAUTHOR());
//            Log.e(LOG_TAG, "Values Content: " + contents.getCONTENT());
            reviews.reviewText.setText(contents.getCONTENT());
            reviews.reviewAuthor.setText(contents.getAUTHOR());
        }
        return convertView;
    }

    static class Reviews {
        TextView reviewAuthor;
        TextView reviewText;

    }
}
