package com.rnsit.anuj.popularmovies.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.rnsit.anuj.popularmovies.Contents.MovieContents;
import com.rnsit.anuj.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anuj on 1/8/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieContents> {

    static private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    Context mcontext;
    int LayoutID;
    ArrayList<MovieContents> arrayList;

    public MovieAdapter(Context context, int resource, ArrayList<MovieContents> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.LayoutID = resource;
        this.arrayList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoviePoster moviePoster;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mcontext).getLayoutInflater();
            convertView = inflater.inflate(LayoutID, parent, false);
            moviePoster = new MoviePoster();
            moviePoster.imageView = (ImageView) convertView.findViewById(R.id.item_movie_image);
            convertView.setTag(moviePoster);
        } else {
            moviePoster = (MoviePoster) convertView.getTag();
        }
        MovieContents movieContents = arrayList.get(position);

//        Log.d(LOG_TAG,movieContents.POSTER_PATH);
        Picasso.with(mcontext)
                .load(movieContents.POSTER_PATH)
                .placeholder(R.drawable.moviepic)
                .error(R.drawable.moviepic)
                .into(moviePoster.imageView);

        return convertView;
    }

    private class MoviePoster {
        ImageView imageView;
    }
}
