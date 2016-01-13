package com.rnsit.anuj.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    PopularMoviesAdapter mPopularMoviesAdapter;

    //Default Constructor for MainActivityFragment Class
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fetchMoviesTask fetchMoviesTaskOBJ = new fetchMoviesTask();
        fetchMoviesTaskOBJ.execute();
    }

    @Override
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_Movies);
        //gridView.setAdapter(mPopularMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), MovieDetails.class).putExtra("ClickedMovieItem", position));
            }
        });
        mPopularMoviesAdapter = new PopularMoviesAdapter(getActivity(), R.layout.movies_item_layout, new ArrayList<MovieContents>());
        fetchMoviesTask fetchMoviesTaskOBJ = new fetchMoviesTask();
        fetchMoviesTaskOBJ.execute();
        gridView.setAdapter(mPopularMoviesAdapter);
        return rootView;
    }

    public class fetchMoviesTask extends AsyncTask<Void, Void, MovieContents[]> {
        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();
        MovieContents[] movieContentses;


        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected MovieContents[] doInBackground(Void... params) {

            Log.d(LOG_TAG, "INVOKED doInBackground");
           /* if(params.length == 0){
                return null;
            }
            */
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String API_KEY = "de9b3e8199b5086a960146aad5ace061";


            try{
                Log.d(LOG_TAG, "Building URL");
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String sortPopularity = "sort_by=popularity.desc";
                final String sortHighestRated = "sort_by=vote_average.desc";
                final String SORT_ORDER_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                /**
                 * Make the url here
                 * http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[MY API KEY]
                 */
                Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_ORDER_PARAM,sortPopularity)
                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                Log.d(LOG_TAG, url.toString());

                // Create the request to TheMovieDB.org, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));


                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr =buffer.toString();
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;

            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                String RESULTS = "results";
                ArrayList<MovieContents> movieContentsArrayList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(moviesJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    MovieContents TEMPmovieContents = new MovieContents(jsonObject1);
                    movieContentsArrayList.add(TEMPmovieContents);
                }
                MovieContents[] ReturnedmovieContents = new MovieContents[movieContentsArrayList.size()];
                movieContentses = (MovieContents[]) movieContentsArrayList.toArray(ReturnedmovieContents);
                Log.d(LOG_TAG, String.valueOf(movieContentses));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieContentses;
        }


        @Override
        protected void onPostExecute(MovieContents[] movieContentses) {
            //super.onPostExecute(movieContentses);
            //mPopularMoviesAdapter.clear();
            for (MovieContents movieContentsTempLoop : movieContentses)
                mPopularMoviesAdapter.add(movieContentsTempLoop);
        }
    }

}
