package com.example.shubham.expensetrack.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shubham.expensetrack.AddActivity;
import com.example.shubham.expensetrack.R;
import com.example.shubham.expensetrack.data.expenseContract;
import com.example.shubham.expensetrack.expenseAdapter;

/**
 * Created by Shubham on 13-01-2017.
 */
public class Current extends AppCompatActivity {
    public TextView u;
    public TextView v;
    public TextView w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });
        u=(TextView)findViewById(R.id.title1);
        v=(TextView)findViewById(R.id.amnt);
        w=(TextView)findViewById(R.id.title2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Temp", Context.MODE_PRIVATE);
        int counter = sharedPrefs.getInt("Withdrawl", 0);
        displayDatabaseInfo();
        u.setText("Total Withdrawls =");
        v.setText(" "+counter);
        w.setText("All Withdrwals Transactions :");

    }

    private void displayDatabaseInfo() {

        String[] projection = {
                expenseContract.Entry._ID,
                expenseContract.Entry.COLUMN_TYPE,
                expenseContract.Entry.COLUMN_NAME,
                expenseContract.Entry.COLUMN_AMOUNT,
                expenseContract.Entry.COLUMN_DATE,
                expenseContract.Entry.COLUMN_CATEGORY};
        String selection = expenseContract.Entry.COLUMN_TYPE + " LIKE ?";
        String[] selectionArgs = { "1" };


        Cursor cursor = getContentResolver().query(expenseContract.Entry.CONTENT_URI, projection, selection, selectionArgs, null);
        ListView lvItems = (ListView) findViewById(R.id.list_item);
        expenseAdapter todoAdapter = new expenseAdapter(this, cursor);
        lvItems.setAdapter(todoAdapter);
    }
}