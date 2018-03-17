package com.example.awesome.opticsdisplay.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.awesome.opticsdisplay.Model.Display;
import com.example.awesome.opticsdisplay.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by awesome on 1/17/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHandler";

    private Context ctx;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    //---------- onCreate -------------//
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DISPLAY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.KEY_NAME + " VARCHAR, "
                + Constants.KEY_DESCRIPTION + " VARCHAR, "
                + Constants.KEY_MODEL_NUMBER + " VARCHAR, "
                + Constants.KEY_LOCATION + " VARCHAR, "
                + Constants.KEY_SHELF_LOCTION + " VARCHAR, "
                + Constants.KEY_OLD_LOCATION + " VARCHAR, "
                + Constants.KEY_DATE_ADDED + " LONG)";

        db.execSQL(CREATE_DISPLAY_TABLE);

    }

    //---------- onUpgrade -----------//
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /*************************************
     *                CRUD
     *************************************/

    //----------- addDisplay --------------//
    public void addDisplay(Display display){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME, display.getName());
        values.put(Constants.KEY_DESCRIPTION, display.getDescription());
        values.put(Constants.KEY_MODEL_NUMBER, display.getModel());
        values.put(Constants.KEY_LOCATION, display.getLocation());
        values.put(Constants.KEY_SHELF_LOCTION, display.getShelfLocation());
        values.put(Constants.KEY_OLD_LOCATION, display.getOldLocation());
        values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());

        //Insert the row
        //====
        String n,m,i;
        n = display.getName();
        m = display.getModel();
        i = String.valueOf(display.getId());
        //====
        db.insert(Constants.TABLE_NAME, null, values);
        Log.d(TAG, "addDisplay: SAVED..... Name: " + n + " Model: " + m + " ID: " + i);
        db.close();

    }
    //----------- end of addDisplay --------------//

    //------------ getDisplay --------------//
    public Display getDisplay(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, Constants.QUERY_STRING_ARRAY,
                Constants.KEY_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        //set
        Display display = new Display();
        display.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        display.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
        display.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
        display.setModel(cursor.getString(cursor.getColumnIndex(Constants.KEY_MODEL_NUMBER)));
        display.setLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_LOCATION)));
        display.setOldLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_OLD_LOCATION)));
        display.setShelfLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_SHELF_LOCTION)));

        //convert timestamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)))
                .getTime());
        display.setDateItemAdded(formattedDate);

        return display;
    }
    //------------ end of getDisplay --------------//

    //------------ getAllDisplay --------------//
    public List<Display> getAllDisplay(){
        List<Display> displayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, Constants.QUERY_STRING_ARRAY,
                null, null, null, null, Constants.KEY_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {
                Display display = new Display();

                //set
                display.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                display.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
                display.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                display.setModel(cursor.getString(cursor.getColumnIndex(Constants.KEY_MODEL_NUMBER)));
                display.setLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_LOCATION)));
                display.setOldLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_OLD_LOCATION)));
                display.setShelfLocation(cursor.getString(cursor.getColumnIndex
                        (Constants.KEY_SHELF_LOCTION)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)))
                        .getTime());
                display.setDateItemAdded(formattedDate);

                //Add to displayList
                displayList.add(display);

            } while (cursor.moveToNext());
        }

        return displayList;
    }
    //------------ end of getAllDisplay --------------//

    //------------ updateDisplay --------------//
    public int updateDisplay(Display display){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME, display.getName());
        values.put(Constants.KEY_DESCRIPTION, display.getDescription());
        values.put(Constants.KEY_MODEL_NUMBER, display.getModel());
        values.put(Constants.KEY_LOCATION, display.getLocation());
        values.put(Constants.KEY_SHELF_LOCTION, display.getShelfLocation());
        values.put(Constants.KEY_OLD_LOCATION, display.getOldLocation());
        values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());

        //Update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(display.getId())});
    }
    //------------ end of updateDisplay --------------//

    //------------ deleteDisplay ---------------//
    public void deleteDisplay(Display display){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(display.getId())});

        db.close();
    }
    //------------ end of deleteDisplay ---------------//

    //------------- getDisplayCount ---------------//
    public int getDisplayCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
    //------------- end of getDisplayCount ---------------//

    //------------- getListItems -----------------//
    // This will return a string of items by location
    public String getListItems(String loc){
        StringBuffer stringBuffer = new StringBuffer();
        SQLiteDatabase db = this.getReadableDatabase();

        //====
        String[] col = {Constants.KEY_ID};
        String selection = Constants.KEY_LOCATION + " LIKE ?";
        String[] selection_args = {loc};
        Cursor cursor = db.query(Constants.TABLE_NAME, Constants.QUERY_STRING_ARRAY,
                selection, selection_args, null, null, null);
        //====
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            //==== converting timestamp to something readable ====//
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong
                    (cursor.getColumnIndex(Constants.KEY_DATE_ADDED))));
            //==== converting timestamp to something readable ====//

            stringBuffer.append(name + "\n");
        }

        return stringBuffer.toString();
    }
    //------------- end of getListItems -----------------//

    //------------- getItemsByLocation ----------------//
    // function to get an item by location, and return the items
    // in a string array
    public String[] getItemsByLocation(String loc){
        String [] list = new String[]{};
        ArrayList<String> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //====
        String[] col = {Constants.KEY_ID};
        String selection = Constants.KEY_LOCATION + " LIKE ?";
        String[] selection_args = {loc};
        Cursor cursor = db.query(Constants.TABLE_NAME, Constants.QUERY_STRING_ARRAY,
                selection, selection_args, null, null, null);
        //====

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            //==== converting timestamp to something readable ====//
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong
                    (cursor.getColumnIndex(Constants.KEY_DATE_ADDED))));
            //==== converting timestamp to something readable ====//

            arrayList.add(name);
        }

        return arrayList.toArray(new String[0]);
    }
    //------------- end of getItemsByLocation ----------------//

    //------------- getListByLocation ------------------//
    //function that will return a List<Display> with the same location
    public List<Display> getListByLocation(String loc){
        List<Display> displayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = Constants.KEY_LOCATION + " LIKE ?";
        String[] selection_args = {loc};

        Cursor cursor = db.query(Constants.TABLE_NAME, Constants.QUERY_STRING_ARRAY,
                selection, selection_args, null, null, Constants.KEY_NAME + " ASC");

        while (cursor.moveToNext()) {
            Display display = new Display();

            //set
            display.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            display.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
            display.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
            display.setModel(cursor.getString(cursor.getColumnIndex(Constants.KEY_MODEL_NUMBER)));
            display.setLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_LOCATION)));
            display.setOldLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_OLD_LOCATION)));
            display.setShelfLocation(cursor.getString(cursor.getColumnIndex(Constants.KEY_SHELF_LOCTION)));

            //convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)))
                    .getTime());
            display.setDateItemAdded(formattedDate);

            //add to array
            displayList.add(display);

        }

        return displayList;
    }
    //------------- end of getListByLocation ------------------//


}//--- END OF DATABASEHANDLER ---//
