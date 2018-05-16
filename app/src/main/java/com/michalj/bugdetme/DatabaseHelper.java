package com.michalj.bugdetme;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Expanses";

    // Table columns
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";

    // Shared preferences
    public static final String SHARED_PREFERENCES_NAME = "Budged prefs";
    public static final String MONTHLY_BUDGET = "Monthly budget";
    public static final String SAVINGS_GOAL = "Savings goal";


    // Types of expenses
    public static final ArrayList<String> TYPES_OF_EXPENSES = new ArrayList<>(Arrays.asList("FMCG", "Utilities", "Car", "Kids",
            "Leisure", "Health", "Clothes"));

    // Database Information
    static final String DB_NAME = "BUDGET.DB";
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