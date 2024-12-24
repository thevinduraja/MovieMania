package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterMovie extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    private TextInputLayout movieTitle;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout movieYear;
    private TextInputLayout movieDirector;
    private TextInputLayout castOfMovie;
    private TextInputLayout movieRating;
    private TextInputLayout movieReview;
    private EditText getMovieYear;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int maxRate = 10;
        int minRate = 1;

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_register_movie);

        movieManiaDBHelper = new MovieManiaDBHelper(this);

        movieTitle = findViewById(R.id.textInputLayoutTitle);
        movieYear = findViewById(R.id.textInputLayoutYear);
        castOfMovie = findViewById(R.id.textInputLayoutCast);
        movieDirector = findViewById(R.id.textInputLayoutDirector);
        movieRating = findViewById(R.id.textInputLayoutRating);
        movieReview = findViewById(R.id.textInputLayoutReview);
        getMovieYear = findViewById(R.id.inputYear);

        //Checking if the rating is in between 1 and 10.
        Objects.requireNonNull(movieRating.getEditText()).setFilters(new InputFilter[]{
                new InputFilterMinMax(minRate, maxRate),
                new InputFilter.LengthFilter(String.valueOf(maxRate).length())
        });
        saveBtn = findViewById(R.id.saveButton);
    }

    //Adding the new movie to the database.
    public void registerMovieToDB(View view){
        //Getting the values from the Text boxes and assigning them to Strings.
        String title = String.valueOf(Objects.requireNonNull(movieTitle.getEditText()).getText());
        String year = String.valueOf(Objects.requireNonNull(movieYear.getEditText()).getText());
        String cast = String.valueOf(Objects.requireNonNull(castOfMovie.getEditText()).getText());
        String director = String.valueOf(Objects.requireNonNull(movieDirector.getEditText()).getText());
        String rating = String.valueOf(Objects.requireNonNull(movieRating.getEditText()).getText());
        String review = String.valueOf(Objects.requireNonNull(movieReview.getEditText()).getText());

        //Checking if all the text inputs are filled.
        if (title.equalsIgnoreCase("")||year.equalsIgnoreCase("")||cast.equalsIgnoreCase("")||director.equalsIgnoreCase("")||rating.equalsIgnoreCase("")||review.equalsIgnoreCase("")){
            Toast.makeText(RegisterMovie.this,"MAKE SURE ALL FIELDS ARE FILLED",Toast.LENGTH_LONG).show();
        } else {
            int userInput = Integer.parseInt(String.valueOf(getMovieYear.getText()));
            //Checking if the movies are in the given year range.
            if (userInput<1895 || userInput>2021){
                Toast.makeText(RegisterMovie.this,"THE MOVIE YEAR SHOULD BE 1895-2021",Toast.LENGTH_LONG).show();
            }else if (userInput >= 1896){
                //Inserting data into the database.
                int intYear = Integer.parseInt(year);
                int intRating = Integer.parseInt(rating);
                boolean insertData = movieManiaDBHelper.registerMovie(title,intYear,cast,director,intRating,review);
                if (insertData){
                    Toast.makeText(RegisterMovie.this,"SAVED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                    //Clearing all the fields after data has been inserted.
                    movieTitle.getEditText().getText().clear();
                    movieTitle.clearFocus();
                    movieYear.getEditText().getText().clear();
                    movieYear.clearFocus();
                    castOfMovie.getEditText().getText().clear();
                    castOfMovie.clearFocus();
                    movieDirector.getEditText().getText().clear();
                    movieDirector.clearFocus();
                    movieRating.getEditText().getText().clear();
                    movieRating.clearFocus();
                    movieReview.getEditText().getText().clear();
                    movieReview.clearFocus();
                }else {
                    Toast.makeText(RegisterMovie.this,"FAILED TO SAVE",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Referenced from : https://stackoverflow.com/questions/41505465/making-edittext-accept-a-range-of-values-without-post-validation
    //To check if the ratings are in the given range.
    private static class InputFilterMinMax implements InputFilter {
        private final int min;
        private final int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            return "Error";
        }

        private boolean isInRange(int min, int max, int input) {
            return max > min ? input >= min && input <= max : input >= max && input <= min;
        }
    }
}