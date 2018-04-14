package com.example.awesome.opticsdisplay.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.awesome.opticsdisplay.Activities.LocationActivity;
import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.Util.Constants;

import java.util.List;

public class AddItemToDbHelper {
    private static final String TAG = "AddItemToDbHelper";

    private AppCompatActivity ACTIVITY;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText item_name;
    private EditText item_model;
    private EditText item_description;
    private Button saveButton;
    private Button cancelButton;
    private Spinner location_spinner, shelf_location_spinner;
    private TextView searchResultTV;
    private DatabaseHandler db;

    //for popup spinner
    private String location_item, location;
    private String shelf_location_item;

    //Listview variables
    private String[] location_array;
    private String[] shelf_location_array;
    private ListView location_listView;
    private String INTENT_KEY = "locationKey";
    private List<String> location_itemList;
    ListAdapter listAdapter;

    private View view;
    private LayoutInflater inflater;
    private Context context;

    private static final String PREF_NAME = "PrefCount";
    private static final String PREF_KEY = "count";
    private static final String PREF_KEY1 = "s1_count";
    private static final String PREF_KEY2 = "s2_count";
    private static final String PREF_KEY3 = "s3_count";
    private static final String PREF_KEY4 = "s4_count";
    private static final String PREF_KEY5 = "s5_count";

    private Display display;




    //Stuff that needs to be passed in
    /*
    context
    location
     */

    public AddItemToDbHelper(String location, Context context) {
        this.location = location;
        this.context = context;
    }

    public void createPopUpDialogToAddDisplay() {

        initializeThem();
        setSpinnerEntries(location);

        //==== FOR DIALOG POPUP TO SHOW ====//
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        //=== FOR SHELF SPINNER ====//
        shelf_location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shelf_location_item = shelf_location_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ew_name = item_name.getText().toString().trim();
                String ew_desc = item_description.getText().toString().trim();
                String ew_modNum = item_model.getText().toString().trim();

                if (!ew_name.isEmpty() && !ew_desc.isEmpty() && !ew_modNum.isEmpty()) {
                    if (!db.checkModelNumber(ew_modNum)) {// this is to check if there is no double
                                                            //display
                        saveDisplayItemToDB(v, ew_name, ew_desc, ew_modNum);
                        hideKeyboard(v);

                    } else {
                        String item_loc = db.locationOfItem(ew_modNum);
                        String str_output = "Item already exist!\n" + item_loc;

                        Snackbar snackbar = Snackbar.make(v, str_output, Snackbar
                                .LENGTH_INDEFINITE);

                        View snackbar_view = snackbar.getView();

                        //To show multiple lines when snackbar shows up
                        TextView tv = (TextView) snackbar_view.findViewById(android.support
                                .design.R.id.snackbar_text);
                        tv.setMaxLines(5);
                        snackbar.setAction("X", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                } else if (ew_name.isEmpty() && ew_desc.isEmpty() && ew_modNum.isEmpty()) {
                    item_name.setError("Enter Eyewear Name");
                    item_description.setError("Enter Description");
                    item_model.setError("Enter Model#");
                } else if (ew_name.isEmpty()) {
                    item_name.setError("Enter Eyewear Name");
                } else if (ew_desc.isEmpty()) {
                    item_description.setError("Enter Description");
                } else if (ew_modNum.isEmpty()) {
                    item_model.setError("Enter Model#");
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
    

    private void saveDisplayItemToDB(View v, String ew_name, String ew_desc, String ew_modNum) {
        Display display = new Display();
        display.setName(ew_name);
        display.setDescription(ew_desc);
        display.setModel(ew_modNum);
        display.setLocation(location);
        display.setOldLocation(Constants.NOT_APPLICABLE);
        display.setShelfLocation(shelf_location_item);

        db.addDisplay(display);

        //update count in shared pref
        String loc_count = getLocationCountFromSharedPref(PREF_KEY);
        Log.d(TAG, "saveDisplayItemToDB: ===>> oldCount = " + loc_count);
        int _locCount = Integer.valueOf(loc_count);
        _locCount++;
        String updatedCount = String.valueOf(_locCount);
        Log.d(TAG, "saveDisplayItemToDB: ===>> newCount = " + updatedCount);
        updateLocationCountToSharedPref(updatedCount);


        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        //this will delay by 1sec in showing the next activity
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                context.startActivity(new Intent(context, LocationActivity.class));
            }
        }, 1000);
    }


    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void initializeThem() {
        dialogBuilder = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup2, null);
        item_name = (EditText) view.findViewById(R.id.item_name_popup2);
        item_description = (EditText) view.findViewById(R.id.item_description_popup2);
        item_model =  (EditText) view.findViewById(R.id.item_model_popup2);
        saveButton = (Button) view.findViewById(R.id.save_button_popup2);
        cancelButton = (Button) view.findViewById(R.id.cancel_button_popup2);
        shelf_location_spinner = (Spinner) view.findViewById(R.id
                .shelf_location_spinner_popup2);

        shelf_location_array = context.getResources().getStringArray(R.array.shelf_location);
        db = new DatabaseHandler(context);
    }

    private void updateCountToShelfSharedPref(String shelfcount, String shelf_key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(shelf_key, shelfcount);
        editor.apply();
    }

    private String getShelfCountFromSharedPref(String pref_key) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(pref_key, "");
    }

    private void updateLocationCountToSharedPref(String locCount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY, locCount);
        editor.apply();

    }

    private String getLocationCountFromSharedPref(String loc_key) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(loc_key, "");
    }

    private String findShelfNameKey(String shelf) {

        String returnValue = "";
        if (shelf.contains("1")) {
            returnValue = PREF_KEY1;
        } else if (shelf.contains("2")) {
            returnValue = PREF_KEY2;
        } else if (shelf.contains("3")) {
            returnValue = PREF_KEY3;
        } else if (shelf.contains("4")) {
            returnValue =  PREF_KEY4;
        } else if (shelf.contains("5")) {
            returnValue = PREF_KEY5;
        }

        return returnValue;
    }

    //================= TO CHECK IF LIFESTYLE, CUSTOM, SALE =======================//
    private boolean isLifestyleSaleCustomShelf(String loc) {
        if (loc.equals(Constants.LIFESTYLE) || loc.equals(Constants.SALE) || loc.equals(Constants
                .CUSTOM)) {
            return true;
        } else {
            return false;
        }
    }

    /*
    This function will set the spinner Entries to different items depending on the location.
    Since this function is in a non activity class, setting up the ArrayAdapter is done by using
    'createFromResource' instead of ' new ArrayAdapter<>(context, android.R.layout
    .simple_spinner_dropdown_item, list)'
     */
    private void setSpinnerEntries(String location) {

        ArrayAdapter<CharSequence> arrayAdapter;
        if (isLifestyleSaleCustomShelf(location)) {
            arrayAdapter = ArrayAdapter.createFromResource(context, R.array.shelf_location_lcs, android.R.layout
                    .simple_spinner_dropdown_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shelf_location_spinner.setAdapter(arrayAdapter);
            Log.d(TAG, "setSpinnerEntries: ====>>> LCS");
        } else if (!location.equals(Constants.MISSING_STR) && !location.equals(Constants
                .SOLD_STR)) {
            arrayAdapter = ArrayAdapter.createFromResource(context, R.array
                    .s1tos5_shelf_location, android.R.layout.simple_spinner_dropdown_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shelf_location_spinner.setAdapter(arrayAdapter);
        } else {
            arrayAdapter = ArrayAdapter.createFromResource(context, R.array.shelf_location,
                    android.R.layout.simple_spinner_dropdown_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shelf_location_spinner.setAdapter(arrayAdapter);
        }



    }
}
