package com.rnsit.anuj.popularmovies.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rnsit.anuj.popularmovies.Activities.MovieDetails;
import com.rnsit.anuj.popularmovies.Adapter.ReviewAdapter;
import com.rnsit.anuj.popularmovies.Adapter.VideoAdapter;
import com.rnsit.anuj.popularmovies.BuildConfig;
import com.rnsit.anuj.popularmovies.Contents.MovieContents;
import com.rnsit.anuj.popularmovies.Contents.ReviewContents;
import com.rnsit.anuj.popularmovies.Contents.VideoContents;
import com.rnsit.anuj.popularmovies.R;
import com.rnsit.anuj.popularmovies.Utility.Utility;
import com.rnsit.anuj.popularmovies.data.MovieColumns;
import com.rnsit.anuj.popularmovies.data.MovieProvider;
import com.rnsit.anuj.popularmovies.data.ReviewColumns;
import com.rnsit.anuj.popularmovies.data.VideoColumns;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    final String LOG_TAG = MovieDetails.class.getSimpleName();
    public String VIDEO_TRAILER_ENDPOINT;
    MovieContents MOVIE_DATA;
    ReviewContents[] REVIEW_DATA;
    VideoContents[] VIDEO_DATA;
    ReviewAdapter mReviewAdapter;
    VideoAdapter mVideoAdapter;
    Context context = getActivity();

    public MovieDetailsFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
//        Intent intent;
//        intent = getActivity().getIntent();
        Bundle bundle = getArguments();
        updateView(bundle, rootView);
        return rootView;
    }

    void updateView(Bundle bundle, View rootView) {
        if (bundle != null) {
            MOVIE_DATA = bundle.getParcelable("MOVIE_DATA");
            //MOVIE_DATA = bundle.getExtras().getParcelable("MOVIE_DATA");
            TextView MOVIE_TITLE = (TextView) rootView.findViewById(R.id.Movie_Title);
            TextView MOVIE_OVERVIEW = (TextView) rootView.findViewById(R.id.Movie_Overview);
            ImageView MOVIE_POSTER = (ImageView) rootView.findViewById(R.id.Movie_Poster);
            TextView MOVIE_RATING = (TextView) rootView.findViewById(R.id.Movie_Rating);
            TextView MOVIE_RELEASE_YEAR = (TextView) rootView.findViewById(R.id.Movie_Release_Year);

            Picasso.with(context)
                    .load(MOVIE_DATA.POSTER_PATH)
                    .placeholder(R.drawable.moviepic)
                    .error(R.drawable.moviepic)
                    .into(MOVIE_POSTER);
            MOVIE_TITLE.setText(MOVIE_DATA.getTITLE());
            MOVIE_OVERVIEW.setText(MOVIE_DATA.getOVERVIEW());
            MOVIE_RATING.setText("RATING " + String.valueOf(MOVIE_DATA.getAVERAGE_VOTE()));
            MOVIE_RELEASE_YEAR.setText(MOVIE_DATA.getRELEASE_DATE());

            final String ReviewsBaseURL = "http://api.themoviedb.org/3/movie/";
            final String MovieID = String.valueOf(MOVIE_DATA.getID());
            final String ReviewEndpoint = "/reviews";
            final String TrailerEndpoint = "/videos";
            final String API_KEY = "api_key";

            mReviewAdapter = new ReviewAdapter(getActivity(),
                    R.layout.reviews_item_layout,
                    new ArrayList<ReviewContents>());
            //mReviewAdapter.clear();
            ListView reviewListView = (ListView) rootView.findViewById(R.id.view_movie_reviews);
            reviewListView.setAdapter(mReviewAdapter);
            Utility.getListViewSize(reviewListView);

            if (Utility.isNetworkAvailable(getActivity())) {

                Uri ReviewUri = Uri.parse(ReviewsBaseURL + MovieID + ReviewEndpoint).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.API_KEY_PARAM)
                        .build();

                JsonObjectRequest ReviewRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        ReviewUri.toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Log.d(LOG_TAG, "Review Request Successful");
                                try {
                                    JSONArray RESULTS = response.getJSONArray("results");
                                    int length = RESULTS.length();
                                    //Log.d(LOG_TAG, "Length of Review Results : " + length);
                                    REVIEW_DATA = new ReviewContents[length];
                                    for (int i = 0; i < length; i++) {
                                        JSONObject temp = RESULTS.getJSONObject(i);
                                        ReviewContents RCTemp = new ReviewContents(temp);
                                        REVIEW_DATA[i] = RCTemp;
                                        mReviewAdapter.add(RCTemp);
                                    }
                                } catch (JSONException e) {
                                    Log.e("Reviews", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Some Error Occoured While Fetching Reviews. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                Volley.newRequestQueue(getActivity()).add(ReviewRequest);


            } else {
                Cursor cursor = getActivity().getContentResolver().query(
                        MovieProvider.Review.REVIEWS
                        , null
                        , "movie_id=?"
                        , new String[]{MovieID}
                        , null);
                if (!cursor.moveToFirst()) {
                    Log.e(LOG_TAG, "Empty Cursor Returned For Reviews");
                } else {
//                    Log.d(LOG_TAG, "Loading Review Data From Cursor");
                    do {
                        String AUTHOR = cursor.getString(cursor.getColumnIndex(ReviewColumns.AUTHOR));
                        String CONTENT = cursor.getString(cursor.getColumnIndex(ReviewColumns.CONTENT));
                        ReviewContents TEMP = new ReviewContents(AUTHOR, CONTENT);
                        mReviewAdapter.add(TEMP);
                    } while (cursor.moveToNext());
                }
                cursor.close();

            }


            //Start Fetching Video Trailers
            Uri Traileruri = Uri.parse(ReviewsBaseURL + MovieID + TrailerEndpoint).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY_PARAM)
                    .build();

            mVideoAdapter = new VideoAdapter(getActivity(),
                    R.layout.trailers_item_layout,
                    new ArrayList<VideoContents>());
            //mVideoAdapter.clear();
            final ListView videoListView = (ListView) rootView.findViewById(R.id.view_movie_trailers);
            videoListView.setAdapter(mVideoAdapter);
            Utility.getListViewSize(videoListView);
            videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(context, "Launch Trailer " + position, Toast.LENGTH_SHORT).show();
                    VideoContents TEMP = mVideoAdapter.getItem(position);
                    String VIDEO_ENDPOINT = TEMP.getKEY();
                    String FINAL_URL = VIDEO_URL + VIDEO_ENDPOINT;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FINAL_URL));
                    startActivity(intent);
                }
            });


            if (Utility.isNetworkAvailable(getActivity())) {
                JsonObjectRequest VideoRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        Traileruri.toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Log.d(LOG_TAG, "Video Request Successful");
                                try {
                                    JSONArray RESULTS = response.getJSONArray("results");
                                    int length = RESULTS.length();
                                    //Log.d(LOG_TAG, "Length of Video Results : " + length);
                                    VIDEO_DATA = new VideoContents[length];
                                    for (int i = 0; i < length; i++) {
                                        JSONObject temp = RESULTS.getJSONObject(i);

                                        VideoContents VCTemp = new VideoContents(temp);
                                        VIDEO_DATA[i] = VCTemp;
                                        mVideoAdapter.add(VCTemp);
                                    }
                                    VIDEO_TRAILER_ENDPOINT = VIDEO_DATA[0].getKEY();
                                    Log.e(LOG_TAG, "2" + VIDEO_TRAILER_ENDPOINT);
                                } catch (JSONException e) {
                                    Log.e("Videos", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(context, "Some Error Occoured While Fetching Reviews. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                Volley.newRequestQueue(getActivity()).add(VideoRequest);
            } else {
                Cursor cursor = getActivity().getContentResolver().query(
                        MovieProvider.Video.VIDEO
                        , null
                        , "movie_id=?"
                        , new String[]{MovieID}
                        , null);
                if (!cursor.moveToFirst()) {
                    Log.e(LOG_TAG, "Empty Cursor Returned For Videos");
                } else {
                    VIDEO_TRAILER_ENDPOINT = cursor.getString(cursor.getColumnIndex(VideoColumns.KEY));
                    Log.e(LOG_TAG, "3" + VIDEO_TRAILER_ENDPOINT);
//                    Log.d(LOG_TAG, "Loading Video Data From Cursor");
                    do {
                        String KEY = cursor.getString(cursor.getColumnIndex(VideoColumns.KEY));
                        String NAME = cursor.getString(cursor.getColumnIndex(VideoColumns.NAME));
                        VideoContents TEMP = new VideoContents(KEY, NAME);
                        mVideoAdapter.add(TEMP);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }


        } else {
            Log.e(LOG_TAG, "Bundle passed was null!!!!!");
        }


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = getActivity().getContentResolver().query(
                        MovieProvider.Movie.MOVIE,
                        null,
                        "id=?",
                        new String[]{String.valueOf(MOVIE_DATA.getID())},
                        null);
                if (cursor.moveToFirst()) {
                    Snackbar.make(view, "Movie Already Marked Favourite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    int inserted_review = 0, inserted_video = 0;
                    Snackbar.make(view, "Movie Marked as Favourite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    ContentValues MOVIE_VALUES = new ContentValues();
                    MOVIE_VALUES.put(MovieColumns._ID, MOVIE_DATA.getID());
                    MOVIE_VALUES.put(MovieColumns.OVERVIEW, MOVIE_DATA.getOVERVIEW());
                    MOVIE_VALUES.put(MovieColumns.RELEASE_DATE, MOVIE_DATA.getRELEASE_DATE());
                    MOVIE_VALUES.put(MovieColumns.POSTER_PATH, MOVIE_DATA.getPOSTER_PATH());
                    MOVIE_VALUES.put(MovieColumns.TITLE, MOVIE_DATA.getTITLE());
                    MOVIE_VALUES.put(MovieColumns.VOTE_AVERAGE, MOVIE_DATA.getAVERAGE_VOTE());
                    getActivity().getContentResolver().insert(MovieProvider.Movie.MOVIE, MOVIE_VALUES);

                    for (int i = 0; i < VIDEO_DATA.length; i++) {
                        ContentValues TEMP = new ContentValues();
                        TEMP.put(VideoColumns.MOVIE_ID, MOVIE_DATA.getID());
                        TEMP.put(VideoColumns.KEY, VIDEO_DATA[i].getKEY());
                        TEMP.put(VideoColumns.NAME, VIDEO_DATA[i].getNAME());
                        getActivity().getContentResolver().insert(MovieProvider.Video.VIDEO, TEMP);
                    }

                    for (int i = 0; i < REVIEW_DATA.length; i++) {
                        ContentValues TEMP = new ContentValues();
                        TEMP.put(ReviewColumns.MOVIE_ID, MOVIE_DATA.getID());
                        TEMP.put(ReviewColumns.AUTHOR, REVIEW_DATA[i].getAUTHOR());
                        TEMP.put(ReviewColumns.CONTENT, REVIEW_DATA[i].getCONTENT());
                        getActivity().getContentResolver().insert(MovieProvider.Review.REVIEWS, TEMP);
                    }
                }
                cursor.close();
            }
        });

        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, VIDEO_URL + VIDEO_TRAILER_ENDPOINT);
                startActivity(Intent.createChooser(intent, "Share Trailer"));
            }
        });

    }


    private void createShareForecastIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, VIDEO_URL + VIDEO_TRAILER_ENDPOINT);
    }
}
