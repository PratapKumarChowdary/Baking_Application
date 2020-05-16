package com.pratapkumar.bakingapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.pratapkumar.bakingapplication.api.BakingHelper;
import com.pratapkumar.bakingapplication.api.BakingService;
import com.pratapkumar.bakingapplication.utilities.Constant;
import com.pratapkumar.bakingapplication.utilities.GridAutofitLayoutManager;
import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.adapters.RecipeAdapter;
import com.pratapkumar.bakingapplication.utilities.SimpleIdlingResource;
import com.pratapkumar.bakingapplication.widget.RecipeWidgetProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pratapkumar.bakingapplication.utilities.Constant.GRID_COLUMN_WIDTH;


public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener {

    private RecipeAdapter mRecipeAdapter;
    private BakingService mBakingService;

    GridAutofitLayoutManager layoutManager;

    @BindView(R.id.recipesList)
    RecyclerView recipesListRecyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(RecipeActivity.this);
        mBakingService = BakingHelper.getInstance(RecipeActivity.this);
        layoutManager = new GridAutofitLayoutManager(
                this, GRID_COLUMN_WIDTH);

        getIdlingResource();
        getAllRecipes();
    }

    private void getAllRecipes() {
        if (mIdlingResource!=null)
            mIdlingResource.setIdleState(false);

        Call<List<Recipe>> call = mBakingService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                if(response.isSuccessful()){
                    mRecipeAdapter = new RecipeAdapter(response.body(),getApplicationContext(),RecipeActivity.this);
                    recipesListRecyclerView.setLayoutManager(layoutManager);
                    recipesListRecyclerView.setAdapter(mRecipeAdapter);
                }

                mIdlingResource.setIdleState(true);


            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                mIdlingResource.setIdleState(true);

            }
        });

    }

    @Override
    public void onRecipeItemClicked(Recipe recipe) {
        Bundle b = new Bundle();
        b.putParcelable(Constant.RECIPE, recipe);

        updateSharedPreference(recipe);
        sendBroadcastToWidget();

        Intent intent = new Intent(RecipeActivity.this, RecipeDetailsActivity.class);
        intent.putExtra(Constant.RECIPE,b);
        startActivity(intent);
    }

    private void updateSharedPreference(Recipe recipe) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String ingredientString = Constant.toIngredientString(recipe.getmIngredients());

        editor.putString(getString(R.string.pref_ingredient_list_key), ingredientString);
        editor.putString(getString(R.string.pref_recipe_name_key), recipe.getmName());

        String stepString = Constant.toStepString(recipe.getmSteps());

        editor.putInt(getString(R.string.pref_recipe_id_key), recipe.getmId());
        editor.putString(getString(R.string.pref_step_list_key), stepString);
        editor.putString(getString(R.string.pref_image_key), recipe.getmImage());
        editor.putInt(getString(R.string.pref_servings_key), recipe.getmServings());

        editor.apply();
    }

    private void sendBroadcastToWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        Intent updateAppWidgetIntent = new Intent();
        updateAppWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAppWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(updateAppWidgetIntent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
