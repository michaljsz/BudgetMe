package com.michalj.bugdetme;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Expanses";

    // Table columns
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";

    // Database Information
    static final String DB_NAME = "BUDGET.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATE + " TEXT, " + AMOUNT + " INTEGER, " + TYPE + " TEXT, " + DESCRIPTION + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}