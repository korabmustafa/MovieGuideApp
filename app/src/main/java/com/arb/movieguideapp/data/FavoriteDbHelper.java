package com.arb.movieguideapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.arb.movieguideapp.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TAG = "FAVORITE";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public FavoriteDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        Log.i(TAG, "Database opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close(){
        Log.i(TAG, "Database closed");
        dbHandler.close();
    }

    private static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME +
            " (" + FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER, " +
            FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            FavoriteContract.FavoriteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " + "); ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addFavorite(Movie movie) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getThumbnail());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_DESCRIPTION, movie.getDescription());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH, movie.getCoverImg());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + id, null);
    }

    public List<Movie> getFavoriteMovies() {
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME;
        Log.v("LIST", selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        initCursor(cursor, favoriteList);

        db.close();

        return favoriteList;
    }

    public boolean checkIfMovieExists(int movie_Id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME +
                " WHERE " + FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + movie_Id;

        Log.v("TAG", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Movie> searchMovies(String searchMovie) {
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME +
                " WHERE " + FavoriteContract.FavoriteEntry.COLUMN_TITLE + " LIKE '" + searchMovie + "%'";
        Log.v("LIST", selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        initCursor(cursor, favoriteList);

        db.close();

        return favoriteList;
    }

    private void initCursor(Cursor cursor, List<Movie> favoriteList) {
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE))));
                movie.setThumbnail(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setDescription(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_DESCRIPTION)));
                movie.setCoverImg(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)));

                favoriteList.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }
}