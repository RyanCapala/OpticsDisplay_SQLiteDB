package com.example.awesome.opticsdisplay.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.Util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awesome on 1/17/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final String TAG = "RecyclerViewAdapter";
    
    
    //-------------------------
    private Context context;
    private List<Display> displayItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    
    
    public static final String PREF_NAME = "PrefCount";
    public static final String PREF_KEY = "count";
    public static final String PREF_KEY1 = "s1_count";
    public static final String PREF_KEY2 = "s2_count";
    public static final String PREF_KEY3 = "s3_count";
    public static final String PREF_KEY4 = "s4_count";
    public static final String PREF_KEY5 = "s5_count";

    
    private UpdateLocationCountInterface updateLocationCountInterface;
    private UpdateLocationCountInterface updateShelfCountInterface;
    //-------------------------


    public RecyclerViewAdapter(Context context, List<Display> displayItems) {
        this.context = context;
        this.displayItems = displayItems;
    }
    
    //This constructor is used when you are trying to pass some data from the
    //adapter to the location activity
    public RecyclerViewAdapter(Context context, List<Display> displayItems,
                               UpdateLocationCountInterface updateLocationCountInterface,
                               UpdateLocationCountInterface updateShelfCountInterface) {
        this.context = context;
        this.displayItems = displayItems;
        this.updateLocationCountInterface = updateLocationCountInterface;
        this.updateShelfCountInterface = updateShelfCountInterface;
    }
    
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Display display = displayItems.get(position);
        holder.displayItemName.setText(display.getName());
        holder.displayDescription.setText(display.getDescription());
        holder.modelNumber.setText(display.getModel());
        holder.location.setText(display.getLocation());
        holder.oldLocation.setText(display.getOldLocation());
        holder.shelfLocation.setText(display.getShelfLocation());
        holder.dateAdded.setText(display.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    //-------------------- ViewHolder ----------------------//
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //--------------------------------
        public TextView displayItemName;
        public TextView displayDescription;
        public TextView modelNumber;
        public TextView location, oldLocation;
        public TextView dateAdded;
        public TextView shelfLocation;
        public Button editButton;
        public Button deleteButton;
        public int id;
        String spinnerLocation;
        String shelf_loc_item;
        String currLoc;
        String oldLoc;
        
        //--------------------------------
        //Constructor
        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;

            displayItemName = (TextView) view.findViewById(R.id.name);
            displayDescription = (TextView) view.findViewById(R.id.description);
            modelNumber = (TextView) view.findViewById(R.id.modelNumber);
            location = (TextView) view.findViewById(R.id.location);
            oldLocation = (TextView) view.findViewById(R.id.oldLocation);
            shelfLocation = (TextView) view.findViewById(R.id.shelfLocationTV);
            dateAdded = (TextView) view.findViewById(R.id.date);
            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos;
            Display _display;
            switch (v.getId()) {
                case R.id.editButton:
                    pos = getAdapterPosition();
                    _display = displayItems.get(pos);
                    editItem(_display);
                    break;
                case R.id.deleteButton:
                    pos = getAdapterPosition();
                    _display = displayItems.get(pos);
                    deleteItem(_display);
                    break;
            }

        }

        //--------- deleteItem --------------//
        private void deleteItem(final Display display) {

            final String sloc = display.getShelfLocation();
            final String scount = getShelfCountFromSharedPref(findShelfNameKey(sloc));
            final String sname_key = findShelfNameKey(sloc);
            
            //Create alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete items
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteDisplay(display);
                    displayItems.remove(getAdapterPosition());
                    List<Display> dl = new ArrayList<>();
                    notifyItemRemoved(getAdapterPosition());

                    //to set location count in LocationActivity
                    //String loc_count = String.valueOf(displayItems.size());
                    String loc_count = db.getCountStr(display.getLocation());


                    int _scount = Integer.valueOf(scount);
                    String resVal;
                    if (_scount != 0) {
                        _scount -= 1;
                        resVal = String.valueOf(_scount);
                    } else {
                        resVal = "0";
                    }

                    updateCountToSharedPref(loc_count);
                    updateCountToShelfSharedPref(resVal, sname_key);
                    updateLocationCountInterface.updateLocationCount(loc_count);
                    updateShelfCountInterface.updateShelfCount(resVal);
                    dialog.dismiss();
                    

                }
            });

        }
        //--------- end of deleteItem --------------//

        //----------- editItem ---------------//
        private void editItem(final Display display) {

            //==== for spinner
            final String[] loc_array = context.getResources().getStringArray(R.array.location_array);
            final String[] shelf_loc_array = context.getResources().getStringArray(R
                    .array.shelf_location);
            
            final String _location = display.getLocation();
            final String _shelf = display.getShelfLocation();

            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            TextView title = (TextView) view.findViewById(R.id.title_popup);
            final EditText itemName = (EditText) view.findViewById(R.id.item_name_popup);
            final EditText itemDescription = (EditText) view.findViewById(R.id.item_description_popup);
            final EditText itemModel = (EditText) view.findViewById(R.id.item_model_popup);
            Button saveButton = (Button) view.findViewById(R.id.save_button_popup);
            Button cancelButton = (Button) view.findViewById(R.id.cancel_button_popup);
            Spinner location_spinner = (Spinner) view.findViewById(R.id.location_spinner_popup);
            Spinner shelf_location_spinner = (Spinner) view.findViewById(R.id
                    .shelf_location_spinner_popup);

            //------set location of spinner-----------//
            location_spinner.setSelection(getSpinnerIndex(location_spinner, display.getLocation()));

            // assign current location to oldLoc
            currLoc = display.getLocation();
            oldLoc = display.getOldLocation();

            location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerLocation = loc_array[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            
            //----------set shelf location of spinner--------//
            shelf_location_spinner.setSelection(getSpinnerIndex(shelf_location_spinner,
                    display.getShelfLocation()));
            
            shelf_location_spinner.setOnItemSelectedListener(new AdapterView
                    .OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    shelf_loc_item = shelf_loc_array[position];
                }
    
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
        
                }
            });

            //Set title Edit
            title.setText("Edit Item");

            //populate fields
            int pos = getAdapterPosition();
            Display display1 = new Display();
            display1 = displayItems.get(pos);
            itemName.setText(display1.getName());
            itemDescription.setText(display1.getDescription());
            itemModel.setText(display1.getModel());

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            final String sloc = display.getShelfLocation();
            final String scount = getShelfCountFromSharedPref(findShelfNameKey(sloc));
            final String sname_key = findShelfNameKey(sloc);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    //update item
                    display.setName(itemName.getText().toString());
                    display.setDescription(itemDescription.getText().toString());
                    display.setModel(itemModel.getText().toString());
                    display.setLocation(spinnerLocation);
                    display.setShelfLocation(shelf_loc_item);

                    //to set the old location
                    if (isSoldOrMissing(spinnerLocation) && !isSoldOrMissing(currLoc)) {
                        display.setOldLocation(currLoc);
                    } else if (isSoldOrMissing(spinnerLocation) && isSoldOrMissing(currLoc)) {
                        display.setOldLocation(oldLoc);
                    } else if (!isSoldOrMissing(spinnerLocation)) {
                        display.setOldLocation(Constants.NOT_APPLICABLE);
                    }

                    if (!itemName.getText().toString().isEmpty() &&
                            !itemDescription.getText().toString().isEmpty() &&
                            !itemModel.getText().toString().isEmpty()) {

                        db.updateDisplay(display);

                        if (!_location.equals(display.getLocation())) {
                            displayItems.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            //String loc_count = String.valueOf(db.getCountStr());
                            String loc_count = db.getCountStr(currLoc);

//                            Log.d(TAG, "onClick: <<+++>>> Count = " + db.getCountStr(currLoc));

                            updateCountToSharedPref(loc_count);
                            updateLocationCountInterface.updateLocationCount(loc_count);

                            int _scount = 0;

                            if (!scount.isEmpty()) {
                                _scount = Integer.valueOf(scount);
                            }

                            String resultValue;
                            if (_scount != 0) {
                                _scount -= 1;
                                resultValue = String.valueOf(_scount);
                            } else {
                                resultValue = "0";
                            }

//                            Log.d(TAG, "onClick: <<+++>>> resultValue = " + resultValue);

                            updateCountToShelfSharedPref(resultValue, sname_key);
                            updateShelfCountInterface.updateShelfCount(resultValue);
                        } else if (!_shelf.equals(display.getShelfLocation()) && _location.equals
                                (display.getLocation())) {
                            displayItems.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
//                            String loc_count = String.valueOf(displayItems.size());
//                            updateCountToSharedPref(loc_count);
//                            updateLocationCountInterface.updateLocationCount(loc_count);

                            int _scount = 0;
                            if (!scount.isEmpty()) {
                                _scount = Integer.valueOf(scount);
                            }

                            String resultValue;
                            if (_scount != 0) {
                                _scount -= 1;
                                resultValue = String.valueOf(_scount);
                            } else {
                                resultValue = "0";
                            }

//                            Log.d(TAG, "onClick: elif <<+++>>> resultValue = " + resultValue);

                            updateCountToShelfSharedPref(resultValue, sname_key);
                            updateShelfCountInterface.updateShelfCount(resultValue);

                        }
                        notifyItemChanged(getAdapterPosition(), display);
                    } else {
                        Snackbar.make(view, "Fill Empty Field(s)", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
        //----------- editItem ---------------//

    }
    //--------------------end of ViewHolder ----------------------//

    //==== a function that will return the position of the string from the spinner.
    private int getSpinnerIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    //-------------------------------------------//
    // function used for the searchview
    public void setFilter(List<Display> newList){
        displayItems = new ArrayList<>();
        displayItems.addAll(newList);
        notifyDataSetChanged();
    }
    
    private void updateCountToSharedPref(String locCount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY, locCount);
        editor.apply();
        
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

    //---------------------------------------------//
    //This is used to trigger the the setting of the updated count when you delete an item
    public interface UpdateLocationCountInterface {
        //The string being passed when this is invoke is just to check the data
        //You can also use the data to whatever you want
        void updateLocationCount(String str);

        void updateShelfCount(String count);
    }

    private boolean isSoldOrMissing(String str) {
        if (str.equals(Constants.MISSING_STR) || str.equals(Constants.SOLD_STR)) {
            return true;
        } else {
            return false;
        }
    }


}
