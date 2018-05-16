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

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.AMOUNT, DatabaseHelper.TYPE, DatabaseHelper.DESCRIPTION};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns,
                null, null, null, null, "date DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor expensesInCurrentMonth() {
        return database.rawQuery("SELECT sum(amount) FROM " + DatabaseHelper.TABLE_NAME,null);
    }

    public Cursor typeSumCurrentMonth(String expenseType) {
        return database.rawQuery("SELECT COALESCE(sum(" + DatabaseHelper.AMOUNT + "),0) FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.TYPE + " = " + "'"+expenseType+"'", null);
    }


//    public int update(long _id, String name, String desc) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.SUBJECT, name);
//        contentValues.put(DatabaseHelper.DESC, desc);
//        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
//        return i;
//    }
//
//    public void delete(long _id) {
//        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
//    }

}
