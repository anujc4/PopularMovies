package com.rnsit.anuj.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent;
        intent = getIntent();


        if (intent != null) {
            MovieContents MOVIE_DATA = intent.getExtras().getParcelable("MOVIE_DATA");

            TextView MOVIE_TITLE = (TextView) findViewById(R.id.Movie_Title);
            TextView MOVIE_OVERVIEW = (TextView) findViewById(R.id.Movie_Overview);
            //Log.d("POSTER PATH",MOVIE_DATA.getPOSTER_PATH());
            ImageView MOVIE_POSTER = (ImageView) findViewById(R.id.Movie_Poster);
            TextView MOVIE_RATING = (TextView) findViewById(R.id.Movie_Rating);
            TextView MOVIE_RELEASE_YEAR = (TextView) findViewById(R.id.Movie_Release_Year);

            Picasso.with(getApplicationContext())
                    .load(MOVIE_DATA.POSTER_PATH)
                    .into(MOVIE_POSTER);
            MOVIE_TITLE.setText(MOVIE_DATA.getORIGINAL_TITLE());
            MOVIE_OVERVIEW.setText(MOVIE_DATA.getOVERVIEW());
            MOVIE_RATING.setText("RATING " + MOVIE_DATA.getAVERAGE_VOTE());
            MOVIE_RELEASE_YEAR.setText(MOVIE_DATA.getRELEASE_DATE());

        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This will set Movie as Favourite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
