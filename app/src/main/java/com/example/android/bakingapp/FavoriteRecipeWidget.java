package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.Services.BackingServices;
import com.example.android.bakingapp.ui.MainActivity;
import com.example.android.bakingapp.ui.StepsActivity;
import com.example.android.bakingapp.Services.GridWidgetService;
import com.example.android.bakingapp.utilities.RecipesData;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteRecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String recipe_name;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);

        if (RecipesData.getRecipeName(context).equals("")) {
            recipe_name = context.getString(R.string.widget_title);
            //open the app when the widget is clicked on the name, when there are no recipe saved, open the MainActivity
            Intent openActivity = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openActivity, 0);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
        } else {
            recipe_name = RecipesData.getRecipeName(context);
        }
        views.setTextViewText(R.id.recipe_name, recipe_name);

        //sets the remoteview adapter
        Intent listService = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, listService);

        //gets the intents and performs the action
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
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        //update the widget when its configuration changes
        BackingServices.updateWidgets(context);
    }
}

