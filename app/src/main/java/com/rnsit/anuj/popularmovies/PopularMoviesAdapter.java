package com.rnsit.anuj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anuj on 1/8/2016.
 */
public class PopularMoviesAdapter extends ArrayAdapter<MovieContents> {

    static private final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();
    Context context;
    int LayoutID;
    ArrayList<MovieContents> arrayList;
    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the GridView.
     */
    public PopularMoviesAdapter(Context context, int resource, ArrayList<MovieContents> objects) {
        super(context, resource, objects);
        this.context = context;
        this.LayoutID = resource;
        this.arrayList = objects;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoviePoster moviePoster;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(LayoutID, parent, false);
            moviePoster = new MoviePoster();
            moviePoster.imageView = (ImageView) convertView.findViewById(R.id.item_movie_image);
            convertView.setTag(moviePoster);
        } else {
            moviePoster = (MoviePoster) convertView.getTag();
        }
        MovieContents movieContents = arrayList.get(position);

//        Log.d(LOG_TAG,movieContents.POSTER_PATH);
        Picasso.with(context)
                .load(movieContents.POSTER_PATH)
                .into(moviePoster.imageView);

        return convertView;
    }

    private class MoviePoster {
        ImageView imageView;
    }
}
