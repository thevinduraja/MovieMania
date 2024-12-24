package com.example.moviemania;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieManiaDBHelper extends SQLiteOpenHelper {
    private static final String DBName = "MovieMania.db";
    private static final String TABLE1 = "RegisterMovieTable";
    private static final String TABLE2 = "FavouriteMovieTable";

    private static final String TABLE1_COL1 = "TITLE";
    private static final String TABLE1_COL2 = "YEAR";
    private static final String TABLE1_COL3 = "CASTOFMOVIE";
    private static final String TABLE1_COL4 = "DIRECTOR";
    private static final String TABLE1_COL5 = "RATINGS";
    private static final String TABLE1_COL6 = "REVIEW";

    private static final String TABLE2_COL1 = "FAVTITLE";

    public MovieManiaDBHelper(Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE1 + "(TITLE TEXT PRIMARY KEY, YEAR INTEGER, CASTOFMOVIE TEXT, DIRECTOR TEXT, RATINGS INTEGER, REVIEW TEXT)");
        db.execSQL("create table " + TABLE2 + "(FAVTITLE TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //Registering a movie to the Database.
    public boolean registerMovie(String title, int year, String castOfMovie, String director, int ratings, String review){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL1,title);
        contentValues.put(TABLE1_COL2,year);
        contentValues.put(TABLE1_COL3,castOfMovie);
        contentValues.put(TABLE1_COL4,director);
        contentValues.put(TABLE1_COL5,ratings);
        contentValues.put(TABLE1_COL6,review);
        long result = db.insert(TABLE1,null,contentValues);
        return result != -1;
    }

    //To get all title names from the movies.
    public Cursor getMovieTitle(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select TITLE from " + TABLE1 + " order by " + TABLE1_COL1,null);
        return cursor;
    }

    //To get all movie names.
    public Cursor getMovieDetails(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE1 + " order by " + TABLE1_COL1,null);
        return cursor;
    }

    //Saving the Favourite Movies.
    public boolean saveFavMovies(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COL1,title);
        long result = db.insert(TABLE2,null,contentValues);
        return result != -1;
    }

    //To get the favourite movie titles.
    public Cursor getFavMovieTitle(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select FAVTITLE from " + TABLE2 + " order by " + TABLE2_COL1,null);
        return cursor;
    }


    //Deleting the movie from the Favourites Table.
    public void removeFavMoive(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE2,"FAVTITLE=?",new String[] { word });
    }

    //Updating the Movie details.
    public Boolean updateMovies(String title, int year, String castOfMovie, String director, int ratings, String review) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL1,title);
        contentValues.put(TABLE1_COL2,year);
        contentValues.put(TABLE1_COL3,castOfMovie);
        contentValues.put(TABLE1_COL4,director);
        contentValues.put(TABLE1_COL5,ratings);
        contentValues.put(TABLE1_COL6,review);
        Cursor cursor = DB.rawQuery("Select * from "+ TABLE1 +" where TITLE=?", new String[]{title});
        if (cursor.getCount() > 0) {
            long result = DB.update(TABLE1, contentValues, "TITLE=?", new String[]{title});
            return result != -1;
        } else {
            return false;
        }}

}
