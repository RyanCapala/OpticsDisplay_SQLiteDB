package com.example.awesome.opticsdisplay.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.awesome.opticsdisplay.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awesome on 1/21/18.
 */

public class DatabaseHelperUser extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //
    private String QUERY_STRING_ARRAY[] = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD};

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelperUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop table if exists
        db.execSQL(DROP_USER_TABLE);
        //create table again
        onCreate(db);
    }

    /**
     * CRUD
     *
     */

    // Add user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.get_user());
        values.put(COLUMN_USER_PASSWORD, user.get_password());

        //insert row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Get all user
    public List<User> getAllUser() {
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD};
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, columns, null,null,null,null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.set_user(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.set_password(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));

                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    //======================
    // Get all users and return a String[]
    public List<String> getAllUserNames(){
        List<String> usernames = new ArrayList<>();
        String uName;
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME};
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.set_user(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                uName = user.get_user();
                usernames.add(uName);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return usernames;
    }
    //======================

    // Update User
    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.get_user());
        values.put(COLUMN_USER_PASSWORD, user.get_password());

        db.update(TABLE_USER, values, COLUMN_USER_ID + "=?", new String[] {
                String.valueOf(user.get_id())});
        db.close();
    }

    // Delete User
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + "=?",
                new String[] {String.valueOf(user.get_id())});
        db.close();
    }

    //===============================================
    //Delete User using with string input;
    public void deleteUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] userName = {user};
        Cursor cursor = db.query(TABLE_USER, QUERY_STRING_ARRAY, COLUMN_USER_NAME + " =?", userName,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User userToDelete = new User();
        userToDelete.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
        userToDelete.set_user(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
        userToDelete.set_password(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        db.close();
        //now we can delete
        deleteUser(userToDelete);

    }
    //===============================================

    // Check User
    public boolean checkUser(String username) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " =?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;

    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " =?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0){
            return true;
        }

        return false;
    }
}
