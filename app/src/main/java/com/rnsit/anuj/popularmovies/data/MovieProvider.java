package com.rnsit.anuj.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by anuj on 2/15/2016.
 */
@ContentProvider(authority = MovieProvider.CONTENT_AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String CONTENT_AUTHORITY = "com.rnsit.anuj.popularmovies";

    @TableEndpoint(table = MovieDatabase.MOVIE)
    public static class Movie {
        @ContentUri(
                path = MovieDatabase.MOVIE,
                type = "vnd.android.cursor.dir/movie"
                //defaultSort = MovieColumns.TITLE + " ASC"
        )
        public static final Uri MOVIE = Uri.parse("content://" + CONTENT_AUTHORITY + "/movie");

        @InexactContentUri(
                path = MovieDatabase.MOVIE + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + CONTENT_AUTHORITY + "/lists/" + id);
        }
    }


    @TableEndpoint(table = MovieDatabase.VIDEO)
    public static class Video {
        @ContentUri(
                path = MovieDatabase.VIDEO,
                type = "vnd.android.cursor.dir/video",
                defaultSort = VideoColumns._ID + " ASC"
        )

        public static final Uri VIDEO = Uri.parse("content://" + CONTENT_AUTHORITY + "/video");

        @InexactContentUri(
                path = MovieDatabase.VIDEO + "/#",
                name = "VIDEO_ID",
                type = "vnd.android.cursor.item/video",
                whereColumn = VideoColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + CONTENT_AUTHORITY + "/lists/" + id);
        }
    }

    @TableEndpoint(table = MovieDatabase.REVIEW)
    public static class Review {
        @ContentUri(
                path = MovieDatabase.REVIEW,
                type = "vnd.android.cursor.dir/review",
                defaultSort = ReviewColumns._ID + " ASC"
        )
        public static final Uri REVIEWS = Uri.parse("content://" + CONTENT_AUTHORITY + "/review");

        @InexactContentUri(
                path = MovieDatabase.REVIEW + "/#",
                name = "REVIEW_ID",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + CONTENT_AUTHORITY + "/lists/" + id);
        }
    }
}
