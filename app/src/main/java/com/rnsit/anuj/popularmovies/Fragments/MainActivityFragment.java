package com.rnsit.anuj.popularmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rnsit.anuj.popularmovies.Activities.SettingsActivity;
import com.rnsit.anuj.popularmovies.Adapter.MovieAdapter;
import com.rnsit.anuj.popularmovies.BuildConfig;
import com.rnsit.anuj.popularmovies.Contents.MovieContents;
import com.rnsit.anuj.popularmovies.R;
import com.rnsit.anuj.popularmovies.data.MovieColumns;
import com.rnsit.anuj.popularmovies.data.MovieProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {


    public MovieAdapter mPopularMovieAdapter;
    public MovieContents[] movieContentsArray;
    private int mPosition = GridView.INVALID_POSITION;
    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        //        Log.d("onStart", "ON START EXECUTED");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Order = sharedPreferences
                .getString(getString(R.string.sorting_order_key),
                        getString(R.string.sorting_order_default_value));
        updateMovies(Order);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray("stored_array", movieContentsArray);
    }

    private void updateMovies(String Order) {
//        Log.d("UPDATE", "UPDATE MOVIES EXECUTED");
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_ORDER_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";
        switch (Order) {
            case "popularity.desc":
                Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_ORDER_PARAM, Order)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY_PARAM)
                        .build();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        uri.toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    mPopularMovieAdapter.clear();
                                    JSONArray RESULTS = response.getJSONArray("results");
                                    movieContentsArray = new MovieContents[RESULTS.length()];
                                    for (int i = 0; i < RESULTS.length(); i++) {
                                        JSONObject temp = RESULTS.getJSONObject(i);
                                        MovieContents TEMP_OBJ = new MovieContents(temp);
                                        movieContentsArray[i] = TEMP_OBJ;
                                        mPopularMovieAdapter.add(TEMP_OBJ);
                                    }

                                } catch (JSONException e) {
                                    Log.e(LOG_TAG, e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Some Error Occoured While Fetching Popular Movies", Toast.LENGTH_SHORT).show();
                            }
                        });
                Volley.newRequestQueue(getContext()).add(request);
                break;
            case "vote_average.desc":
                Uri uri1 = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_ORDER_PARAM, Order)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY_PARAM)
                        .build();
                JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                        uri1.toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    mPopularMovieAdapter.clear();
                                    JSONArray RESULTS = response.getJSONArray("results");
                                    movieContentsArray = new MovieContents[RESULTS.length()];
                                    for (int i = 0; i < RESULTS.length(); i++) {
                                        JSONObject temp = RESULTS.getJSONObject(i);

                                        MovieContents TEMP_OBJ = new MovieContents(temp);
                                        movieContentsArray[i] = TEMP_OBJ;
                                        mPopularMovieAdapter.add(TEMP_OBJ);
                                    }

                                } catch (JSONException e) {
                                    Log.e(LOG_TAG, e.getMessage().toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Some Error Occoured While Fetching Average Votes Movies", Toast.LENGTH_SHORT).show();
                            }
                        });
                Volley.newRequestQueue(getContext()).add(request1);
                break;
            default:
                fetchFavourites();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            movieContentsArray = (MovieContents[]) savedInstanceState.getParcelableArray("stored_array");
//            Log.d
// (LOG_TAG,movieContentsArray.toString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteDatabase();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.sorting_order_key), getString(R.string.sorting_order_default_value))
                        .commit();
                String Order = sharedPreferences
                        .getString(getString(R.string.sorting_order_key),
                                getString(R.string.sorting_order_default_value));
                updateMovies(Order);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDatabase() {
        Context c = getContext();
        c.getContentResolver().delete(MovieProvider.Movie.MOVIE, null, null);
        c.getContentResolver().delete(MovieProvider.Review.REVIEWS, null, null);
        c.getContentResolver().delete(MovieProvider.Video.VIDEO, null, null);
        Toast.makeText(c, "Cleared Favourites", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.sorting_order_key), getString(R.string.sorting_order_default_value))
                .commit();
        String Order = sharedPreferences
                .getString(getString(R.string.sorting_order_key),
                        getString(R.string.sorting_order_default_value));
        updateMovies(Order);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_Movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MovieContents TEMPmovieContents = createMovieContents(movieContentsArray[position]);
//                startActivity(new Intent(getActivity(), MovieDetails.class)
//                        .putExtra("MOVIE_DATA", TEMPmovieContents));
//                mPopularMovieAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                MovieContents TEMPmovieContents = createMovieContents(movieContentsArray[position]);
                bundle.putParcelable("MOVIE_DATA", TEMPmovieContents);
                ((Callback) getActivity()).onItemSelected(bundle);
                mPosition = position;
            }
        });
        mPopularMovieAdapter = new MovieAdapter(getActivity(), R.layout.movies_item_layout, new ArrayList<MovieContents>());
        gridView.setAdapter(mPopularMovieAdapter);
        return rootView;
    }

    private MovieContents createMovieContents(MovieContents mc) {
        MovieContents TEMP_OBJECT = new MovieContents();
        TEMP_OBJECT.setID(mc.getID());
        TEMP_OBJECT.setPOSTER_PATH(mc.getPOSTER_PATH());
        TEMP_OBJECT.setOVERVIEW(mc.getOVERVIEW());
        TEMP_OBJECT.setRELEASE_DATE(mc.getRELEASE_DATE());
        TEMP_OBJECT.setTITLE(mc.getTITLE());
        TEMP_OBJECT.setAVERAGE_VOTE(mc.getAVERAGE_VOTE());
        return TEMP_OBJECT;
    }

    void fetchFavourites() {
        Context c = getContext();
        Cursor cursor = c.getContentResolver().query(MovieProvider.Movie.MOVIE,
                null,
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {
            Toast.makeText(getContext(), "No Movies Set As Favourite Yet.", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.sorting_order_key), getString(R.string.sorting_order_default_value))
                    .commit();
            String Order = sharedPreferences
                    .getString(getString(R.string.sorting_order_key),
                            getString(R.string.sorting_order_default_value));
            updateMovies(Order);
        } else {
            mPopularMovieAdapter.clear();
            int i = 0;
            movieContentsArray = new MovieContents[cursor.getCount()];
            do {
                int ID = cursor.getInt(cursor.getColumnIndex(MovieColumns._ID));
                String OVERVIEW = cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW));
                String RELEASE_DATE = cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE));
                String POSTER_PATH = cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH));
                String TITLE = cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE));
                double VOTE_AVERAGE = cursor.getDouble(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE));
                MovieContents contents = new MovieContents(ID, OVERVIEW, RELEASE_DATE, POSTER_PATH, TITLE, VOTE_AVERAGE);
                movieContentsArray[i++] = contents;
                mPopularMovieAdapter.add(contents);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    public interface Callback {
        void onItemSelected(Bundle bundle);
    }
}