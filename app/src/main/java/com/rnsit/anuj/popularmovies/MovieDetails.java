package com.rnsit.anuj.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rnsit.anuj.popularmovies.data.MovieColumns;
import com.rnsit.anuj.popularmovies.data.MovieProvider;
import com.rnsit.anuj.popularmovies.Contents.MovieContents;
import com.rnsit.anuj.popularmovies.Contents.ReviewContents;
import com.rnsit.anuj.popularmovies.Contents.VideoContents;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    @Bind
            (R.id.Movie_Title)
    TextView MOVIE_TITLE;

    @Bind
            (R.id.Movie_Overview)
    TextView MOVIE_OVERVIEW;

    @Bind
            (R.id.Movie_Poster)
    ImageView MOVIE_POSTER;

    @Bind
            (R.id.Movie_Rating)
    TextView MOVIE_RATING;

    @Bind
            (R.id.Movie_Release_Year)
    TextView MOVIE_RELEASE_YEAR;

    MovieContents MOVIE_DATA;
    ReviewContents REVIEW_DATA;
    VideoContents VIDEO_DATA;

    List<ReviewContents> LIST_REVIEW = new ArrayList<>();
    List<VideoContents> LIST_VIDEO = new ArrayList<>();

    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent;
        intent = getIntent();

        if (intent != null) {

            MOVIE_DATA = intent.getExtras().getParcelable("MOVIE_DATA");
            ButterKnife.bind(this);
            Picasso.with(getApplicationContext())
                    .load(MOVIE_DATA.POSTER_PATH)
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
                            try{
                                JSONArray RESULTS = response.getJSONArray("results");
                                int length = RESULTS.length();
                                for(int i=0 ; i<length ; i++ ){
                                    JSONObject temp = RESULTS.getJSONObject(i);
                                    ReviewContents RCTemp = new ReviewContents(temp);

                                }
                            }
                            catch (JSONException e){
                                Log.e("Reviews",e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }
            );
            Volley.newRequestQueue(this).add(ReviewRequest);


            Uri Traileruri = Uri.parse(ReviewsBaseURL + MovieID + TrailerEndpoint).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY_PARAM)
                    .build();
            JsonObjectRequest TrailerRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    Traileruri.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            Volley.newRequestQueue(this).add(TrailerRequest);

        }
        else {
            Toast.makeText(this,"Some Error Occured,Please Try Again",Toast.LENGTH_SHORT).show();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This will set Movie as Favourite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //MOVIE_DATA
                ContentValues MOVIE_VALUES = new ContentValues();
                ContentValues REVIEW_VALUES = new ContentValues();
                ContentValues VIDEO_VALUES = new ContentValues();
                MOVIE_VALUES.put(MovieColumns._ID,MOVIE_DATA.getID());
                MOVIE_VALUES.put(MovieColumns.OVERVIEW,MOVIE_DATA.getOVERVIEW());
                MOVIE_VALUES.put(MovieColumns.RELEASE_DATE,MOVIE_DATA.getRELEASE_DATE());
                MOVIE_VALUES.put(MovieColumns.POSTER_PATH,MOVIE_DATA.getPOSTER_PATH());
                MOVIE_VALUES.put(MovieColumns.TITLE,MOVIE_DATA.getTITLE());
                MOVIE_VALUES.put(MovieColumns.VOTE_AVERAGE,MOVIE_DATA.getAVERAGE_VOTE());

//                REVIEW_VALUES.put(ReviewColumns.MOVIE_ID,MOVIE_DATA.getID());
//                REVIEW_VALUES.put(ReviewColumns.AUTHOR,REVIEW_DATA.getAUTHOR());
//                REVIEW_VALUES.put(ReviewColumns._ID,REVIEW_DATA.getCONTENT());
//
//                VIDEO_VALUES.put(VideoColumns.MOVIE_ID,MOVIE_DATA.getID());
//                VIDEO_VALUES.put(VideoColumns.KEY,VIDEO_DATA.getKEY());
//                VIDEO_VALUES.put(VideoColumns.NAME,VIDEO_DATA.getNAME());

                Cursor cursor = context.getContentResolver().query(MovieProvider.Movie.MOVIE,
                        null,
                        "id=?",
                        new String[]{String.valueOf(MOVIE_DATA.getID())},
                        null);
                if(cursor.moveToFirst()){
                    Toast.makeText(MovieDetails.this, "Already In DataBase", Toast.LENGTH_SHORT).show();
                }
                else {
                    context.getContentResolver().insert(MovieProvider.Movie.MOVIE, MOVIE_VALUES);
                }




            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void getMovieDetails(){

    }


}
