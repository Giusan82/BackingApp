package com.example.android.bakingapp.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.android.bakingapp.FavoriteRecipeWidget;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.MainActivity;

import timber.log.Timber;

public class BackingServices extends IntentService {

    public static final String ACTION_INGREDIENT_LIST = "com.example.android.bakingapp.action.ingredients_list";
    public static final String ACTION_UPDATE_INGREDIENT_LIST = "com.example.android.bakingapp.action.update_ingredients_list";

    public BackingServices() {
        super("BackingServices");
    }

    public static void startServices(Context context){
        Intent intent = new Intent(context, BackingServices.class);
        intent.setAction(ACTION_INGREDIENT_LIST);
        context.startService(intent);
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context, BackingServices.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_INGREDIENT_LIST.equals(action)){
                Timber.d("Service of ACTION_INGREDIENT_LIST");
            }else if(ACTION_UPDATE_INGREDIENT_LIST.equals(action)){

                //provide to update the widgets
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteRecipeWidget.class));
                //Trigger data update to handle the GridView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
                FavoriteRecipeWidget.onUpdateWidget(this, appWidgetManager, appWidgetIds);
                Timber.d("Service of ACTION_UPDATE_INGREDIENT_LIST");
            }
        }
    }
}
