package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class EditMovies2 extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    int index;
    //Setting up arrayLists to save each column values separately.
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> castList = new ArrayList<>();
    ArrayList<String> directorList = new ArrayList<>();
    ArrayList<Integer> ratingsList = new ArrayList<>();
    ArrayList<String> reviewList = new ArrayList<>();
    ArrayList<String> favList = new ArrayList<>();

    TextView movieNameHeader;
    private TextInputLayout movieTitle;
    private TextInputLayout movieYear;
    private TextInputLayout movieDirector;
    private TextInputLayout castOfMovie;
    private TextInputLayout movieReview;
    private EditText getMovieYear;
    LinearLayout linearlayout;
    CheckBox chk1;
    RatingBar ratingBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_edit_movies2);

        movieManiaDBHelper = new MovieManiaDBHelper(this);

        Intent intent = getIntent();
        index = intent.getExtras().getInt("indexValue");

        movieNameHeader = findViewById(R.id.textViewUpdateMovies);
        movieTitle = findViewById(R.id.textInputLayoutTitleUpdate);
        movieYear = findViewById(R.id.textInputLayoutYearUpdate);
        castOfMovie = findViewById(R.id.textInputLayoutCastUpdate);
        movieDirector = findViewById(R.id.textInputLayoutDirectorUpdate);
        movieReview = findViewById(R.id.textInputLayoutReviewUpdate);
        ratingBar = findViewById(R.id.ratingBar);
        getMovieYear = findViewById(R.id.inputYearUpdate);

        //Adding a text view and checkbox to indicate if the movie is a favourite movie.
        linearlayout = findViewById(R.id.linearFav);
        LinearLayout lin1 = new LinearLayout(this);
        lin1.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(this);
        chk1 = new CheckBox(this);
        textView.setText("  Favourites                        ");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(30);
        lin1.addView(textView);
        lin1.addView(chk1);
        linearlayout.addView(lin1);

        displayUpdateMovies();
    }

    //Accessing data from the database and displaying it in the relevant fields.
    private void displayUpdateMovies() {
        Cursor cursor = movieManiaDBHelper.getMovieDetails();

        //Checking if the db has any saved values.
        if (cursor.getCount() == 0){
            Toast.makeText(EditMovies2.this,"NO MOVIES ADDED",Toast.LENGTH_LONG).show();
            return;
        }

        //Iterating through the db and saving each value in the relevant arrayList.
        while (cursor.moveToNext()){
            nameList.add(cursor.getString(0));
            yearList.add(cursor.getString(1));
            castList.add(cursor.getString(2));
            directorList.add(cursor.getString(3));
            ratingsList.add(cursor.getInt(4));
            reviewList.add(cursor.getString(5));
        }

        //Setting up the values accessed from the DB to the Fields.
        movieNameHeader.setText(nameList.get(index));
        Objects.requireNonNull(movieTitle.getEditText()).setText(nameList.get(index));
        Objects.requireNonNull(movieYear.getEditText()).setText(yearList.get(index));
        Objects.requireNonNull(castOfMovie.getEditText()).setText(castList.get(index));
        Objects.requireNonNull(movieDirector.getEditText()).setText(directorList.get(index));
        Objects.requireNonNull(movieReview.getEditText()).setText(reviewList.get(index));
        ratingBar.setRating(ratingsList.get(index));

        //Checking if the movie is a favourite movie or not and checking them accordingly.
        Cursor cursor1 = movieManiaDBHelper.getFavMovieTitle();
        if (cursor1.getCount() == 0){
            return;
        }
        while (cursor1.moveToNext()){
            favList.add(cursor1.getString(0));
        }
        for (int i = 0; i < favList.size() ; i++) {
            if (nameList.get(index).equalsIgnoreCase(favList.get(i))){
                chk1.setChecked(true);
            }
        }
    }

    public void updateDetails(View view){
        String title = String.valueOf(Objects.requireNonNull(movieTitle.getEditText()).getText());
        String year = String.valueOf(Objects.requireNonNull(movieYear.getEditText()).getText());
        String cast = String.valueOf(Objects.requireNonNull(castOfMovie.getEditText()).getText());
        String director = String.valueOf(Objects.requireNonNull(movieDirector.getEditText()).getText());
        String rating = String.valueOf(ratingBar.getRating());
        String review = String.valueOf(Objects.requireNonNull(movieReview.getEditText()).getText());

        //Checking if all the text inputs are filled.
        if (title.equalsIgnoreCase("")||year.equalsIgnoreCase("")||cast.equalsIgnoreCase("")||director.equalsIgnoreCase("")||rating.equalsIgnoreCase("")||review.equalsIgnoreCase("")){
            Toast.makeText(EditMovies2.this,"MAKE SURE ALL FIELDS ARE FILLED",Toast.LENGTH_LONG).show();
        } else {
            int userInput = Integer.parseInt(String.valueOf(getMovieYear.getText()));
            //Checking if the movies are in the given year range.
            if (userInput<1895 || userInput>2021){
                Toast.makeText(EditMovies2.this,"THE MOVIE YEAR SHOULD BE 1895-2021",Toast.LENGTH_LONG).show();
            }else if (userInput >= 1896){
                //Inserting data into the database.
                int intYear = Integer.parseInt(year);
                double data = ratingBar.getRating();
                Double newData = new Double(data);
                int intRating = newData.intValue();
                boolean insertData = movieManiaDBHelper.updateMovies(title,intYear,cast,director,intRating,review);
                if (chk1.isChecked()){
                    movieManiaDBHelper.saveFavMovies(title);
                }else {
                    movieManiaDBHelper.removeFavMoive(title);
                }
                if (insertData){
                    Toast.makeText(EditMovies2.this,"UPDATED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(EditMovies2.this,"FAILED TO UPDATE",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}