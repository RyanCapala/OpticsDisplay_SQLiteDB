package com.example.awesome.opticsdisplay.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.Util.Constants;
import com.example.awesome.opticsdisplay.Util.Session;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private AppCompatActivity ACTIVITY = MainActivity.this;

    private AlertDialog.Builder dialogBuilder, dialogBuilder2;
    private AlertDialog dialog, dialog2;
    private EditText item_name;
    private EditText item_model, item_model2;
    private EditText item_description;
    private Button saveButton, searchButton;
    private Button cancelButton, cancelButton2, clearBtn;
    private Spinner location_spinner, shelf_location_spinner;
    private TextView searchResultTV;
    private DatabaseHandler db, db2;

    //for popup spinner
    private String location_item;
    private String shelf_location_item;

    //Listview variables
    private String[] location_array;
    private String[] shelf_location_array;
    private ListView location_listView;
    private String INTENT_KEY = "locationKey";
    private List<String> location_itemList;
    ListAdapter listAdapter;
    
    //private String INTENT_KEY_UNAME = "username";
    private TextView usernameTV;
    private String _uname = "";
    //public static final String UNAME_KEY = "userName";
    public static final String PREF_NAME = "Preferences";
    public static final String PREF_KEY = "text";
    
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        session = new Session(this);
        if (!session.checkIfUserIsLoggedIn()) {
            logoutUser();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        _uname = sharedPreferences.getString(PREF_KEY, "");
        
        
        usernameTV = (TextView) findViewById(R.id.userLoggedInTV);
        String str = "User:\t\t" + firstLetterToUpper(_uname);
        usernameTV.setText(str);
        

        db = new DatabaseHandler(this);
        db2 = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                createPopupDialog();

            }
        });


        //---for listView---//
        location_array = getResources().getStringArray(R.array.location_array);
        location_listView = (ListView)findViewById(R.id.location_listView);

        shelf_location_array = getResources().getStringArray(R.array.shelf_location);


        //----------------- custom listview -----------------//
        getCountInEachLocation();
        listAdapter = new CustomAdapter(this, location_itemList);
        location_listView.setAdapter(listAdapter);
        //----------------- custom listview -----------------//

        location_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                intent.putExtra(INTENT_KEY, location_array[position]);
                startActivity(intent);
            }
        });
        //---for listView---//


    }//End of onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_main).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(this);
//        //--- To change the cursor color of the search menu ---//
//        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById
//                (android.support.v7.appcompat.R.id.search_src_text);
//        try {
//            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
//            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(searchTextView, R.drawable.cursor_color);
//        } catch (Exception e) {
//
//        }
//        //------------------------------------------------------//
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch (id) {
            case R.id.createdBy_info_menu:
                Snackbar.make(getCurrentFocus(), "THE AWESOME NAYR!!", Snackbar.LENGTH_LONG)
                        .show();
                break;
                
            case R.id.sign_out_menu:
                logoutUser();
                break;

            case R.id.search_menu_main:
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                createPopupDialog2();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        return false;
//    }

    private void createPopupDialog2() {
        dialogBuilder2 = new AlertDialog.Builder(this);
        View view2 = getLayoutInflater().inflate(R.layout.search_popup, null);
        item_model2 = (EditText) view2.findViewById(R.id.item_model_search_popup);
        searchResultTV = (TextView) view2.findViewById(R.id.search_result_TV_popup);
        searchButton = (Button) view2.findViewById(R.id.search_button_search_popup);
        cancelButton2 = (Button) view2.findViewById(R.id.cancel_button_search_popup);
        clearBtn = (Button) view2.findViewById(R.id.clear_search_button);

        dialogBuilder2.setView(view2);
        dialog2 = dialogBuilder2.create();
        dialog2.show();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modelNumber = item_model2.getText().toString().trim();
                if (!modelNumber.isEmpty()) {
                    String foundItem = db2.locationOfItem2(modelNumber);
                    searchResultTV.setText(foundItem);
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_model2.setText(null);
                searchResultTV.setText(null);
            }
        });

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

    }

    //---------- createPopupDialog -------------//
    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        item_name = (EditText) view.findViewById(R.id.item_name_popup);
        item_description = (EditText) view.findViewById(R.id.item_description_popup);
        item_model =  (EditText) view.findViewById(R.id.item_model_popup);
        saveButton = (Button) view.findViewById(R.id.save_button_popup);
        cancelButton = (Button) view.findViewById(R.id.cancel_button_popup);
        location_spinner = (Spinner) view.findViewById(R.id.location_spinner_popup);
        shelf_location_spinner = (Spinner) view.findViewById(R.id
                .shelf_location_spinner_popup);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        //---- for spinner --------//
        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location_item = location_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        shelf_location_spinner.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shelf_location_item = shelf_location_array[position];
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
        //---- end of for spinner --------//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item_name.getText().toString().isEmpty() &&
                        !item_description.getText().toString().isEmpty() &&
                        !item_model.getText().toString().isEmpty()){
                    String modNum = item_model.getText().toString().trim();

                    if (!db.checkModelNumber(modNum)) {
                        saveDisplayToDB(v);
                    } else {
                        String itemLocation = db.locationOfItem(modNum);
                        String output = itemLocation + "\nAlready Exist!!";
                        //This will allow the Snackbar to stay open until
                        //the 'x' button is clicked.
                        Snackbar.make(v, output, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                    }


                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    //---------- end of createPopupDialog -------------//

    //---------- saveDisplayToDB -----------//
    private void saveDisplayToDB(View v) {
        final Display display = new Display();
        String newDisplay_item_name = item_name.getText().toString();
        String newDisplay_item_description = item_description.getText().toString();
        String newDisplay_item_model = item_model.getText().toString();

        display.setName(newDisplay_item_name);
        display.setDescription(newDisplay_item_description);
        display.setModel(newDisplay_item_model);
        display.setLocation(location_item);
        display.setOldLocation(Constants.NOT_APPLICABLE);
        display.setShelfLocation(shelf_location_item);

        //save to DB
        db.addDisplay(display);
        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        //this will delay by 1sec in showing the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        }, 1000);

    }
    //---------- end of saveDisplayToDB -----------//


    //---------------------------------------
    private void getCountInEachLocation() {

        location_itemList = new ArrayList<>();
        db = new DatabaseHandler(this);
        List<Display> displayList = new ArrayList<>();
        int len_of_array = location_array.length;

        for (int i = 0; i < len_of_array; i++) {
            displayList = db.getListByLocation(location_array[i]);

            String count;
            if (displayList.size() == 0) {
                count = "0";
            } else {
                count = String.valueOf(displayList.size());
            }

            String item = location_array[i] + "\nCount:\t\t" + count;
            location_itemList.add(item);


        }

    }
    //--------------------------------------------
    
    private String firstLetterToUpper(String str) {
        String convertedString = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        return convertedString;
    }
    
    //--------------------------------------------
    private void logoutUser() {
        session.setUserLoggedIn(false);
        startActivity(new Intent(ACTIVITY, LoginActivity.class));
        finish();
    }



}
