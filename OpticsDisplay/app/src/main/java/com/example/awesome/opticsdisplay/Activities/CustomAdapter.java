package com.example.awesome.opticsdisplay.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.awesome.opticsdisplay.R;

import java.util.List;

/**
 * Created by awesome on 1/17/18.
 */

class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, List<String> loc_array) {
        super(context, R.layout.row_item1, loc_array);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.row_item1, parent, false);

        String loc_items = getItem(position);
        TextView location_name = (TextView) view.findViewById(R.id.location_name);

        location_name.setText(loc_items);
        return view;
    }
}


/*
// Original
class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, String[] loc_array) {
        super(context, R.layout.row_item1, loc_array);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.row_item1, parent, false);

        String loc_items = getItem(position);
        TextView location_name = (TextView) view.findViewById(R.id.location_name);
        TextView location_count = (TextView) view.findViewById(R.id.location_count);

        location_name.setText(loc_items);
        return view;
    }
}

 */
