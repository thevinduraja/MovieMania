package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayMovies extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    ArrayList<String> favArrayList = new ArrayList<>();
    ListView movieList;
    Button addToFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_display_movies);

        movieManiaDBHelper = new MovieManiaDBHelper(this);
        movieList = findViewById(R.id.movieListView);
        addToFav = findViewById(R.id.btnFav);

        displayMovieList();
    }

    private void displayMovieList() {
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = movieManiaDBHelper.getMovieTitle();

        int count = 1;

        //Checking if the db has any saved values.
        if (cursor.getCount() == 0){
            Toast.makeText(DisplayMovies.this,"NO MOVIES ADDED",Toast.LENGTH_LONG).show();
            return;
        }

        //Iterating through the db and printing the names to the list view using an adapter.
        while (cursor.moveToNext()){
            arrayList.add(count +" . " + cursor.getString(0));
            favArrayList.add(cursor.getString(0));
            ListView lView = findViewById(R.id.movieListView);
            lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayList){
                //Setting up the text view colour.
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextColor(Color.rgb(255, 255, 255));
                    return textView;
                }
            });
            lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            count++;
        }
    }

    public void onClickAddToFav(View view){
        boolean insertData = false;
        ArrayList<String> checkedValues = new ArrayList<>();
        ListView lView = findViewById(R.id.movieListView);
        SparseBooleanArray checked = lView.getCheckedItemPositions();   //Saving the checked values.
        for (int i = 0; i < lView.getCount() ; i++) {
            if (checked.get(i)){
                checkedValues.add(favArrayList.get(i)); //Adding the checked values inorder to insert into the db.
            }
        }
        for (int i = 0; i < checkedValues.size() ; i++) {   //inserting values into the db.
            String title = checkedValues.get(i);
            insertData = movieManiaDBHelper.saveFavMovies(title);
        }
        if (insertData){
            Toast.makeText(DisplayMovies.this,"Added to Favorites",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(DisplayMovies.this,"Movie/s Already Added",Toast.LENGTH_LONG).show();
        }

    }
}