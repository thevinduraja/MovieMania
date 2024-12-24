package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Ratings extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    ArrayList<String> favArrayList = new ArrayList<>();
    ListView movieList;
    String selectedMovie;
    Button btnIMDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_ratings);

        movieManiaDBHelper = new MovieManiaDBHelper(this);
        movieList = findViewById(R.id.movieListViewIMDB);
        btnIMDB = findViewById(R.id.btnIMDB);


        btnIMDB.setOnClickListener(v -> {
            SparseBooleanArray checked = movieList.getCheckedItemPositions();
            for (int i = 0; i < movieList.getCount() ; i++) {
                if (checked.get(i)){
                    selectedMovie = favArrayList.get(i);
                }
            }
            Intent intent = new Intent(Ratings.this, Ratings2.class);
            intent.putExtra("selectedMovie",selectedMovie);
            startActivity(intent);
        });
        displayRatings();
    }

    private void displayRatings() {
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = movieManiaDBHelper.getMovieTitle();

        int count = 1;

        //Checking if the db has any saved values.
        if (cursor.getCount() == 0){
            Toast.makeText(Ratings.this,"NO MOVIES ADDED",Toast.LENGTH_LONG).show();
            return;
        }

        //Iterating through the db and printing the names to the list view using an adapter.
        while (cursor.moveToNext()){
            arrayList.add(count +" . " + cursor.getString(0));
            favArrayList.add(cursor.getString(0));
            ListView lView = findViewById(R.id.movieListViewIMDB);
            lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayList){
                //Setting up the text view colour.
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextColor(Color.rgb(255, 255, 255));
                    return textView;
                }
            });
            lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //Making the choice mode single so only one value can be selected.
            count++;
        }
    }

}