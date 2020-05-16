
package com.pratapkumar.bakingapplication.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.preference.PreferenceManager;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Ingredient;
import com.pratapkumar.bakingapplication.utilities.Constant;

import java.util.List;


public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private List<Ingredient> mIngredientList;

        public ListRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String ingredientString = sharedPreferences.getString(getString(R.string.pref_ingredient_list_key), "");
            mIngredientList = Constant.toIngredientList(ingredientString);
        }

        @Override
        public void onDestroy() {

        }


        @Override
        public int getCount() {
            if (mIngredientList == null) return 0;
            return mIngredientList.size();
        }


        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredientList == null || mIngredientList.size() == 0) return null;

            Ingredient ingredient = mIngredientList.get(position);

            double quantity = ingredient.getmQuantity();
            String measure = ingredient.getmMeasure();
            String ingredientName = ingredient.getmIngredient();

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

            views.setTextViewText(R.id.widget_quantity, String.valueOf(quantity));
            views.setTextViewText(R.id.widget_measure, measure);
            views.setTextViewText(R.id.widget_ingredient, ingredientName);
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}