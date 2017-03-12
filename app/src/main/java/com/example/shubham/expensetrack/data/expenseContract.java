package com.example.shubham.expensetrack.data;

import android.provider.BaseColumns;
import android.net.Uri;

/**
 * Created by Shubham on 07-01-2017.
 */
public final class expenseContract {

    public static final String CONTENT_AUTHORITY = "com.example.shubham.expensetrack";
    public static final Uri BASE_CONTENT= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH = "expensetrack";
    private  expenseContract(){}
    public final static class Entry implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT,PATH);
        public final static String TABLE_NAME = "Expense";
        public final static String _ID= BaseColumns._ID;
        public final static String COLUMN_TYPE = "Type";
        public final static String COLUMN_NAME  = "Name";
        public final static String COLUMN_AMOUNT = "Amount";
        public final static String COLUMN_DATE = "Date";
        public final static String COLUMN_CATEGORY= "Category";

        public final static int WITHDRAWAL = 1;
        public final static int DEPOSIT = 2;
        public final static int NONE = 0;


    }

}
