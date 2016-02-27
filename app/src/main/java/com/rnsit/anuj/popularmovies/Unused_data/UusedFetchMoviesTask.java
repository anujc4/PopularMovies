package com.rnsit.anuj.popularmovies.Unused_data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.rnsit.anuj.popularmovies.BuildConfig;
import com.rnsit.anuj.popularmovies.Contents.MovieContents;
import com.rnsit.anuj.popularmovies.Fragments.MainActivityFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anuj on 2/18/2016.
 * Used Earlier in Stage 1
 * Now this functionality is handled by Volley
 */


public class UusedFetchMoviesTask extends AsyncTask<String, Void, MovieContents[]> {
    private final String LOG_TAG = UusedFetchMoviesTask.class.getSimpleName();
    private MainActivityFragment mainActivityFragment;

    public UusedFetchMoviesTask(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    @Override
    protected MovieContents[] doInBackground(String... params) {
        if (params.length == 0) {
            Log.e(LOG_TAG, "Params Returned 0");
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;
        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_ORDER_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_ORDER_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY_PARAM)
                    .build();
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                Log.e(LOG_TAG, "NETWORK ERROR: Nothing Returned from WEB!");
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
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
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            mainActivityFragment.movieContentsArray = new MovieContents[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                MovieContents TempOBJ = new MovieContents(jsonObject1);
                mainActivityFragment.movieContentsArray[i] = TempOBJ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainActivityFragment.movieContentsArray;
    }

    @Override
    protected void onPostExecute(MovieContents[] result) {
        super.onPostExecute(result);
        mainActivityFragment.mPopularMovieAdapter.clear();
        for (MovieContents temp : result)
            mainActivityFragment.mPopularMovieAdapter.add(temp);
    }


}
