package com.example.android.bakingapp.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.FavoriteRecipeWidget;
import com.example.android.bakingapp.R;

public class BackingServices extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENT_LIST = "com.example.android.bakingapp.action.update_ingredients_list";

    public BackingServices() {
        super("BackingServices");
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context, BackingServices.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            //provide to update the widgets
            if (ACTION_UPDATE_INGREDIENT_LIST.equals(action)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteRecipeWidget.class));

                //Trigger data update to handle the GridView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
                FavoriteRecipeWidget.onUpdateWidget(this, appWidgetManager, appWidgetIds);
            }
        }
    }
}
