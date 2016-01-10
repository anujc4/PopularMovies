package com.rnsit.anuj.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anuj on 1/8/2016.
 */
public class PopularMoviesAdapter extends ArrayAdapter<popularMovies> {

    static private final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();


    /**
     * Constructor
     * @param context  The current context.
     * @param objects  The objects to represent in the ListView.
     */
    public PopularMoviesAdapter(Context context, List<popularMovies> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        popularMovies popmov = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movies_item_layout, parent, false);
        }

        ImageView movieIcon = (ImageView) convertView.findViewById(R.id.item_movie_image);
        movieIcon.setImageResource(popmov.movieImage);

        TextView movieTitle = (TextView) convertView.findViewById(R.id.item_movie_title);
        movieTitle.setText(popmov.movieTitle);

        return convertView;
    }
}
