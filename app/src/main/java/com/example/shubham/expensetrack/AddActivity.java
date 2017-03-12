package com.example.shubham.expensetrack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shubham.expensetrack.data.Helper;
import com.example.shubham.expensetrack.data.expenseContract;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText name;
    private int type;
    private EditText amount;
    private static Button but;
    private static String date;
    private String category;
    public int mBalance;
    public static Cursor cursor;
    public int mWithdrawl;
    public int mDeposit;
    private Spinner typeSpinner;
    private Spinner CatSpinner;
    public static Uri current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Helper mHelper;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        SharedPreferences sharedPrefs = this.getSharedPreferences("Temp", Context.MODE_PRIVATE);
        mBalance = sharedPrefs.getInt("Balance", 0);
        mWithdrawl = sharedPrefs.getInt("Withdrawl", 0);
        mDeposit = sharedPrefs.getInt("Deposit", 0);
        Intent intent = getIntent();
        current = intent.getData();
        if (current == null) {
            setTitle("New Transaction");
            invalidateOptionsMenu();
        } else
            setTitle("Edit Transaction");

        name = (EditText) findViewById(R.id.item);
        amount = (EditText) findViewById(R.id.amount);
        but = (Button) findViewById(R.id.date);
        typeSpinner = (Spinner) findViewById(R.id.spinner_gender);
        setupSpinner();
        CatSpinner = (Spinner) findViewById(R.id.spinner_cat);
        setSpinner();
    }

    private void setSpinner() {

        ArrayAdapter CatSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_cat, android.R.layout.simple_spinner_item);


        CatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        CatSpinner.setAdapter(CatSpinnerAdapter);
        CatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Accommodation")) {
                        category = "Accommodation";
                    } else if (selection.equals("Choose a Category")) {
                        category = "None";
                    } else if (selection.equals("Entertainment")) {
                        category = "Entertainment";
                    } else if (selection.equals("Food")) {
                        category = "Food";
                    } else if (selection.equals("Groceries")) {
                        category = "Groceries";
                    } else if (selection.equals("Household Item")) {
                        category = "Household Item";
                    } else if (selection.equals("Others")) {
                        category = "Others";
                    } else if (selection.equals("Medicare")) {
                        category = "Medicare";
                    } else if (selection.equals("Personal Care")) {
                        category = "Personal Care";
                    } else if (selection.equals("Tax")) {
                        category = "Tax";
                    } else if (selection.equals("Travel")) {
                        category = "Travel";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "NONE"; // Unknown
            }
        });
    }

    private void setupSpinner() {

        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type, android.R.layout.simple_spinner_item);


        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Withdrawal Transaction")) {
                        type = expenseContract.Entry.WITHDRAWAL;
                    } else if (selection.equals("Withdrawal Deposit")) {
                        type = expenseContract.Entry.DEPOSIT;
                    } else {
                        type = expenseContract.Entry.NONE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = expenseContract.Entry.NONE; // Unknown
            }
        });
        Button clickButton = (Button) findViewById(R.id.action_save);
        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add();
                finish();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (current == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void add() {
        String mName = name.getText().toString().trim();
        String mAmount1 = amount.getText().toString().trim();
        int mAmount = 0;
        if (!TextUtils.isEmpty(mAmount1)) {
            mAmount = Integer.parseInt(mAmount1);
        }
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mAmount1)) {
            Toast.makeText(this,"Please Enter a valid NAme or Amount",Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(expenseContract.Entry.COLUMN_TYPE, type);
        values.put(expenseContract.Entry.COLUMN_NAME, mName);
        values.put(expenseContract.Entry.COLUMN_AMOUNT, mAmount);
        values.put(expenseContract.Entry.COLUMN_DATE, date);
        values.put(expenseContract.Entry.COLUMN_CATEGORY, category);

        if (current == null) {
            Uri newUri = getContentResolver().insert(expenseContract.Entry.CONTENT_URI, values);
            Toast.makeText(this, "SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            if (type == 1) {
                mBalance = mBalance - mAmount;
                mWithdrawl = mWithdrawl + mAmount;
            } else {
                mBalance = mBalance + mAmount;
                mDeposit = mDeposit + mAmount;
            }

        } else {
            int Row = getContentResolver().update(current, values, null, null);
            if (Row == -1)
                Toast.makeText(this, "ERROR WITH SAVING", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "DATA ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            if (type == 1) {
                mBalance = mBalance - mAmount;
                mWithdrawl = mWithdrawl + mAmount;
            } else {
                mBalance = mBalance + mAmount;
                mDeposit = mDeposit + mAmount;
            }
        }
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Temp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("Balance", mBalance);
        editor.putInt("Withdrawl", mWithdrawl);
        editor.putInt("Deposit", mDeposit);
        editor.commit();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (month == 0) date = day + " Jan " + year;
            else if (month == 1) date = day + " Feb " + year;
            else if (month == 2) date = day + " Mar " + year;
            else if (month == 3) date = day + " Apr " + year;
            else if (month == 4) date = day + " May " + year;
            else if (month == 5) date = day + " Jun " + year;
            else if (month == 6) date = day + " Jul " + year;
            else if (month == 7) date = day + " Aug " + year;
            else if (month == 8) date = day + " Sept " + year;
            else if (month == 9) date = day + " Oct " + year;
            else if (month == 10) date = day + " Nov " + year;
            else date = day + " Dec " + year;
            but.setText(date);}
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Transaction?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();}
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        String[] projection = {
                expenseContract.Entry._ID,
                expenseContract.Entry.COLUMN_TYPE,
                expenseContract.Entry.COLUMN_AMOUNT};

       /* SQLiteDatabase db = mHelper.getReadableDatabase();
        long id = ContentUris.parseId(current);
        String selection = expenseContract.Entry._ID + " LIKE ?";
        String[] selectionArgs = {"1"};
        cursor = db.query(expenseContract.Entry.TABLE_NAME, projection,selection, selectionArgs,
                null, null,null);
        int  currentamount=cursor.getInt(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_AMOUNT));
        int currenttype = cursor.getInt(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_TYPE));*/

        if (current != null) {
            int rowsDeleted = getContentResolver().delete(current, null, null);
           /* if (currenttype == 1) {
                mBalance = mBalance + currentamount;
                mWithdrawl = mWithdrawl - currentamount;
            } else {
                mBalance = mBalance - currentamount;
                mDeposit = mDeposit - currentamount;
            }*/
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Temp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("Balance", mBalance);
        editor.putInt("Withdrawl", mWithdrawl);
        editor.putInt("Deposit", mDeposit);
        editor.commit();
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deletion Succcessful", Toast.LENGTH_SHORT).show();}
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_save:
                add();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}