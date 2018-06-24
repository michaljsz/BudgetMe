package com.michalj.bugdetme;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;
    private final Context context;
    private SQLiteDatabase database;
    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String date, int amount, String type, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.AMOUNT, amount);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.DESCRIPTION, desc);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void insertCarData(int id, String date, int amount, int mileage,double fuel, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._CAR_ID, id);
        contentValue.put(DatabaseHelper.CAR_DATE, date);
        contentValue.put(DatabaseHelper.CAR_AMOUNT, amount);
        contentValue.put(DatabaseHelper.MILEAGE, mileage);
        contentValue.put(DatabaseHelper.FUEL_VOLUME, fuel);
        contentValue.put(DatabaseHelper.CAR_DESCRIPTION, desc);
        database.insert(DatabaseHelper.CAR_TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.AMOUNT, DatabaseHelper.TYPE, DatabaseHelper.DESCRIPTION};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns,
                null, null, null, null, "date DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getLastId() {
        return database.rawQuery("SELECT MAX(" + DatabaseHelper._ID + ") FROM " + DatabaseHelper.TABLE_NAME,null);
    }

    public Cursor expensesInCurrentMonth() {
        return database.rawQuery("SELECT sum(amount) FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  " +
                "strftime('%m',date) = strftime('%m',date('now'))",null);
    }

    public Cursor expensesInMonth(String year, String month) {
        return database.rawQuery("SELECT sum(amount) FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%Y',date) = '" + year + "' AND  strftime('%m',date) = '" +
                month + "'",null);
    }

    public Cursor expensesInCurrentMonthByType(String expenseType) {
        return database.rawQuery("SELECT COALESCE(sum(" + DatabaseHelper.AMOUNT + "),0) FROM " +
                DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.TYPE + " = " + "'"+expenseType+"' " +
                "AND strftime('%Y',date) = strftime('%Y',date('now')) AND " +
                "strftime('%m',date) = strftime('%m',date('now'))", null);
    }

    public Cursor mileage() {
        return database.rawQuery("SELECT " + DatabaseHelper.MILEAGE + " FROM " +
                DatabaseHelper.CAR_TABLE_NAME + " ORDER BY " + DatabaseHelper._CAR_ID +
                " DESC LIMIT 1", null);
    }

    public Cursor burntFuel() {
        return database.rawQuery("SELECT sum(" + DatabaseHelper.FUEL_VOLUME + ") FROM " +
                DatabaseHelper.CAR_TABLE_NAME, null);
    }


    public int update(long _id, String date, double amount, String type, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.AMOUNT, amount);
        contentValues.put(DatabaseHelper.TYPE, type);
        contentValues.put(DatabaseHelper.DESCRIPTION, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
