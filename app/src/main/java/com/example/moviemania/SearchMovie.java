package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SearchMovie extends AppCompatActivity {
    private MovieManiaDBHelper movieManiaDBHelper;
    private TextInputLayout searchBar;
    String searchInput;
    Button button;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> castList = new ArrayList<>();
    ArrayList<String> directorList = new ArrayList<>();
    ArrayList<String> searchOutputList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search_movie);

        movieManiaDBHelper = new MovieManiaDBHelper(this);
        button = findViewById(R.id.btnLookup);
        button.setText("Lookup");

        searchBar = findViewById(R.id.textInputSearchBar);
    }

    @SuppressLint("SetTextI18n")
    public void searchResult(View view) {
        if (button.getText().equals("Lookup")) {
            searchInput = String.valueOf(Objects.requireNonNull(searchBar.getEditText()).getText());

            Cursor cursor = movieManiaDBHelper.getMovieDetails();

            if (cursor.getCount() == 0) {
                Toast.makeText(SearchMovie.this, "NO MOVIES ADDED", Toast.LENGTH_LONG).show();
                return;
            }
            while (cursor.moveToNext()) {
                nameList.add(cursor.getString(0));
                castList.add(cursor.getString(2));
                directorList.add(cursor.getString(3));
            }

            String lowerSearchInput = searchInput.toLowerCase();

            //Iterating through the NAMES array to search the char values and to display the output.
            for (int i = 0; i < nameList.size(); i++) {
                String lowerDbValue = nameList.get(i).toLowerCase();
                if (lowerDbValue.contains(lowerSearchInput)) {
                    searchOutputList.add("TITLE : " + nameList.get(i));
                }
            }

            //Iterating through the CAST array to search the char values and to display the output.
            for (int i = 0; i < castList.size(); i++) {
                String lowerDbValue = castList.get(i).toLowerCase();
                if (lowerDbValue.contains(lowerSearchInput)) {
                    searchOutputList.add("CAST : " + castList.get(i));
                }
            }

            //Iterating through the DIRECTOR array to search the char values and to display the output.
            for (int i = 0; i < directorList.size(); i++) {
                String lowerDbValue = directorList.get(i).toLowerCase();
                if (lowerDbValue.contains(lowerSearchInput)) {
                    searchOutputList.add("DIRECTOR : " + directorList.get(i));
                }
            }

            //Displaying the output.
            ListView lView = findViewById(R.id.movieSearchListView);
            lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchOutputList){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextColor(Color.rgb(255, 255, 255));
                    return textView;
                }
            });

            if (searchOutputList.size()==0){
                Toast.makeText(SearchMovie.this,"NO MOVIES",Toast.LENGTH_LONG).show();
            }

            button.setText("New Search");
        }else if (button.getText().equals("New Search")){
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}