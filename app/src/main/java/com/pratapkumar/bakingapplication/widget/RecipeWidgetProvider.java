
package com.pratapkumar.bakingapplication.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.activities.RecipeActivity;
import com.pratapkumar.bakingapplication.activities.RecipeDetailsActivity;
import com.pratapkumar.bakingapplication.models.Ingredient;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.models.Step;
import com.pratapkumar.bakingapplication.utilities.Constant;

import java.util.List;


public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getIngredientListRemoteView(context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    static void updateAppWidgetTitle(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getTitleRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static RemoteViews getTitleRemoteView(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPreferences.getString(context.getString(R.string.pref_recipe_name_key), Constant.DEFAULT_STRING);
        int recipeId = sharedPreferences.getInt(context.getString(R.string.pref_recipe_id_key), Constant.DEFAULT_INTEGER);
        String ingredientString = sharedPreferences.getString(context.getString(R.string.pref_ingredient_list_key),  Constant.DEFAULT_STRING);
        String stepString = sharedPreferences.getString(context.getString(R.string.pref_step_list_key),  Constant.DEFAULT_STRING);
        int servings =  sharedPreferences.getInt(context.getString(R.string.pref_servings_key), Constant.DEFAULT_INTEGER_FOR_SERVINGS);
        String image = sharedPreferences.getString(context.getString(R.string.pref_image_key),  Constant.DEFAULT_STRING);

        Intent intent;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);


        if (ingredientString.isEmpty() || stepString.isEmpty()) {
            intent = new Intent(context, RecipeActivity.class);
            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.app_name));
        } else {
            intent = new Intent(context, RecipeDetailsActivity.class);

            List<Ingredient> ingredientList = Constant.toIngredientList(ingredientString);
            List<Step> stepList = Constant.toStepList(stepString);
            Log.d("list", "ingredientList: "+ingredientList);

            Recipe recipe = new Recipe(recipeId, recipeName, ingredientList, stepList, servings, image);

            Bundle b = new Bundle();
            b.putParcelable(Constant.RECIPE, recipe);
            intent.putExtra(Constant.RECIPE, b);

            views.setTextViewText(R.id.widget_recipe_name, recipeName);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Constant.WIDGET_PENDING_INTENT_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

        return views;
    }


    private static RemoteViews getIngredientListRemoteView(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            updateAppWidgetTitle(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

