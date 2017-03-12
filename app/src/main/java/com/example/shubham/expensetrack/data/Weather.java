package com.example.shubham.expensetrack.data;

/**
 * Created by Shubham on 20-01-2017.
 */
public class Weather {
    public int icon;
    public String title;
    public Weather(){
        super();
    }

    public Weather(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }
}