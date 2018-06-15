package com.example.android.bakingapp.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

public class SharedUtilities {

    //Constructor
    public SharedUtilities(){}

    //source:  Udacity reviewer
    public int numberOfColumns(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 1; //to keep the grid aspect
        return nColumns;
    }
}
