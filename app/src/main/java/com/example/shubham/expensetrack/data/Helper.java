package com.example.shubham.expensetrack.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import com.example.shubham.expensetrack.data.expenseContract.Entry;

public class Helper extends SQLiteOpenHelper {

    public static final String LOG_TAG = Helper.class.getSimpleName();
    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;
    public Helper(Context context) {
               super(context, DATABASE_NAME, null, DATABASE_VERSION);
           }
    @Override
       public void onCreate(SQLiteDatabase db) {
                String CREATE_TABLE = "CREATE TABLE " + Entry.TABLE_NAME + "("
                         + Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Entry.COLUMN_TYPE + " INTEGER NOT NULL DEFAULT 1,"
                         + Entry.COLUMN_NAME + " TEXT NOT NULL," + Entry.COLUMN_AMOUNT + " INTEGER,"
                         + Entry.COLUMN_DATE + " BIGINT NOT NULL, "
                         + Entry.COLUMN_CATEGORY + " TEXT NOT NULL );" ;
        db.execSQL(CREATE_TABLE);
    }
    @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
 }
