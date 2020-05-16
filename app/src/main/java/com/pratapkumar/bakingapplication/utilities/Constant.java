package com.pratapkumar.bakingapplication.utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Ingredient;
import com.pratapkumar.bakingapplication.models.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public final class Constant {

    public static final String RECIPE= "RECIPE";
    public static final String STEP= "STEP";

    public static final int INGREDIENTS = 0;
    public static final int STEPS = 1;

    public static final String EXTRA_STEP_INDEX = "step_index";

    public static final String[] TAP_TITLE = new String[] {"Ingredients", "Steps"};
    public static final int PAGE_COUNT = TAP_TITLE.length;

    public static final String SAVE_STEP = "save_step";
    public static final String STATE_STEP_INDEX = "state_step_index";
    public static final String STATE_PLAYBACK_POSITION = "state_playback_position";
    public static final String STATE_CURRENT_WINDOW = "state_current_window";
    public static final String STATE_PLAY_WHEN_READY = "state_play_when_ready";

    public static final int GRID_COLUMN_WIDTH = 520;
    public static final int GRID_COLUMN_WIDTH_DEFAULT = 48;
    public static final int GRID_SPAN_COUNT = 1;

    public static final int POSITION_ZERO = 0;
    public static final int POSITION_ONE = 1;
    public static final int POSITION_TWO = 2;
    public static final int POSITION_THREE = 3;
    public static final int NUM_POSITION_FOUR = 4;

    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INTEGER = 1;
    public static final int DEFAULT_INTEGER_FOR_SERVINGS = 8;


    public static final int WIDGET_PENDING_INTENT_ID = 0;

    public static final String NAME_OKHTTP= "OkHttp";
    public static final String RECIPE_NAME_AT_ZERO = "Nutella Pie";
    public static final String RECIPE_NAME_AT_ONE = "Brownies";

    public static final String EXTRA_RECIPE = "recipe";


    /** Constants for ExoPlayer */
    public static final float PLAYER_PLAYBACK_SPEED = 1f;
    public static final int REWIND_INCREMENT = 3000;
    public static final int FAST_FORWARD_INCREMENT = 3000;
    public static final int START_POSITION = 0;

    public static int getImageResource(int position) {
        int imageResourceId;
        switch (position % NUM_POSITION_FOUR) {
            case POSITION_ZERO:
                imageResourceId = R.drawable.nutella_pie;
                break;
            case POSITION_ONE:
                imageResourceId = R.drawable.brownies;
                break;
            case POSITION_TWO:
                imageResourceId = R.drawable.yellow_cake;
                break;
            case POSITION_THREE:
                imageResourceId = R.drawable.cheesecake;
                break;
            default:
                imageResourceId = R.drawable.cake;
                break;
        }
        return imageResourceId;
    }

    public static String toIngredientString(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(ingredientList, listType);
    }

    public static List<Step> toStepList(String stepString) {
        if (stepString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type stepListType = new TypeToken<List<Step>>() {}.getType();
        return gson.fromJson(stepString, stepListType);
    }

    public static List<Ingredient> toIngredientList(String ingredientString) {
        if (ingredientString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredientString, listType);
    }

    public static String toStepString(List<Step> stepList) {
        if (stepList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type stepListType = new TypeToken<List<Step>>() {}.getType();
        return gson.toJson(stepList, stepListType);
    }
}
