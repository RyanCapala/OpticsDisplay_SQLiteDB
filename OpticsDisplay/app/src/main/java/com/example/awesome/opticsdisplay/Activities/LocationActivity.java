package com.example.awesome.opticsdisplay.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements SearchView
        .OnQueryTextListener, RecyclerViewAdapter.UpdateLocationCountInterface {
    public static final String TAG = "LocationActivity";

    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Display> displayList;
    private List<Display> itemList;
    private TextView tv_title, tv_count;
    private String locatedAt;
    private String[] location_array;

    private String INTENT_KEY = "locationKey";
    private String passed_location;
    private Intent intent;
    
    public static final String PREF_NAME = "PrefCount";
    public static final String PREF_KEY = "count";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_act_loc);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);
        tv_title = (TextView) findViewById(R.id.location_title_cont_loc);
        tv_count = (TextView) findViewById(R.id.location_title_count);
        
        


        //Getting the string that was passed by MainActivity
        intent = getIntent();
        passed_location = intent.getStringExtra(INTENT_KEY);

        Toast.makeText(this, passed_location, Toast.LENGTH_LONG).show();
        tv_title.setText(passed_location);
        
        //for count TextView
        storeCountToSharedPref(String.valueOf(getLocationCount(passed_location)));
        tv_count.setText(getCountFromSharedPref());


        //For recyclerView
        location_array = getResources().getStringArray(R.array.location_array);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_cont_loc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayList = new ArrayList<>();
        itemList = new ArrayList<>();

        //get items from DB by location
        displayList = db.getListByLocation(passed_location);

        for (Display d : displayList) {
            Display display = new Display();
            display.setId(d.getId());
            display.setName(d.getName());
            display.setDescription(d.getDescription());
            display.setModel(d.getModel());
            display.setLocation(d.getLocation());
            display.setShelfLocation(d.getShelfLocation());
            display.setDateItemAdded(d.getDateItemAdded());

            itemList.add(display);
        }


        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    
    
        Log.d(TAG, "onCreate: called");
    }//end onCreate


    //---------------------------------------------------
    // is used for search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.closeBtn_toolbar:
                goBackToMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Display> newList = new ArrayList<>();
        for (Display d : displayList){
            String _name = d.getName().toLowerCase();
            String _model = d.getModel().toLowerCase();
            String _shelf = d.getShelfLocation().toLowerCase();
            if (_name.contains(newText) || _model.contains(newText) || _shelf.contains
                    (newText))
                newList.add(d);
        }
        recyclerViewAdapter.setFilter(newList);
        Toast.makeText(this, "Found " + newList.size() + " items",
                Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }
    
    
    @Override
    public void updateLocationCount(String str) {
        //Log.d(TAG, "updateLocationCount: " + str);
        tv_count.setText(getCountFromSharedPref());
    }
    
    //---------------------------------------------------


    public void goBackToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    private int getLocationCount(String loc) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<Display> displayList = new ArrayList<>();
        displayList = db.getListByLocation(loc);
        
        return displayList.size();
    }
    
    private void storeCountToSharedPref(String locCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY, locCount);
        editor.apply();
        
    }
    
    private String getCountFromSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        
        return sharedPreferences.getString(PREF_KEY, "");
    }
    
    public void refreshActivity() {
        Intent intent = new Intent(LocationActivity.this, LocationActivity.class);
        startActivity(intent);
    }
    
    
}
