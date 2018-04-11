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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.UI.RecyclerViewAdapter;
import com.example.awesome.opticsdisplay.Util.Constants;

import java.lang.reflect.Field;
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
    private TextView tv_title, tv_count, shelf_count_id, shelf_count, shelf_name_id;
    private Button shelf1_btn, shelf2_btn, shelf3_btn, shelf4_btn, shelf5_btn, display_all_btn;
    private Button shelf6_btn;
    private String locatedAt;
    private String[] location_array;
    private String[] shelf_name_array;
    private Spinner shelfSpinner;
    private String choosenShelf = "";

    private String INTENT_KEY = "locationKey";
    private String passed_location;
    private Intent intent;
    private String SHELFNAME = "SHELF ";
    
    private static final String PREF_NAME = "PrefCount";
    private static final String PREF_KEY = "count";
    private static final String PREF_KEY1 = "s1_count";
    private static final String PREF_KEY2 = "s2_count";
    private static final String PREF_KEY3 = "s3_count";
    private static final String PREF_KEY4 = "s4_count";
    private static final String PREF_KEY5 = "s5_count";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_act_loc);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);
        initWidgets();
        hideTextView(true);
        


        //Getting the string that was passed by MainActivity
        intent = getIntent();
        passed_location = intent.getStringExtra(INTENT_KEY);

        tv_title.setText(passed_location);
        checkLocation(passed_location);
        
        //for count TextView
        storeCountToSharedPref(String.valueOf(getLocationCount(passed_location)));
        tv_count.setText(getCountFromSharedPref());

        //for shelf name spinner
        shelf_name_array = getResources().getStringArray(R.array.shelf_name);

        //For recyclerView
        location_array = getResources().getStringArray(R.array.location_array);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_cont_loc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayList = new ArrayList<>();
        itemList = new ArrayList<>();

        //get items from DB by location
//        displayList = db.getListByLocation(passed_location);
//
//        for (Display d : displayList) {
//            Display display = new Display();
//            display.setId(d.getId());
//            display.setName(d.getName());
//            display.setDescription(d.getDescription());
//            display.setModel(d.getModel());
//            display.setLocation(d.getLocation());
//            display.setShelfLocation(d.getShelfLocation());
//            display.setDateItemAdded(d.getDateItemAdded());
//            display.setOldLocation(d.getOldLocation());
//
//            itemList.add(display);
//        }

        displayItems(passed_location);


        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList, this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        shelfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosenShelf = shelf_name_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        display_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayItems(passed_location);
                recyclerViewAdapter.setFilter(displayList);
                hideTextView(true);
            }
        });

        shelf1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isLifestyleSaleCustomShelf(passed_location)) {
                    hideTextView(false);
                    String str = choosenShelf + "1";
                    searchShelf(str, PREF_KEY1);
                    if (!choosenShelf.equals("**")) {
                        shelf_name_id.setText(str);
                    } else {
                        shelf_name_id.setText("Shelf");
                    }

                } else {
                    if (!choosenShelf.equals("**")) {
                        String s1 = "1";
                        searchShelf(s1, PREF_KEY1);
                        hideTextView(true);
                    }



                }


                shelf_count.setText(getShelfCountFromSharedPref(PREF_KEY1));
                hideTextView(false);
                if (!isLifestyleSaleCustomShelf(passed_location)) {
                    shelf_name_id.setText(SHELFNAME + "1");
                }

            }
        });

        shelf2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLifestyleSaleCustomShelf(passed_location)) {
                    hideTextView(false);
                    String str = choosenShelf + "2";
                    searchShelf(str, PREF_KEY2);
                    if (!choosenShelf.equals("**")) {
                        shelf_name_id.setText(str);
                    } else {
                        shelf_name_id.setText("Shelf");
                    }
                } else {
                    if (!choosenShelf.equals("**")) {
                        String s = "2";
                        searchShelf(s, PREF_KEY2);
                        hideTextView(true);
                    }

                }

                shelf_count.setText(getShelfCountFromSharedPref(PREF_KEY2));
                hideTextView(false);
                if (!isLifestyleSaleCustomShelf(passed_location)) {
                    shelf_name_id.setText(SHELFNAME + "2");
                }

            }
        });

        shelf3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLifestyleSaleCustomShelf(passed_location)) {
                    hideTextView(false);
                    String str = choosenShelf + "3";
                    searchShelf(str, PREF_KEY3);
                    if (!choosenShelf.equals("**")) {
                        shelf_name_id.setText(str);
                    } else {
                        shelf_name_id.setText("Shelf");
                    }
                } else {
                    if (!choosenShelf.equals("**")) {
                        String s = "3";
                        searchShelf(s, PREF_KEY3);
                        hideTextView(true);
                    }

                }
//                String s3 = "3";
//                searchShelf(s3, PREF_KEY3);

                shelf_count.setText(getShelfCountFromSharedPref(PREF_KEY3));
                hideTextView(false);
                if (!isLifestyleSaleCustomShelf(passed_location)) {
                    shelf_name_id.setText(SHELFNAME + "3");
                }

            }
        });

        shelf4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLifestyleSaleCustomShelf(passed_location)) {
                    hideTextView(false);
                    String str = choosenShelf + "4";
                    searchShelf(str, PREF_KEY4);
                    if (!choosenShelf.equals("**")) {
                        shelf_name_id.setText(str);
                    } else {
                        shelf_name_id.setText("Shelf");
                    }
                } else {
                    if (!choosenShelf.equals("**")) {
                        String s = "4";
                        searchShelf(s, PREF_KEY4);
                    }

                }
//                String s4 = "4";
//                searchShelf(s4, PREF_KEY4);

                shelf_count.setText(getShelfCountFromSharedPref(PREF_KEY4));
                hideTextView(false);
                if (!isLifestyleSaleCustomShelf(passed_location)) {
                    shelf_name_id.setText(SHELFNAME + "4");
                }

            }
        });

        shelf5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s5 = "5";
                searchShelf(s5, PREF_KEY5);

                shelf_count.setText(getShelfCountFromSharedPref(PREF_KEY5));
                hideTextView(false);
                if (!isLifestyleSaleCustomShelf(passed_location)) {
                    shelf_name_id.setText(SHELFNAME + "5");
                }

            }
        });


    }//end onCreate


    private void displayItems(String location) {

        displayList = db.getListByLocation(location);

        for (Display d : displayList) {
            Display display = new Display();
            display.setId(d.getId());
            display.setName(d.getName());
            display.setDescription(d.getDescription());
            display.setModel(d.getModel());
            display.setLocation(d.getLocation());
            display.setShelfLocation(d.getShelfLocation());
            display.setDateItemAdded(d.getDateItemAdded());
            display.setOldLocation(d.getOldLocation());

            itemList.add(display);
        }

    }

    //---------------------------------------------------
    // is used for search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        //--- To change the cursor color of the search menu ---//
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById
                (android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor_color);
        } catch (Exception e) {

        }
        //------------------------------------------------------//

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
        //Toast.makeText(this, "Found " + newList.size() + " items",
        //        Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }
    
    //----INTERFACES-----//
    @Override
    public void updateLocationCount(String str) {
        //Log.d(TAG, "updateLocationCount: " + str);
        tv_count.setText(getCountFromSharedPref());
    }

    @Override
    public void updateShelfCount(String count) {
        //Log.d(TAG, "updateShelfCount: ======>>>> " + count);
        shelf_count.setText(count);
    }

    //---------------------------------------------------


    private void initWidgets() {
        tv_title = (TextView) findViewById(R.id.location_title_cont_loc);
        tv_count = (TextView) findViewById(R.id.location_title_count);
        shelf_count = (TextView) findViewById(R.id.shelf_count_tv);
        shelf_count_id = (TextView) findViewById(R.id.shelf_count_id_tv);
        shelf_name_id = (TextView) findViewById(R.id.shelf_name_id_tv);
        shelf1_btn = (Button) findViewById(R.id.shelf1_btn);
        shelf2_btn = (Button) findViewById(R.id.shelf2_btn);
        shelf3_btn = (Button) findViewById(R.id.shelf3_btn);
        shelf4_btn = (Button) findViewById(R.id.shelf4_btn);
        shelf5_btn = (Button) findViewById(R.id.shelf5_btn);
        //shelf6_btn = (Button) findViewById(R.id.shelf6_btn);
        display_all_btn = (Button) findViewById(R.id.display_all_btn);

        shelfSpinner = (Spinner) findViewById(R.id.shelfSpinner);
    }
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
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_KEY, "");
    }
    
    public void refreshActivity() {
        Intent intent = new Intent(LocationActivity.this, LocationActivity.class);
        startActivity(intent);
    }

    //===========
    private void searchShelf(String s_loc, String pref_key) {
        List<Display> newList = new ArrayList<>();
        for (Display d : displayList) {
            String s1 = d.getShelfLocation();
            if (s1.contains(s_loc)) {
                newList.add(d);
            }
        }

        int listcount = newList.size();
        String scount = String.valueOf(listcount);
        storeShelfCountToSharedPref(pref_key, scount);
        recyclerViewAdapter.setFilter(newList);
    }


    private String getShelfCountFromSharedPref(String pref_key) {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(pref_key, "");
    }

    private void storeShelfCountToSharedPref(String shelf_key, String s_loc_count) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(shelf_key, s_loc_count);
        editor.apply();
    }

    private void checkLocation(String loc) {

        if (isLifestyleSaleCustomShelf(loc)) {
            shelfSpinner.setVisibility(View.VISIBLE);
        } else {
            shelfSpinner.setVisibility(View.GONE);
        }
    }

    private boolean isLifestyleSaleCustomShelf(String loc) {
        if (loc.equals(Constants.LIFESTYLE) || loc.equals(Constants.SALE) || loc.equals(Constants
                .CUSTOM)) {
            return true;
        } else {
            return false;
        }
    }

    private void hideTextView(boolean isHidden) {
        if (isHidden) {
            shelf_count.setVisibility(View.INVISIBLE);
            shelf_count_id.setVisibility(View.INVISIBLE);
            shelf_name_id.setVisibility(View.INVISIBLE);
        } else {
            shelf_count.setVisibility(View.VISIBLE);
            shelf_count_id.setVisibility(View.VISIBLE);
            shelf_name_id.setVisibility(View.VISIBLE);
        }
    }

    //===========
    
    
}
