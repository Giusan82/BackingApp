package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.Services.BackingServices;
import com.example.android.bakingapp.ui.MainActivity;
import com.example.android.bakingapp.ui.StepsActivity;
import com.example.android.bakingapp.utilities.GridWidgetService;
import com.example.android.bakingapp.utilities.RecipesData;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteRecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String recipe_name = recipe_name = RecipesData.getRecipeName(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);
        views.setTextViewText(R.id.recipe_name, recipe_name);

        //open the app when the widget is clicked on the name
//        Intent openActivity = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent =  PendingIntent.getActivity(context, 0, openActivity, 0);
//        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

//        //start a service when the widget is clicked on the name
//        Intent startService = new Intent(context, BackingServices.class);
//        startService.setAction(BackingServices.ACTION_INGREDIENT_LIST);
//        PendingIntent startServicePendingIntent = PendingIntent.getService(context, 0, startService, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.recipe_name, startServicePendingIntent);

        Intent listService = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, listService);

        Intent appIntent = new Intent(context, StepsActivity.class);
        PendingIntent appPendingIntentTemplate = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntentTemplate);
        // Handle empty view
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BackingServices.updateWidgets(context);
    }

    public static void onUpdateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Timber.d("Widget Created");
        BackingServices.updateWidgets(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {
//        final String action = intent.getAction();
//        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
//            // refresh all your widgets
//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, FavoriteRecipeWidget.class);
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_grid_view);
//        }
//        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        BackingServices.updateWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }


}
