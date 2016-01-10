package com.rnsit.anuj.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private PopularMoviesAdapter mPopularMoviesAdapter;

    popularMovies popMovies[] = {
            new popularMovies(R.drawable.sample_0,"1"),
            new popularMovies(R.drawable.sample_1,"2"),
            new popularMovies(R.drawable.sample_2,"3"),
            new popularMovies(R.drawable.sample_3,"4"),
            new popularMovies(R.drawable.sample_4,"5")
    };

    //Default Constructor for MainActivityFragment Class
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mPopularMoviesAdapter = new PopularMoviesAdapter(
                getActivity(),
                Arrays.asList(popMovies));

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_Movies);
        gridView.setAdapter(mPopularMoviesAdapter);


        return rootView;
    }

    public class fetchMoviesTask extends AsyncTask<Void, Void, String> {
        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();

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
        protected String doInBackground(Void... params) {

            if(params.length == 0){
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String API_KEY = "de9b3e8199b5086a960146aad5ace061";


            try{
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
                Log.i(LOG_TAG,url.toString());

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
                // If the code didn't successfully get the weather data, there's no point in attemping
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
            return moviesJsonStr;
        }



        @Override
        protected void onPostExecute(String moviesJsonStr){
            super.onPostExecute(moviesJsonStr);
            final String RESULTS = "results";
            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String ORIGINAL_TITLE = "original_title";
            final String POPULARITY = "popularity";

            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONObject jsonObject1;
            try {
                jsonObject = new JSONObject(moviesJsonStr);
                jsonArray = jsonObject.getJSONArray(RESULTS);
                Log.d(LOG_TAG,"ARRAY Received is "+jsonArray);


                int length = jsonArray.length();
                Log.d(LOG_TAG,"ARRAY Length is "+length);

                for(int i=0; i<length; i++) {
                    String poster_pathStr;
                    String overviewStr;
                    String release_dateStr;
                    String original_titleStr;
                    String popularityStr;
                    jsonObject1 = jsonArray.getJSONObject(i);
                    poster_pathStr = jsonObject.getString(POSTER_PATH);
                    overviewStr = jsonObject.getString(OVERVIEW);
                    release_dateStr = jsonObject.getString(RELEASE_DATE);
                    original_titleStr = jsonObject.getString(ORIGINAL_TITLE);
                    popularityStr = jsonObject.getString(POPULARITY);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
