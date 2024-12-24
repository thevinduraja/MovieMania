package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Favourites extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> favArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_favourites);

        movieManiaDBHelper = new MovieManiaDBHelper(this);

        displayFavMovies();
    }

    private void displayFavMovies(){
        ListView lView = findViewById(R.id.favMovieListView);
        Cursor cursor = movieManiaDBHelper.getFavMovieTitle();

        int count = 1;
        int checkedCounter = 0;

        //Checking if the db has any saved values.
        if (cursor.getCount() == 0){
            Toast.makeText(Favourites.this,"NO MOVIES ADDED",Toast.LENGTH_LONG).show();
            return;
        }

        //Iterating through the db and printing the names to the list view using an adapter.
        while (cursor.moveToNext()){
            arrayList.add(count +" . " + cursor.getString(0));
            favArrayList.add(cursor.getString(0));
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
            checkedCounter++;
            //Setting all checkboxes to true by default.
            for (int i = 0; i < checkedCounter ; i++) {
                lView.setItemChecked(i, true);
            }
            count++;
        }
    }

    public void favSave(View view){
        ListView lView = findViewById(R.id.favMovieListView);
        SparseBooleanArray checked = lView.getCheckedItemPositions();   //Saving the checked values.
        for (int i = 0; i < lView.getCount() ; i++) {
            if (!checked.get(i)) {  //checking if any items are unchecked and removing them.
                String word = favArrayList.get(i);
                movieManiaDBHelper.removeFavMoive(word);
                Toast.makeText(Favourites.this,"Removed from Favourites",Toast.LENGTH_LONG).show();
                //Restarting the activity.
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        }
    }
}