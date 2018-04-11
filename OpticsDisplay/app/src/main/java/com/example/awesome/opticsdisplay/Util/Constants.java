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
    public static final String KEY_SHELF_LOCTION = "item_shelf_location";
    public static final String KEY_OLD_LOCATION = "old_location";
    public static final String KEY_DATE_ADDED = "date_added";

    public static final String MISSING_STR = "** MISSING **";
    public static final String SOLD_STR = "** SOLD **";
    public static final String NOT_APPLICABLE = "N/A";

    public static final String ADMIN_NAME_KEY = "adminNameKey";
    public static final String ADMIN_PASS_KEY = "adminPassKey";
    public static final String SUCCESS_MSG = "Registration Succesful!";
    public static final String PIN_MATCH_ERR = "Pin doesn't match";
    public static final String USER_EXIST_ERR = "User already exist";
    public static final String EMPTY_FIELDS_ERR = "Fields Empty";
    public static final String NO_MATCH = "No Match!\nCheck the 'Model#' you entered.";
    public static final String SHELF1 = "S1";
    public static final String SHELF2 = "S2";
    public static final String SHELF3 = "S3";
    public static final String SHELF4 = "S4";
    public static final String SHELF5 = "S5";

    public static final String LIFESTYLE = "LIFESTYLE WALL";
    public static final String CUSTOM = "CUSTOM WALL";
    public static final String SALE = "SALE WALL";


    //String Array
    public static final String[] QUERY_STRING_ARRAY =
            {
                    KEY_ID,
                    KEY_NAME,
                    KEY_DESCRIPTION,
                    KEY_MODEL_NUMBER,
                    KEY_LOCATION,
                    KEY_SHELF_LOCTION,
                    KEY_OLD_LOCATION,
                    KEY_DATE_ADDED
            };

}
