package com.example.shubham.expensetrack;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CursorAdapter;

import com.example.shubham.expensetrack.data.expenseContract;

/**
 * Created by Shubham on 12-01-2017.
 */
public class expenseAdapter extends CursorAdapter{
    public expenseAdapter(Context context, Cursor c)
    {

        super(context,c,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expenselist, parent, false);
    }
   // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView name = (TextView) view.findViewById(R.id.mname);
        TextView amount = (TextView) view.findViewById(R.id.mamount);
        TextView category = (TextView) view.findViewById(R.id.mcategory);
        TextView date  = (TextView) view.findViewById(R.id.mdate);
        ImageView symbol = (ImageView) view.findViewById(R.id.symbol);
        ImageView database = (ImageView) view.findViewById(R.id.database);

        String sname = cursor.getString(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_NAME));
        String scategory = cursor.getString(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_CATEGORY));
        int samount = cursor.getInt(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_AMOUNT));
        String sdate = cursor.getString(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_DATE));
        int stype = cursor.getInt(cursor.getColumnIndexOrThrow(expenseContract.Entry.COLUMN_TYPE));

        name.setText(sname);
       amount.setText(String.valueOf(samount));
        category.setText(scategory);
        date.setText(String.valueOf(sdate));
        if(stype==1)
        {   symbol.setImageResource(R.drawable.inr);
            amount.setTextColor(Color.parseColor("#f62929"));
            database.setImageResource(R.drawable.database_minus);
        }
        else
        { symbol.setImageResource(R.drawable.inr1);
            amount.setTextColor(Color.parseColor("#7CB342"));
            database.setImageResource(R.drawable.database_plus);
        }
    }

}
