package com.example.android.bakingapp.utilities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.MainActivity;
import com.example.android.bakingapp.ui.StepsActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import timber.log.Timber;

public class GridWidgedAdapter implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private ArrayList mItems = new ArrayList();
    private RecipesData[] mRecipes;
    private RecipesData.Steps[] mSteps;
    private RecipesData.Ingredients[] mIngredients;
    private int mPosition = 0;



    public GridWidgedAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        fetchingData();
    }

    private void fetchingData(){
        mPosition = RecipesData.getPosition(mContext);
        ApiRequest apiRequest = new ApiRequest(mContext);
        apiRequest.get(new ApiRequest.Callback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mRecipes = gson.fromJson(result, RecipesData[].class);
                mSteps = mRecipes[mPosition].getSteps();
                mIngredients = mRecipes[mPosition].getIngredients();

                clear();
//                for(int i = 0; i < data.length; i++){
//                    mItems.add(data[i]);
//
//                }



                for(int i = 0; i < mIngredients.length; i++){
                    mItems.add(mIngredients[i]);
                }

            }
        }, false);
    }

    private void clear() {
        //clear the arraylist
        this.mItems.clear();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mItems == null) return 0;
        return mItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RecipesData.Ingredients current = (RecipesData.Ingredients) mItems.get(position);
        Log.d("getViewAt", current.getIngredient());
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_layout);
        views.setTextViewText(R.id.tw_list_recipe_name, mContext.getString(R.string.ingredients, position+1, current.getQuantity(), current.getMeasure(), current.getIngredient()));
        Intent openSteps = new Intent();
        openSteps.putExtra(RecipesData.EXTRA_STEPS, mSteps);
        openSteps.putExtra(RecipesData.EXTRA_INGREDIENTS, mIngredients);
        openSteps.putExtra(RecipesData.EXTRA_RECIPE_NAME, mRecipes[mPosition].getName());
        views.setOnClickFillInIntent(R.id.tw_list_recipe_name, openSteps);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
