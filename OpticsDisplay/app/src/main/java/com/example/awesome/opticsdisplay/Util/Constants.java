package com.example.awesome.opticsdisplay.Util;

/**
 * Created by awesome on 1/17/18.
 */

public class Constants {

    public static final int DB_VERSION = 111;
    public static final String DB_NAME = "opticsDisplayDB";
    public static final String TABLE_NAME = "displayTable";

    //Table Columns
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "item_name";
    public static final String KEY_DESCRIPTION = "item_description";
    public static final String KEY_MODEL_NUMBER = "model_number";
    public static final String KEY_LOCATION = "item_location";
    public static final String KEY_DATE_ADDED = "date_added";


    //String Array
    public static final String[] QUERY_STRING_ARRAY =
            {
                    KEY_ID,
                    KEY_NAME,
                    KEY_DESCRIPTION,
                    KEY_MODEL_NUMBER,
                    KEY_LOCATION,
                    KEY_DATE_ADDED
            };

}
