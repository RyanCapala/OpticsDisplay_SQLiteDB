package com.example.awesome.opticsdisplay.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.awesome.opticsdisplay.Model.Admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awesome on 1/23/18.
 */

public class DBHandlerAdmin extends SQLiteOpenHelper {
    //------------- Constants --------------------//
    // Database Version
    private static final int DATABASE_VERSION = 1111;

    // Database Name
    private static final String DATABASE_NAME = "AdminManager.db";

    // User table name
    private static final String TABLE_NAME_ADMIN = "admin";

    // User Table Columns names
    private static final String COL_ADMIN_ID = "_id";
    private static final String COL_ADMIN_NAME = "adminName";
    private static final String COL_ADMIN_PASSWORD = "adminPassword";

    private String QUERY_STRING_ARRAY[] = {COL_ADMIN_ID, COL_ADMIN_NAME, COL_ADMIN_PASSWORD};
    private String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_NAME_ADMIN + "("
            + COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_ADMIN_NAME + " TEXT,"
            + " TEXT," + COL_ADMIN_PASSWORD + " TEXT" + ")";

    private String DROP_ADMIN_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_ADMIN;

    //------------- Constants --------------------//

    // Constructor
    public DBHandlerAdmin(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_ADMIN_TABLE);
        onCreate(db);
    }

    /**
     * CRUD
     *
     */

    // Add Admin
    public void addAdmin(Admin admin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ADMIN_NAME, admin.get_user_());
        values.put(COL_ADMIN_PASSWORD, admin.get_password_());
        db.insert(TABLE_NAME_ADMIN, null, values);
        db.close();
    }

    // Delete Admin
    public void deleteAdmin(Admin admin) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ADMIN, COL_ADMIN_ID + " =?",
                new String[] {String.valueOf(admin.get_id())});
        db.close();
    }

    // Delete Admin using string name
    public void deleteAdmin(String admin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] userName = {admin};
        Cursor cursor = db.query(TABLE_NAME_ADMIN, QUERY_STRING_ARRAY, COL_ADMIN_NAME + " =?", userName,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Admin userToDelete = new Admin();
        userToDelete.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_ID))));
        userToDelete.set_user_(cursor.getString(cursor.getColumnIndex(COL_ADMIN_NAME)));
        userToDelete.set_password_(cursor.getString(cursor.getColumnIndex(COL_ADMIN_PASSWORD)));
        db.close();
        //now we can delete
        deleteAdmin(userToDelete);

    }

    // Get All Admin
    public List<Admin> getAllAdmin() {
        String[] columns = {COL_ADMIN_ID, COL_ADMIN_NAME, COL_ADMIN_PASSWORD};
        String sortOrder = COL_ADMIN_NAME + " ASC";
        List<Admin> adminList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_ADMIN, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                Admin admin = new Admin();
                admin.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_ID))));
                admin.set_user_(cursor.getString(cursor.getColumnIndex(COL_ADMIN_NAME)));
                admin.set_password_(cursor.getString(cursor.getColumnIndex(COL_ADMIN_PASSWORD)));

                adminList.add(admin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  adminList;
    }

    // Get All user and return a List<String>
    public List<String> getAllAdminNames() {
        List<String> adminNames = new ArrayList<>();
        String adName;
        String[] columns = {COL_ADMIN_ID, COL_ADMIN_NAME};
        String sortorder = COL_ADMIN_NAME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_ADMIN, columns, null, null, null, null, sortorder);
        if (cursor.moveToFirst()) {
            do {
                Admin admin = new Admin();
                admin.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_ID))));
                admin.set_user_(cursor.getString(cursor.getColumnIndex(COL_ADMIN_NAME)));
                adName = admin.get_user_();
                adminNames.add(adName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return adminNames;
    }

    // Check Admin
    public boolean checkAdmin(String username) {
        String[] columns = {COL_ADMIN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_ADMIN_NAME + " =?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME_ADMIN, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    // Check admin user and password
    public boolean checkUser(String username, String password) {
        String[] columns = {COL_ADMIN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_ADMIN_NAME + " =?" + " AND " + COL_ADMIN_PASSWORD + " =?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME_ADMIN, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0){
            return true;
        }

        return false;
    }

    public int getAdminCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME_ADMIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

}
