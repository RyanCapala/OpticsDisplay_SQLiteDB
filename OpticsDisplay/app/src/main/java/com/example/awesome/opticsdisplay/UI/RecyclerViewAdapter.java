package com.example.awesome.opticsdisplay.UI;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.example.awesome.opticsdisplay.Data.DatabaseHandler;
import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awesome on 1/17/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final String TAG = "RecyclerViewAdapter";
    //-------------------------
    Context context;
    private List<Display> displayItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    //-------------------------


    public RecyclerViewAdapter(Context context, List<Display> displayItems) {
        this.context = context;
        this.displayItems = displayItems;
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
        public TextView location;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;
        String loc_item;


        //--------------------------------

        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;

            displayItemName = (TextView) view.findViewById(R.id.name);
            displayDescription = (TextView) view.findViewById(R.id.description);
            modelNumber = (TextView) view.findViewById(R.id.modelNumber);
            location = (TextView) view.findViewById(R.id.location);
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
            //Create alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

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
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();

                    //to set location count in LocationActivity
                    String temp = String.valueOf(displayItems.size());
                    Toast.makeText(context, "TOTAL COUNT: " + temp, Toast.LENGTH_LONG).show();

                }
            });

        }
        //--------- end of deleteItem --------------//

        //----------- editItem ---------------//
        private void editItem(final Display display) {
            //==== for spinner
            final String[] loc_array = context.getResources().getStringArray(R.array.location_array);

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

            //set location of spinner
            location_spinner.setSelection(getSpinnerIndex(location_spinner, display.getLocation()));

            location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    loc_item = loc_array[position];
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

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    //update item
                    display.setName(itemName.getText().toString());
                    display.setDescription(itemDescription.getText().toString());
                    display.setModel(itemModel.getText().toString());
                    display.setLocation(loc_item);

                    if (!itemName.getText().toString().isEmpty() &&
                            !itemDescription.getText().toString().isEmpty() &&
                            !itemModel.getText().toString().isEmpty()) {
                        db.updateDisplay(display);
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


}
