package com.rnsit.anuj.popularmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.rnsit.anuj.popularmovies.Fragments.MainActivityFragment;
import com.rnsit.anuj.popularmovies.Fragments.MovieDetailsFragment;
import com.rnsit.anuj.popularmovies.R;
import com.rnsit.anuj.popularmovies.Utility.Utility;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "DETAIL_TAG";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Stetho.initializeWithDefaults(this);


        if (findViewById(R.id.fragment_details) != null) {

            Log.d(LOG_TAG, "Creating Multi-Pane Layout");
            mTwoPane = true;
            Utility.isMultiPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_details, new MovieDetailsFragment(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }

        } else {
            Utility.isMultiPane = false;
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Bundle bundle) {
        Log.d(LOG_TAG, "onItemSelected");
        if (mTwoPane) {
            Log.d(LOG_TAG, "onItemSelected: Replacing Detail Fragment");
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
                    .commit();

        } else {

            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
