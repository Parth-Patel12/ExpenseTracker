package com.example.shubham.expensetrack;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.expensetrack.data.CustomAdapter;
import com.example.shubham.expensetrack.data.Weather;
import com.example.shubham.expensetrack.data.expenseContract;

public class CatalogActivity extends AppCompatActivity {
    public static TextView tv;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.catalog_activity);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Weather weather_data[] = new Weather[]
                {
                        new Weather(R.drawable.database_plus, "Deposits only"),
                        new Weather(R.drawable.database_minus, "Withdrawls only"),
                        new Weather(R.drawable.calendar, "Current Month Reports"),
                        new Weather(R.drawable.delete_2, "DELETE ALL RECORDS"),

                };
        CustomAdapter adapter = new CustomAdapter(this,
                R.layout.drawer_list, weather_data);

        mActivityTitle = getTitle().toString();
        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(CatalogActivity.this, Deposits.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CatalogActivity.this, Withdrawals.class);
                        startActivity(intent1);
                        break;
                    case 3:
                        showDeleteConfirmationDialog();
                        break;

                }
            }
        });
        setupDrawer();    //SETS UP THE DRAWER

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("MENU");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void showWithdrawal(View v) {
        Intent intent = new Intent(v.getContext(), Withdrawals.class);
        startActivity(intent);
    }

    public void Deposits(View v) {
        Intent intent = new Intent(v.getContext(), Deposits.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Temp", Context.MODE_PRIVATE);
        int counter = sharedPrefs.getInt("Balance", 0);
        displayDatabaseInfo();
        tv = (TextView) findViewById(R.id.balance);
        if (counter < 0) {
            tv.setTextColor(Color.parseColor("#f62929"));
        } else {
            tv.setTextColor(Color.parseColor("#7CB342"));
        }
        tv.setText(" " + counter);
    }

    private void displayDatabaseInfo() {

        String[] projection = {
                expenseContract.Entry._ID,
                expenseContract.Entry.COLUMN_TYPE,
                expenseContract.Entry.COLUMN_NAME,
                expenseContract.Entry.COLUMN_AMOUNT,
                expenseContract.Entry.COLUMN_DATE,
                expenseContract.Entry.COLUMN_CATEGORY};
        /*  OLDER DIRECT CALL METHOD(WRONG PRACTICE)
                Cursor cursor = db.query(
                expenseContract.Entry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order   */
        Cursor cursor = getContentResolver().query(expenseContract.Entry.CONTENT_URI, projection, null, null, null);
        ListView lvItems = (ListView) findViewById(R.id.list_item);
        expenseAdapter todoAdapter = new expenseAdapter(this, cursor);
        lvItems.setAdapter(todoAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, AddActivity.class);
                Uri current = ContentUris.withAppendedId(expenseContract.Entry.CONTENT_URI, id);
                intent.setData(current);
                startActivity(intent);
            }
        });
        /*TextView displayView = (TextView) findViewById(R.id.text_view);

        try {

            displayView.setText("The table contains " + cursor.getCount() + " Transactions.\n\n");
            displayView.append(expenseContract.Entry._ID + " - " +
                    expenseContract.Entry.COLUMN_TYPE + " - " +
                    expenseContract.Entry.COLUMN_NAME + " - " +
                    expenseContract.Entry.COLUMN_AMOUNT + " - " +
                    expenseContract.Entry.COLUMN_DATE + " - " +
                    expenseContract.Entry.COLUMN_CATEGORY + "\n");

            int idcolumn = cursor.getColumnIndex(expenseContract.Entry._ID);
            int typecolumn = cursor.getColumnIndex(expenseContract.Entry.COLUMN_TYPE );
            int namecolumn = cursor.getColumnIndex(expenseContract.Entry.COLUMN_NAME );
            int  amountcolumn= cursor.getColumnIndex( expenseContract.Entry.COLUMN_AMOUNT);
            int datecolumn = cursor.getColumnIndex( expenseContract.Entry.COLUMN_DATE);
            int categorycolumn = cursor.getColumnIndex( expenseContract.Entry.COLUMN_CATEGORY);

            while (cursor.moveToNext()) {

                int currentID = cursor.getInt(idcolumn);
                String currentType = cursor.getString(typecolumn);
                String currentName = cursor.getString(namecolumn);
                int currentamount = cursor.getInt(amountcolumn);
                int currentdate = cursor.getInt(datecolumn);
                String currentcategory = cursor.getString(categorycolumn);

                displayView.append(("\n" + currentID + " - " +
                        currentType + " - " +
                        currentName + " - " +
                        currentamount + " - " +
                        currentdate + " - " +
                        currentcategory));
            }
        }
        finally {
           cursor.close();
        }*/
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure? Delete all Transactions?");
        builder.setPositiveButton("DELETE ALL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
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

        int rowsDeleted = getContentResolver().delete(expenseContract.Entry.CONTENT_URI, null, null);
           /* if (currenttype == 1) {
                mBalance = mBalance + currentamount;
                mWithdrawl = mWithdrawl - currentamount;
            } else {
                mBalance = mBalance - currentamount;
                mDeposit = mDeposit - currentamount;
            }*/
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Temp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("Balance", 0);
        editor.putInt("Withdrawl", 0);
        editor.putInt("Deposit", 0);
        editor.commit();
        if (rowsDeleted == 0) {
            Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion Succcessful", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, this.getClass());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


