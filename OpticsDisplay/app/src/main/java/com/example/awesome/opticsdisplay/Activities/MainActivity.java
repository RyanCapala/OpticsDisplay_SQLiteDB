package com.example.awesome.opticsdisplay.Activities;

import android.content.Intent;
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

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText item_name;
    private EditText item_model;
    private EditText item_description;
    private Button saveButton;
    private Button cancelButton;
    private Spinner location_spinner;
    private DatabaseHandler db;

    //for popup spinner
    private String location_item;

    //Listview variables
    private String[] location_array;
    private ListView location_listView;
    private String INTENT_KEY = "locationKey";
    private List<String> location_itemList;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                createPopupDialog();

            }
        });


        //---for listView---//
        location_array = getResources().getStringArray(R.array.location_array);
        location_listView = (ListView)findViewById(R.id.location_listView);


        //---------------- non custom listview -----------------//
        //ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, location_array);
        //arrayList = new ArrayList<>(Arrays.asList(location_array));
        //arrayAdapter = new ArrayAdapter<String>(this, R.layout.row_item1, R.id.location_name, arrayList);
        //location_listView.setAdapter(arrayAdapter);
        //---------------- non custom listview -----------------//

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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createdBy_info_menu) {
            Snackbar.make(getCurrentFocus(),
                    "None other than,\n Your Awesomeness!!", Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        //---- end of for spinner --------//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item_name.getText().toString().isEmpty() &&
                        !item_description.getText().toString().isEmpty() &&
                        !item_model.getText().toString().isEmpty()){

                    saveDisplayToDB(v);

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

            String item = location_array[i] + "\nCount: " + count;
            location_itemList.add(item);


        }

    }
    //--------------------------------------------



}
