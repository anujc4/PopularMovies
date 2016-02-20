package com.rnsit.anuj.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.rnsit.anuj.popularmovies.Adapter.MovieAdapter;
import com.rnsit.anuj.popularmovies.data.MovieColumns;
import com.rnsit.anuj.popularmovies.data.MovieProvider;
import com.rnsit.anuj.popularmovies.Contents.MovieContents;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    MovieAdapter mPopularMovieAdapter;
    MovieContents[] movieContentses;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        fetchMoviesTask fetchMoviesTaskOBJ = new fetchMoviesTask(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Order = sharedPreferences
                .getString(getString(R.string.sorting_order_key),
                        getString(R.string.sorting_order_default_value));
        fetchMoviesTaskOBJ.execute(Order);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fetchFavourites();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_Movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieContents TEMPmovieContents = createMovieContents(movieContentses[position]);
                startActivity(new Intent(getActivity(), MovieDetails.class)
                        .putExtra("MOVIE_DATA", TEMPmovieContents));
                mPopularMovieAdapter.notifyDataSetChanged();
            }
        });
        mPopularMovieAdapter = new MovieAdapter(getActivity(), R.layout.movies_item_layout, new ArrayList<MovieContents>());
        gridView.setAdapter(mPopularMovieAdapter);
        return rootView;
    }

    private MovieContents createMovieContents(MovieContents movieContentse) {
        MovieContents TEMP_OBJECT = new MovieContents();
        TEMP_OBJECT.setID(movieContentse.getID());
        TEMP_OBJECT.setPOSTER_PATH(movieContentse.getPOSTER_PATH());
        TEMP_OBJECT.setOVERVIEW(movieContentse.getOVERVIEW());
        TEMP_OBJECT.setRELEASE_DATE(movieContentse.getRELEASE_DATE());
        TEMP_OBJECT.setTITLE(movieContentse.getTITLE());
        TEMP_OBJECT.setAVERAGE_VOTE(movieContentse.getAVERAGE_VOTE());
        return TEMP_OBJECT;
    }

    void fetchFavourites() {
        Cursor cursor = getFavouritesCurser();
        if (!cursor.moveToFirst()) {
            Toast.makeText(getContext(), "No Movies Set As Favourite Yet.", Toast.LENGTH_SHORT);
            updateMovies();
        }else {
            mPopularMovieAdapter.clear();
            do {
                int ID = cursor.getInt(cursor.getColumnIndex(MovieColumns._ID));
                String OVERVIEW = cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW));
                String RELEASE_DATE = cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE));
                String POSTER_PATH = cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH));
                String TITLE = cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE));
                double VOTE_AVERAGE = cursor.getDouble(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE));

                MovieContents contents = new MovieContents(ID,OVERVIEW,RELEASE_DATE,POSTER_PATH,TITLE,VOTE_AVERAGE);
                mPopularMovieAdapter.add(contents);
            }while (cursor.moveToNext());
        }

    }
    private Cursor getFavouritesCurser() {
        return getActivity().getContentResolver().query(MovieProvider.Movie.MOVIE, null, null, null, null);
    }

}