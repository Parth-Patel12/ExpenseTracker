package com.example.shubham.expensetrack.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

public class expenseProvider extends ContentProvider {

    public static final String LOG_TAG = expenseProvider.class.getSimpleName();
    private static final int EXPENSE = 100;
    private static final int EXPENSE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(expenseContract.CONTENT_AUTHORITY, expenseContract.PATH, EXPENSE);
        sUriMatcher.addURI(expenseContract.CONTENT_AUTHORITY, expenseContract.PATH + "/#", EXPENSE_ID);
    }

    private Helper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new Helper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;
        sortOrder = "date("+expenseContract.Entry.COLUMN_DATE + ") ASC";
        int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE:
                cursor = db.query(expenseContract.Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EXPENSE_ID:
                selection = expenseContract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(expenseContract.Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case EXPENSE :
                insertinto(uri,contentValues);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return null;
    }
    private Uri insertinto(Uri uri, ContentValues contentValues)
    {
        String name = contentValues.getAsString(expenseContract.Entry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(expenseContract.Entry.TABLE_NAME, null, contentValues);
         if(id==-1)
            Toast.makeText(getContext(),"ROW IS NOT ADDED", Toast.LENGTH_SHORT).show();

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case EXPENSE_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = expenseContract.Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
          if (values.size() == 0) {
            return 0;
        }
         SQLiteDatabase database = mHelper.getWritableDatabase();
        return database.update(expenseContract.Entry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE:
                // Delete all rows that match the selection and selection args
                return database.delete(expenseContract.Entry.TABLE_NAME, selection, selectionArgs);
            case EXPENSE_ID:
                // Delete a single row given by the ID in the URI
                selection = expenseContract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(expenseContract.Entry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }
}