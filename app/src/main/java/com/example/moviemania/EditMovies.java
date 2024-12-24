package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class EditMovies extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> favArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_edit_movies);

        movieManiaDBHelper = new MovieManiaDBHelper(this);

        displayEditMovies();
    }

    private void displayEditMovies() {
        ListView lView = findViewById(R.id.editMovieListView);
        Cursor cursor = movieManiaDBHelper.getMovieTitle();

        int count = 1;

        //Checking if the db has any saved values.
        if (cursor.getCount() == 0){
            Toast.makeText(EditMovies.this,"NO MOVIES ADDED",Toast.LENGTH_LONG).show();
            return;
        }

        //Iterating through the db and printing the names to the list view using an adapter.
        while (cursor.moveToNext()){
            arrayList.add(count +" . " + cursor.getString(0));
            favArrayList.add(cursor.getString(0));
            lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList){
                //Setting up the text view colour.
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextColor(Color.rgb(255, 255, 255));
                    return textView;
                }
            });
            lView.setOnItemClickListener(new ListItemClickListener());
            count++;
        }
    }

    //Navigating to the new activity on listView click and passing the index value.
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long idOfView) {
            Intent intent = new Intent(EditMovies.this, EditMovies2.class);
            intent.putExtra("indexValue",position);
            startActivity(intent);
        }
    }
}