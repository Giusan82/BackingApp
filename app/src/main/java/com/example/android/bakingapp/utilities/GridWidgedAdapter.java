package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GridWidgedAdapter implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList mItems = new ArrayList();
    private RecipesData[] mRecipes;
    private RecipesData.Steps[] mSteps;
    private RecipesData.Ingredients[] mIngredients;
    private int mPosition;

    public GridWidgedAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mPosition = RecipesData.getPosition(mContext);

        //parses data from saved json string when a update is triggered
        if (mPosition != RecipesData.PREF_KEY_DEFAULT_POSITION) {
            String result = RecipesData.getJsonResponse(mContext);
            Gson gson = new Gson();
            mRecipes = gson.fromJson(result, RecipesData[].class);
            mSteps = mRecipes[mPosition].getSteps();
            mIngredients = mRecipes[mPosition].getIngredients();

            this.mItems.clear();

            for (int i = 0; i < mIngredients.length; i++) {
                mItems.add(mIngredients[i]);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mItems == null) return 0;
        return mItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < mIngredients.length) {
            RecipesData.Ingredients current = (RecipesData.Ingredients) mItems.get(position);
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_layout);
            views.setTextViewText(R.id.tw_list_recipe_name, mContext.getString(R.string.ingredients, position + 1, current.getQuantity(), current.getMeasure(), current.getIngredient()));
            Intent openSteps = new Intent();
            openSteps.putExtra(RecipesData.EXTRA_STEPS, mSteps);
            openSteps.putExtra(RecipesData.EXTRA_INGREDIENTS, mIngredients);
            openSteps.putExtra(RecipesData.EXTRA_RECIPE_NAME, mRecipes[mPosition].getName());
            views.setOnClickFillInIntent(R.id.tw_list_recipe_name, openSteps);
            return views;
        } else {
            return null;
        }

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the ListView the same
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
